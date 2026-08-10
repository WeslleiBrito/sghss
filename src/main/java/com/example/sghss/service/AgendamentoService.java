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

        if (dto.dataHoraAgendada().isBefore(escala.getDataHoraInicio()) ||
                dto.dataHoraAgendada().isAfter(escala.getDataHoraFim())) {
            throw new BusinessException("O horário solicitado está fora do turno de trabalho desta escala.");
        }

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
        agendamento.setEscala(escala);
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
        Escala escala = escalaRepository.findById(escalaId)
                .orElseThrow(() -> new EntityNotFoundException("Escala não encontrada."));

        List<LocalDateTime> horariosOcupados = agendamentoRepository.findHorariosOcupadosPorEscala(escalaId);

        List<HorarioDisponivelDTO> gradeDisponivel = new ArrayList<>();

        int duracaoConsultaMinutos = 30;
        LocalDateTime slotAtual = escala.getDataHoraInicio() ;

        while (slotAtual.isBefore(escala.getDataHoraFim())) {

            boolean estaLivre = !horariosOcupados.contains(slotAtual) && slotAtual.isAfter(LocalDateTime.now()) ;
            String horaString = slotAtual.toLocalTime().toString();

            if (estaLivre) {
                gradeDisponivel.add(new HorarioDisponivelDTO(slotAtual, horaString, true));
            }

            slotAtual = slotAtual.plusMinutes(duracaoConsultaMinutos) ;
        }

        return gradeDisponivel;
    }


    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarAgendamentosDoDia() {

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
        return agendamentoRepository.findByPacienteIdOrderByDataHoraAgendadaDesc(pacienteId)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> buscarAgendaPorData(LocalDate data) {
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(23, 59, 59);

        return agendamentoRepository.findByDataHoraAgendadaBetween(inicioDoDia, fimDoDia)
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarMinhaFila(Usuario usuarioLogado) {

        ProfissionalSaude profissional = profissionalSaudeRepository.findByPessoaFisicaId(usuarioLogado.getPessoaFisica().getId())
                .orElseThrow(() -> new BusinessException("O usuário logado não possui um registro ativo de Profissional de Saúde."));

        return agendamentoRepository.findByEscalaColaboradorIdOrderByDataHoraAgendadaAsc(profissional.getId())
                .stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
    }
}