package com.example.sghss.service;

import com.example.sghss.dto.request.AgendamentoCreateDTO;
import com.example.sghss.dto.response.AgendamentoResponseDTO;
import com.example.sghss.exception.BusinessException;
import com.example.sghss.model.*;
import com.example.sghss.model.enums.TipoAtendimento;
import com.example.sghss.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private EscalaRepository escalaRepository;
    @Mock private EspecialidadeRepository especialidadeRepository;

    private Paciente pacienteMock;
    private Escala escalaMock;
    private Especialidade especialidadeMock;
    private AgendamentoCreateDTO dtoMock;

    @BeforeEach
    void setUp() {
        // Preparando os dados falsos (Mocks) para os testes
        pacienteMock = new Paciente();
        pacienteMock.setId(UUID.randomUUID());
        PessoaFisica pfPaciente = new PessoaFisica();
        pfPaciente.setNome("João Silva");
        pacienteMock.setPessoaFisica(pfPaciente);

        escalaMock = new Escala();
        escalaMock.setId(UUID.randomUUID());
        escalaMock.setDataHoraInicio(LocalDate.now().plusDays(1).atTime(8, 0));
        escalaMock.setDataHoraFim(LocalDate.now().plusDays(1).atTime(12, 0));

        ProfissionalSaude medico = new ProfissionalSaude();
        medico.setId(UUID.randomUUID());
        PessoaFisica pfMedico = new PessoaFisica();
        pfMedico.setNome("Dr. Roberto");
        medico.setPessoaFisica(pfMedico);
        escalaMock.setColaborador(medico);

        Clinica clinica = new Clinica();
        clinica.setId(UUID.randomUUID());
        Instituicao inst = new Instituicao();
        inst.setRazaoSocial("Clinica Central");
        clinica.setInstituicao(inst);
        escalaMock.setUnidadeSaude(clinica);

        especialidadeMock = new Especialidade();
        especialidadeMock.setId(UUID.randomUUID());
        especialidadeMock.setNome("Cardiologia");

        dtoMock = new AgendamentoCreateDTO(
                pacienteMock.getId(),
                escalaMock.getId(),
                LocalDate.now().plusDays(1).atTime(9, 0),
                especialidadeMock.getId(),
                TipoAtendimento.CONSULTA_ROTINA,
                "Primeira vez"
        );
    }

    @Test
    @DisplayName("CT-01: Deve agendar com sucesso quando não há conflitos")
    void deveAgendarComSucesso() {
        // Configurando o comportamento dos repositórios falsos
        when(pacienteRepository.findById(pacienteMock.getId())).thenReturn(Optional.of(pacienteMock));
        when(escalaRepository.findById(escalaMock.getId())).thenReturn(Optional.of(escalaMock));
        when(especialidadeRepository.findById(especialidadeMock.getId())).thenReturn(Optional.of(especialidadeMock));

        // Simulando que NÃO existe conflito
        when(agendamentoRepository.existeConflitoHorarioProfissional(any(), any())).thenReturn(false);
        when(agendamentoRepository.existeConflitoHorarioPaciente(any(), any())).thenReturn(false);

        // Simulando o salvamento no banco
        Agendamento agendamentoSalvo = new Agendamento();
        agendamentoSalvo.setId(UUID.randomUUID());
        agendamentoSalvo.setPaciente(pacienteMock);
        agendamentoSalvo.setEscala(escalaMock);
        agendamentoSalvo.setEspecialidade(especialidadeMock);
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamentoSalvo);

        // Ação
        AgendamentoResponseDTO response = agendamentoService.agendar(dtoMock);

        // Verificação
        assertNotNull(response);
        assertEquals("João Silva", response.nomePaciente());
        verify(agendamentoRepository, times(1)).save(any(Agendamento.class));
    }

    @Test
    @DisplayName("CT-02: Deve lançar exceção se o médico já tiver consulta no horário")
    void deveLancarExcecaoQuandoConflitoMedico() {
        when(pacienteRepository.findById(pacienteMock.getId())).thenReturn(Optional.of(pacienteMock));
        when(escalaRepository.findById(escalaMock.getId())).thenReturn(Optional.of(escalaMock));

        // Simulando que JÁ EXISTE conflito para o médico
        when(agendamentoRepository.existeConflitoHorarioProfissional(any(), any())).thenReturn(true);

        // Ação & Verificação
        BusinessException exception = assertThrows(BusinessException.class, () -> agendamentoService.agendar(dtoMock));

        assertEquals("O profissional já possui um atendimento marcado para este horário.", exception.getMessage());
        verify(agendamentoRepository, never()).save(any(Agendamento.class)); // Garante que não tentou salvar
    }
}