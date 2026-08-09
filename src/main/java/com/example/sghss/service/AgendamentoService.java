package com.example.sghss.service;

import com.example.sghss.dto.request.AgendamentoCreateDTO;
import com.example.sghss.dto.response.AgendamentoResponseDTO;
import com.example.sghss.dto.response.HorarioDisponivelDTO;
import com.example.sghss.exception.BusinessException;
import com.example.sghss.model.*;
import com.example.sghss.model.enums.StatusAgendamento;
import com.example.sghss.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final EscalaRepository escalaRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ProfissionalSaudeRepository profissionalSaudeRepository;

    @Transactional
    public AgendamentoResponseDTO agendar(AgendamentoCreateDTO dto) {

        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado."));

        Escala escala = escalaRepository.findById(dto.escalaId())
                .orElseThrow(() -> new EntityNotFoundException("Escala não encontrada."));

        // Garantir que a hora está dentro da escala do profissional
        if (dto.dataHoraAgendada().isBefore(escala.getDataHoraInicio()) ||
                dto.dataHoraAgendada().isAfter(escala.getDataHoraFim())) {
            throw new BusinessException("O horário solicitado está fora do turno de trabalho desta escala.");
        }

        // Checagem de Conflitos pegando o ID de dentro da Escala!
        if (agendamentoRepository.existeConflitoHorarioProfissional(escala.getColaborador().getId(), dto.dataHoraAgendada())) {
            throw new BusinessException("O profissional já possui um atendimento marcado para este horário.");
        }

        if (agendamentoRepository.existeConflitoHorarioPaciente(paciente.getId(), dto.dataHoraAgendada())) {
            throw new BusinessException("O paciente já possui um atendimento marcado para este mesmo horário.");
        }

        Agendamento agendamento = new Agendamento();
        String anoAtual = String.valueOf(java.time.LocalDate.now().getYear());
        String codigoUnico = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        agendamento.setCodigoAgendamento("AGE-" + anoAtual + "-" + codigoUnico);
        agendamento.setPaciente(paciente);
        agendamento.setEscala(escala); // Só precisa setar a escala!
        agendamento.setTipoAtendimento(dto.tipoAtendimento());
        agendamento.setDataHoraAgendada(dto.dataHoraAgendada());
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);
        agendamento.setObservacoesRecepcao(dto.observacoesRecepcao());

        Especialidade especialidade = especialidadeRepository.findById(dto.especialidadeId())
                .orElseThrow(() -> new EntityNotFoundException("Especialidade não encontrada."));
        agendamento.setEspecialidade(especialidade);

        Agendamento salvo = agendamentoRepository.save(agendamento);
        return AgendamentoResponseDTO.fromEntity(salvo);
    }

    // =========================================================================
    // --- MÉTODOS DE TRANSFORMAÇÃO DE ESTADO (MÁQUINA DE ESTADOS) ---
    // =========================================================================

    @Transactional
    public AgendamentoResponseDTO realizarCheckIn(UUID agendamentoId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));

        agendamento.realizarCheckIn(); // O método da nossa entidade se valida sozinho!
        return AgendamentoResponseDTO.fromEntity(agendamento);
    }

    @Transactional
    public AgendamentoResponseDTO iniciarAtendimento(UUID agendamentoId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));

        UUID profissionalId = agendamento.getEscala().getColaborador().getId();
        boolean temAtendimentoAberto = agendamentoRepository.existsByEscalaColaboradorIdAndStatusAgendamento(
                profissionalId,
                StatusAgendamento.EM_ATENDIMENTO
        );

        if (temAtendimentoAberto) {
            throw new BusinessException("Ação bloqueada: Você já possui um paciente em atendimento. Conclua o prontuário atual antes de chamar o próximo.");
        }
        
        agendamento.iniciarAtendimento();
        return AgendamentoResponseDTO.fromEntity(agendamento);
    }

    @Transactional
    public AgendamentoResponseDTO concluirAtendimento(UUID agendamentoId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));

        agendamento.concluir();
        return AgendamentoResponseDTO.fromEntity(agendamento);
    }

    @Transactional
    public AgendamentoResponseDTO cancelar(UUID agendamentoId, String motivo) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));

        agendamento.cancelar(motivo);
        return AgendamentoResponseDTO.fromEntity(agendamento);
    }

    @Transactional(readOnly = true)
    public List<HorarioDisponivelDTO> listarHorariosDisponiveis(UUID escalaId) {
        // 1. Busca a escala para saber o início e o fim do plantão/ambulatório [source: 5, 7]
        Escala escala = escalaRepository.findById(escalaId)
                .orElseThrow(() -> new EntityNotFoundException("Escala não encontrada."));

        // 2. Busca no banco apenas as datas/horas que JÁ ESTÃO OCUPADAS [source: 6]
        List<LocalDateTime> horariosOcupados = agendamentoRepository.findHorariosOcupadosPorEscala(escalaId);

        List<HorarioDisponivelDTO> gradeDisponivel = new ArrayList<>();

        // 3. Define o tempo do "step" de cada consulta (Ex: 30 em 30 minutos) [source: 7]
        int duracaoConsultaMinutos = 30;
        LocalDateTime slotAtual = escala.getDataHoraInicio() ;

        // 4. Loop que fatia a agenda do início ao fim [source: 5, 7]
        while (slotAtual.isBefore(escala.getDataHoraFim())) {

            // Um horário só está livre se NÃO estiver na lista de ocupados E não for no passado [source: 7]
            boolean estaLivre = !horariosOcupados.contains(slotAtual) && slotAtual.isAfter(LocalDateTime.now()) ;

            // Formata a hora para "HH:mm" (ex: "14:30") para facilitar a criação dos botões no front
            String horaString = slotAtual.toLocalTime().toString();

            // Adiciona na lista que será devolvida
            if (estaLivre) {
                gradeDisponivel.add(new HorarioDisponivelDTO(slotAtual, horaString, true));
            }

            // Pula para o próximo slot (08:00 -> 08:30 -> 09:00...) [source: 7]
            slotAtual = slotAtual.plusMinutes(duracaoConsultaMinutos) ;
        }

        return gradeDisponivel;
    }

    // =========================================================================
    // --- CONSULTAS E LISTAGENS (AS VISÕES DO SISTEMA) ---
    // =========================================================================

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarAgendamentosDoDia() {
        // Pega do primeiro segundo de hoje até o último segundo de hoje
        LocalDateTime inicioDoDia = java.time.LocalDate.now().atStartOfDay();
        LocalDateTime fimDoDia = java.time.LocalDate.now().atTime(java.time.LocalTime.MAX);

        return agendamentoRepository.findByDataHoraAgendadaBetweenOrderByDataHoraAgendadaAsc(inicioDoDia, fimDoDia)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarPorMedico(UUID medicoId) {
        return agendamentoRepository.findByEscalaColaboradorIdOrderByDataHoraAgendadaAsc(medicoId)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarPorPaciente(UUID pacienteId) {
        // Já existia essa query no seu repositório!
        return agendamentoRepository.findByPacienteIdOrderByDataHoraAgendadaDesc(pacienteId)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> buscarAgendaPorData(LocalDate data) {
        LocalDateTime inicioDoDia = data.atStartOfDay(); // 00:00:00
        LocalDateTime fimDoDia = data.atTime(23, 59, 59); // 23:59:59

        return agendamentoRepository.findByDataHoraAgendadaBetween(inicioDoDia, fimDoDia)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    // Adicione no seu AgendamentoService.java:

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarMinhaFila(Usuario usuarioLogado) {
        // 1. Descobre quem é o profissional usando o usuário do Token JWT
        ProfissionalSaude profissional = profissionalSaudeRepository.findByPessoaFisicaId(usuarioLogado.getPessoaFisica().getId())
                .orElseThrow(() -> new BusinessException("O usuário logado não possui um registro ativo de Profissional de Saúde."));

        // 2. Busca a agenda baseada no ID real e seguro dele
        return agendamentoRepository.findByEscalaColaboradorIdOrderByDataHoraAgendadaAsc(profissional.getId())
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }
}