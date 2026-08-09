package com.example.sghss.service;

import com.example.sghss.dto.request.ContatoCreateDTO;
import com.example.sghss.dto.request.EnderecoCreateDTO;
import com.example.sghss.dto.request.PacienteCreateDTO;
import com.example.sghss.dto.request.PessoaFisicaCreateDTO;
import com.example.sghss.dto.response.PacienteResponseDTO;
import com.example.sghss.model.Paciente;
import com.example.sghss.model.PessoaFisica;
import com.example.sghss.model.Prontuario;
import com.example.sghss.repository.PacienteRepository;
import com.example.sghss.repository.ProntuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PessoaFisicaService pessoaFisicaService;

    @Mock
    private ProntuarioRepository prontuarioRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    @DisplayName("Deve cadastrar um paciente e gerar seu prontuário eletrônico automaticamente")
    void deveCadastrarPacienteECriarProntuario() {
        // 1. ARRANGE (Preparação do cenário)
        PessoaFisicaCreateDTO pessoaDTO = new PessoaFisicaCreateDTO(
                "Carlos Silva", "11122233344", LocalDate.of(1990, 1, 1),
                List.of(new ContatoCreateDTO(null, "75999999999", null)),
                List.of(new EnderecoCreateDTO("44000000", "Rua A", "10", null, "Centro", "Feira", "BA"))
        );
        PacienteCreateDTO dto = new PacienteCreateDTO(pessoaDTO, "898000011112222", null, null);

        PessoaFisica pessoaMock = new PessoaFisica();
        pessoaMock.setId(UUID.randomUUID());
        pessoaMock.setNome("Carlos Silva");

        Paciente pacienteMock = new Paciente();
        pacienteMock.setId(UUID.randomUUID());
        pacienteMock.setPessoaFisica(pessoaMock);
        pacienteMock.setCartaoSus("898000011112222");

        // Ensinando o Mockito a fingir que o banco de dados respondeu
        when(pacienteRepository.existsByCartaoSus("898000011112222")).thenReturn(false);
        when(pessoaFisicaService.criarOuRecuperarPessoaFisica(any())).thenReturn(pessoaMock);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteMock);

        // 2. ACT (Execução da ação principal)
        PacienteResponseDTO response = pacienteService.cadastrarPaciente(dto);

        // 3. ASSERT (Verificação das provas)
        assertNotNull(response);
        assertEquals("898000011112222", response.cartaoSus());

        // A PROVA DE FOGO: Verifica se o método save do ProntuarioRepository foi chamado exatamente 1 vez!
        verify(prontuarioRepository, times(1)).save(any(Prontuario.class));
    }

    @Test
    @DisplayName("Não deve permitir o cadastro de um paciente com Cartão SUS já existente")
    void naoDeveCadastrarComCartaoSusDuplicado() {
        // 1. ARRANGE
        PacienteCreateDTO dto = new PacienteCreateDTO(null, "898000011112222", null, null);

        // Simulando que o banco já tem esse cartão SUS
        when(pacienteRepository.existsByCartaoSus(anyString())).thenReturn(true);

        // 2 & 3. ACT e ASSERT (Verifica se a exceção correta foi lançada)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pacienteService.cadastrarPaciente(dto);
        });

        assertEquals("Já existe um paciente cadastrado com este Cartão SUS.", exception.getMessage());

        // Garante que se deu erro, ele NUNCA tentou salvar o prontuário no banco
        verify(prontuarioRepository, never()).save(any());
    }
}