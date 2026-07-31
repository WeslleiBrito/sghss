package com.example.sghss.config;

import com.example.sghss.model.*;
import com.example.sghss.model.PessoaFisica;
import com.example.sghss.model.enums.*;
import com.example.sghss.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    // Injeção de todos os repositórios necessários
    private final UsuarioRepository usuarioRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalSaudeRepository profissionalSaudeRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;
    private final EscalaRepository escalaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final PasswordEncoder passwordEncoder;

    // Gerador de aleatoriedade
    private final Random random = new Random();

    // Dicionários para gerar nomes realistas
    private final String[] NOMES_MASCULINOS = {"Lucas", "Pedro", "João", "Carlos", "Mateus", "Gabriel", "Rafael", "Felipe", "Bruno", "Thiago", "Rodrigo", "Fernando", "Diego", "Marcelo", "André"};
    private final String[] NOMES_FEMININOS = {"Ana", "Maria", "Julia", "Letícia", "Amanda", "Beatriz", "Fernanda", "Mariana", "Camila", "Carolina", "Patrícia", "Aline", "Juliana", "Vanessa", "Laura"};
    private final String[] SOBRENOMES = {"Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes", "Costa", "Ribeiro", "Martins", "Carvalho", "Almeida"};
    private final String[] NOMES_ESPECIALIDADES = {"Cardiologia", "Pediatria", "Ortopedia", "Dermatologia", "Psiquiatria", "Ginecologia", "Neurologia", "Endocrinologia", "Oftalmologia", "Urologia", "Otorrinolaringologia", "Gastroenterologia", "Pneumologia", "Reumatologia", "Infectologia"};

    @Override
    @Transactional
    public void run(String... args) {
        // Trava de segurança: Se já tem usuários, o banco já foi populado, então não faz nada!
        if (usuarioRepository.count() > 0) {
            log.info("Banco de dados já está populado. Pulando o Mega Seeder.");
            return;
        }

        log.info("Iniciando o Mega Seeder: Gerando uma clínica viva e realista para o MVP...");

        // 1. GERAÇÃO ESTRUTURAL
        List<Especialidade> especialidades = gerarEspecialidades();
        Clinica clinicaMatrix = gerarInstituicaoEClinica();

        // 2. CONTAS PADRÕES PARA O PROFESSOR TESTAR O SISTEMA
        gerarContasDeAcessoPadrao(clinicaMatrix);

        // 3. GERAÇÃO DE VOLUME (MÉDICOS E PACIENTES)
        List<ProfissionalSaude> medicos = gerarProfissionaisAleatorios(50, clinicaMatrix, especialidades);
        List<Paciente> pacientes = gerarPacientesAleatorios(200);

        // 4. A MÁGICA: GERANDO ESCALAS E AGENDAMENTOS (PASSADO, PRESENTE E FUTURO)
        gerarEscalasEAgendamentos(medicos, clinicaMatrix, pacientes);

        log.info("Mega Seeder finalizado com sucesso! O MVP está pronto.");
    }

    // =========================================================================================
    // MÉTODOS GERADORES
    // =========================================================================================

    private List<Especialidade> gerarEspecialidades() {
        List<Especialidade> salvas = new ArrayList<>();
        for (String nome : NOMES_ESPECIALIDADES) {
            Especialidade esp = new Especialidade();
            esp.setNome(nome);
            esp.setDescricao("Especialidade focada em " + nome);
            salvas.add(especialidadeRepository.save(esp));
        }
        log.info("{} Especialidades geradas.", salvas.size());
        return salvas;
    }

    private Clinica gerarInstituicaoEClinica() {
        Instituicao instituicao = new Instituicao();
        instituicao.setCnpj("12345678000199");
        instituicao.setRazaoSocial("Grupo SGHSS Saúde S.A.");
        instituicaoRepository.save(instituicao);

        // Assumindo que você tem uma entidade 'Clinica' que herda de 'UnidadeSaude'
        Clinica clinica = new Clinica();
        clinica.setInstituicao(instituicao);
        clinica.getInstituicao().setCnpj("12345678000200");
        clinica.getInstituicao().setRazaoSocial("Clínica Central SGHSS - Matriz");
        clinica.setStatusOperacao(true);
        return unidadeSaudeRepository.save(clinica);
    }

    private void gerarContasDeAcessoPadrao(Clinica clinica) {
        log.info("Gerando contas fixas para a banca de avaliação...");

        criarAtorCompleto("Admin Principal", "00000000001", "admin@sghss.com", "123456", Set.of(PerfilAcesso.ROLE_ADMIN));
        criarAtorCompleto("Ana Recepcionista", "00000000002", "recepcao@sghss.com", "123456", Set.of(PerfilAcesso.ROLE_RECEPCIONISTA));

        // Cria o Dr. Roberto e já amarra ele na Clínica e na Cardiologia
        PessoaFisica pfRoberto = criarPessoa("Dr. Roberto Avaliação", "00000000003", LocalDate.of(1980, 5, 20));
        ProfissionalSaude medico = new ProfissionalSaude();
        medico.setPessoaFisica(pfRoberto);
        medico.setMatricula("MED-001");
        medico.setUnidadeLotacao(clinica);
        medico.setTipoConselho(TipoConselho.CRM);
        medico.setNumeroConselho("12345-BA");
        medico.setUfConselho("BA");

        // --- ADICIONE ESTAS DUAS LINHAS ---
        medico.setAtivo(true);
        medico.setDataAdmissao(LocalDate.now());
        // ----------------------------------

        Especialidade cardio = especialidadeRepository.findByNome("Cardiologia").orElseThrow();
        medico.getEspecialidades().add(cardio);
        profissionalSaudeRepository.save(medico);

        Usuario userMed = new Usuario();
        userMed.setPessoaFisica(pfRoberto);
        userMed.setLogin("medico@sghss.com");
        userMed.setSenha(passwordEncoder.encode("123456"));
        userMed.setPerfisAcesso(Set.of(PerfilAcesso.ROLE_MEDICO));
        userMed.setAtivo(true);
        usuarioRepository.save(userMed);

        // Paciente Fixo para App
        PessoaFisica pfLucas = criarPessoa("Lucas Paciente Fixo", "00000000004", LocalDate.of(1995, 8, 15));
        Paciente paciente = new Paciente();
        paciente.setPessoaFisica(pfLucas);
        paciente.setCartaoSus("898000099999999");
        pacienteRepository.save(paciente);

        Usuario userPac = new Usuario();
        userPac.setPessoaFisica(pfLucas);
        userPac.setLogin("paciente@sghss.com");
        userPac.setSenha(passwordEncoder.encode("123456"));
        userPac.setPerfisAcesso(Set.of(PerfilAcesso.ROLE_PACIENTE));
        userPac.setAtivo(true);
        usuarioRepository.save(userPac);
    }

    private List<ProfissionalSaude> gerarProfissionaisAleatorios(int qtd, Clinica clinica, List<Especialidade> especialidades) {
        List<ProfissionalSaude> salvas = new ArrayList<>();
        for (int i = 1; i <= qtd; i++) {
            String nome = sortearNomeRandomico();
            String cpf = String.format("1111111%04d", i); // Garante CPF único no Seeder

            PessoaFisica pf = criarPessoa(nome, cpf, LocalDate.of(1960 + random.nextInt(35), random.nextInt(12) + 1, random.nextInt(28) + 1));

            ProfissionalSaude profissional = new ProfissionalSaude();
            profissional.setPessoaFisica(pf);
            profissional.setMatricula("MAT-" + (1000 + i));
            profissional.setUnidadeLotacao(clinica);
            profissional.setTipoConselho(random.nextBoolean() ? TipoConselho.CRM : TipoConselho.COFEN);
            profissional.setNumeroConselho((10000 + i) + "-BA");
            profissional.setUfConselho("BA");

            // --- ADICIONE ESTAS DUAS LINHAS ---
            profissional.setAtivo(true);
            profissional.setDataAdmissao(LocalDate.now().minusDays(random.nextInt(1000))); // Admissão aleatória
            // ----------------------------------

            // Sorteia de 1 a 2 especialidades para este médico
            profissional.getEspecialidades().add(especialidades.get(random.nextInt(especialidades.size())));
            if (random.nextBoolean()) {
                profissional.getEspecialidades().add(especialidades.get(random.nextInt(especialidades.size())));
            }

            salvas.add(profissionalSaudeRepository.save(profissional));
        }
        log.info("{} Profissionais de Saúde gerados aleatoriamente.", qtd);
        return salvas;
    }

    private List<Paciente> gerarPacientesAleatorios(int qtd) {
        List<Paciente> salvas = new ArrayList<>();
        for (int i = 1; i <= qtd; i++) {
            String nome = sortearNomeRandomico();
            String cpf = String.format("2222222%04d", i);
            String cartaoSus = String.format("8980000%08d", i);

            PessoaFisica pf = criarPessoa(nome, cpf, LocalDate.of(1940 + random.nextInt(70), random.nextInt(12) + 1, random.nextInt(28) + 1));

            Paciente paciente = new Paciente();
            paciente.setPessoaFisica(pf);
            paciente.setCartaoSus(cartaoSus);
            salvas.add(pacienteRepository.save(paciente));
        }
        log.info("{} Pacientes gerados aleatoriamente.", qtd);
        return salvas;
    }

    private void gerarEscalasEAgendamentos(List<ProfissionalSaude> medicos, Clinica clinica, List<Paciente> pacientes) {
        int agendamentosCriados = 0;

        // Pega 10 médicos aleatórios para trabalhar pesado nos próximos 5 dias e nos últimos 2 dias
        for (int i = 0; i < 10; i++) {
            ProfissionalSaude medico = medicos.get(random.nextInt(medicos.size()));

            // Gera escalas de -2 dias (passado) até +5 dias (futuro)
            for (int diasOffset = -2; diasOffset <= 5; diasOffset++) {
                LocalDate dataEscala = LocalDate.now().plusDays(diasOffset);

                // Escala da Manhã: 08:00 às 12:00
                Escala escala = new Escala();
                escala.setColaborador(medico);
                escala.setUnidadeSaude(clinica);
                escala.setTipoAtividade(TipoAtividade.AMBULATORIO);
                escala.setDataHoraInicio(dataEscala.atTime(8, 0));
                escala.setDataHoraFim(dataEscala.atTime(12, 0));
                escala = escalaRepository.save(escala);

                // Agora, lota essa escala com agendamentos! (Preenche de 40% a 80% dos horários)
                LocalDateTime slotAtual = escala.getDataHoraInicio();
                while (slotAtual.isBefore(escala.getDataHoraFim())) {
                    if (random.nextDouble() > 0.3) { // 70% de chance de ter alguém marcado no horário
                        Paciente pacienteSorteado = pacientes.get(random.nextInt(pacientes.size()));

                        Agendamento ag = new Agendamento();
                        ag.setCodigoAgendamento("AGE-" + System.nanoTime());
                        ag.setPaciente(pacienteSorteado);
                        ag.setEscala(escala);
                        ag.setTipoAtendimento(TipoAtendimento.CONSULTA_ROTINA);
                        ag.setDataHoraAgendada(slotAtual);

                        // Lógica temporal inteligente:
                        if (slotAtual.isBefore(LocalDateTime.now())) {
                            // Se é no passado, o paciente ou foi CONCLUIDO ou levou FALTA
                            ag.setStatusAgendamento(random.nextBoolean() ? StatusAgendamento.CONCLUIDO : StatusAgendamento.FALTA);
                        } else if (slotAtual.toLocalDate().isEqual(LocalDate.now()) && slotAtual.isAfter(LocalDateTime.now()) && slotAtual.isBefore(LocalDateTime.now().plusHours(1))) {
                            // Se for hoje e for daqui a pouco, joga na sala de espera (Check-in feito!)
                            ag.setStatusAgendamento(StatusAgendamento.AGUARDANDO_ATENDIMENTO);
                            ag.setDataHoraCheckin(LocalDateTime.now().minusMinutes(15));
                        } else {
                            // Se for amanhã em diante, tá só AGENDADO
                            ag.setStatusAgendamento(StatusAgendamento.AGENDADO);
                        }

                        agendamentoRepository.save(ag);
                        agendamentosCriados++;
                    }
                    slotAtual = slotAtual.plusMinutes(30); // Pula pro próximo slot de meia hora
                }
            }
        }
        log.info("{} Agendamentos (passados e futuros) gerados na esteira com sucesso!", agendamentosCriados);
    }

    // =========================================================================================
    // MÉTODOS AUXILIARES
    // =========================================================================================

    private String sortearNomeRandomico() {
        String nome = random.nextBoolean() ? NOMES_MASCULINOS[random.nextInt(NOMES_MASCULINOS.length)] : NOMES_FEMININOS[random.nextInt(NOMES_FEMININOS.length)];
        String sobrenome1 = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
        String sobrenome2 = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
        // Evita sobrenomes iguais
        if (sobrenome1.equals(sobrenome2)) sobrenome2 = "Brito";

        return nome + " " + sobrenome1 + " " + sobrenome2;
    }

    private PessoaFisica criarPessoa(String nome, String cpf, LocalDate nascimento) {
        PessoaFisica pf = new PessoaFisica();
        pf.setNome(nome);
        pf.setCpf(cpf);
        pf.setDataNascimento(nascimento);
        return pessoaFisicaRepository.save(pf);
    }

    private void criarAtorCompleto(String nome, String cpf, String login, String senhaPura, Set<PerfilAcesso> perfis) {
        PessoaFisica pessoa = criarPessoa(nome, cpf, LocalDate.of(1985, 1, 1));
        Usuario usuario = new Usuario();
        usuario.setPessoaFisica(pessoa);
        usuario.setLogin(login);
        usuario.setSenha(passwordEncoder.encode(senhaPura)); // Senha 123456[cite: 1]
        usuario.setPerfisAcesso(perfis);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }
}