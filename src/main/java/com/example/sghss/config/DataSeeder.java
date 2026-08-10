package com.example.sghss.config;

import com.example.sghss.model.*;
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
    private final ProntuarioRepository prontuarioRepository;

    private final Random random = new Random();

    private final String[] NOMES_MASCULINOS = {"Lucas", "Pedro", "João", "Carlos", "Mateus", "Gabriel", "Rafael", "Felipe", "Bruno", "Thiago"};
    private final String[] NOMES_FEMININOS = {"Ana", "Maria", "Julia", "Letícia", "Amanda", "Beatriz", "Fernanda", "Mariana", "Camila", "Carolina"};
    private final String[] SOBRENOMES = {"Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes"};
    private final String[] NOMES_ESPECIALIDADES = {"Cardiologia", "Pediatria", "Ortopedia", "Dermatologia", "Psiquiatria", "Ginecologia", "Neurologia"};

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Banco de dados já está populado. Pulando o Mega Seeder.");
            return;
        }

        log.info("Iniciando o Mega Seeder: Gerando uma clínica viva e realista para o MVP...");

        List<Especialidade> especialidades = gerarEspecialidades();
        Clinica clinicaMatrix = gerarInstituicaoEClinica();

        ProfissionalSaude drRoberto = gerarContasDeAcessoPadrao(clinicaMatrix);
        List<ProfissionalSaude> medicos = gerarProfissionaisAleatorios(50, clinicaMatrix, especialidades);
        List<Paciente> pacientes = gerarPacientesAleatorios(200);

        log.info("Gerando agenda LOTADA para o Dr. Roberto (Para testes da banca)...");
        gerarAgendaParaMedico(drRoberto, clinicaMatrix, pacientes, especialidades);

        log.info("Gerando agendas aleatórias para o restante do hospital...");
        gerarEscalasEAgendamentos(medicos, clinicaMatrix, pacientes, especialidades);

        log.info("Mega Seeder finalizado com sucesso! O MVP está pronto.");
    }

    private List<Especialidade> gerarEspecialidades() {
        List<Especialidade> salvas = new ArrayList<>();
        for (String nome : NOMES_ESPECIALIDADES) {
            Especialidade esp = new Especialidade();
            esp.setNome(nome);
            esp.setDescricao("Especialidade focada em " + nome);
            salvas.add(especialidadeRepository.save(esp));
        }
        return salvas;
    }

    private Clinica gerarInstituicaoEClinica() {
        Instituicao instituicao = new Instituicao();
        instituicao.setCnpj("12345678000199");
        instituicao.setRazaoSocial("Grupo SGHSS Saúde S.A.");
        instituicaoRepository.save(instituicao);

        Clinica clinica = new Clinica();
        clinica.setInstituicao(instituicao);
        clinica.getInstituicao().setCnpj("12345678000200");
        clinica.getInstituicao().setRazaoSocial("Clínica Central SGHSS - Matriz");
        clinica.setStatusOperacao(true);
        return unidadeSaudeRepository.save(clinica);
    }

    private ProfissionalSaude gerarContasDeAcessoPadrao(Clinica clinica) {
        criarAtorCompleto("Wesllei", "00000000001", "admin.wesllei@sghss.com", "123456", Set.of(PerfilAcesso.ROLE_ADMIN));
        criarAtorCompleto("Ana", "00000000002", "recepcao.ana@sghss.com", "123456", Set.of(PerfilAcesso.ROLE_RECEPCIONISTA));

        PessoaFisica pfRoberto = criarPessoa("Dr. Roberto Avaliação", "00000000003", LocalDate.of(1980, 5, 20));
        ProfissionalSaude medico = new ProfissionalSaude();
        medico.setPessoaFisica(pfRoberto);
        medico.setMatricula("MED-001");
        medico.setUnidadeLotacao(clinica);
        medico.setTipoConselho(TipoConselho.CRM);
        medico.setNumeroConselho("12345-BA");
        medico.setUfConselho("BA");
        medico.setAtivo(true);
        medico.setDataAdmissao(LocalDate.now());

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

        PessoaFisica pfLucas = criarPessoa("Lucas Paciente Fixo", "00000000004", LocalDate.of(1995, 8, 15));
        Paciente paciente = new Paciente();
        paciente.setPessoaFisica(pfLucas);
        paciente.setCartaoSus("898000099999999");
        Paciente pacienteSalvo = pacienteRepository.save(paciente);

        Prontuario prontuarioLucas = new Prontuario();
        prontuarioLucas.setNumeroProntuario("PEP-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        prontuarioLucas.setPaciente(pacienteSalvo);
        prontuarioRepository.save(prontuarioLucas);

        Usuario userPac = new Usuario();
        userPac.setPessoaFisica(pfLucas);
        userPac.setLogin("paciente@sghss.com");
        userPac.setSenha(passwordEncoder.encode("123456"));
        userPac.setPerfisAcesso(Set.of(PerfilAcesso.ROLE_PACIENTE));
        userPac.setAtivo(true);
        usuarioRepository.save(userPac);

        return medico;
    }

    private void gerarAgendaParaMedico(ProfissionalSaude medico, Clinica clinica, List<Paciente> pacientes, List<Especialidade> todasEspecialidades) {

        // ESPECIALIDADE
        Especialidade especialidadeDaConsulta = todasEspecialidades.getFirst();
        if (medico.getEspecialidades() != null && !medico.getEspecialidades().isEmpty()) {
            especialidadeDaConsulta = medico.getEspecialidades().getFirst();
        }

        // Gerando escalas de ontem até 4 dias no futuro
        for (int diasOffset = -1; diasOffset <= 4; diasOffset++) {
            LocalDate dataEscala = LocalDate.now().plusDays(diasOffset);

            Escala escala = new Escala();
            escala.setColaborador(medico);
            escala.setUnidadeSaude(clinica);
            escala.setTipoAtividade(TipoAtividade.AMBULATORIO);
            escala.setDataHoraInicio(dataEscala.atTime(8, 0));
            escala.setDataHoraFim(dataEscala.atTime(12, 0));
            escala = escalaRepository.save(escala);

            LocalDateTime slotAtual = escala.getDataHoraInicio();
            while (slotAtual.isBefore(escala.getDataHoraFim())) {

                if (random.nextBoolean()) {
                    Paciente pacienteSorteado = pacientes.get(random.nextInt(pacientes.size()));

                    Agendamento ag = new Agendamento();
                    ag.setCodigoAgendamento("AGE-" + System.nanoTime());
                    ag.setPaciente(pacienteSorteado);
                    ag.setEscala(escala);
                    ag.setEspecialidade(especialidadeDaConsulta);
                    ag.setTipoAtendimento(TipoAtendimento.CONSULTA_ROTINA);
                    ag.setDataHoraAgendada(slotAtual);

                    if (slotAtual.isBefore(LocalDateTime.now())) {
                        ag.setStatusAgendamento(random.nextBoolean() ? StatusAgendamento.CONCLUIDO : StatusAgendamento.FALTA);
                    } else if (slotAtual.toLocalDate().isEqual(LocalDate.now()) && slotAtual.isBefore(LocalDateTime.now().plusHours(2))) {
                        ag.setStatusAgendamento(StatusAgendamento.AGUARDANDO_ATENDIMENTO);
                        ag.setDataHoraCheckin(LocalDateTime.now().minusMinutes(15));
                    } else {
                        ag.setStatusAgendamento(StatusAgendamento.AGENDADO);
                    }

                    agendamentoRepository.save(ag);
                }
                slotAtual = slotAtual.plusMinutes(30);
            }
        }
    }

    private void gerarEscalasEAgendamentos(List<ProfissionalSaude> medicos, Clinica clinica, List<Paciente> pacientes, List<Especialidade> todasEspecialidades) {
        for (ProfissionalSaude medico : medicos) {
            gerarAgendaParaMedico(medico, clinica, pacientes, todasEspecialidades);
        }
    }

    private String sortearNomeRandomico() {
        String nome = random.nextBoolean() ? NOMES_MASCULINOS[random.nextInt(NOMES_MASCULINOS.length)] : NOMES_FEMININOS[random.nextInt(NOMES_FEMININOS.length)];
        String sobrenome1 = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
        String sobrenome2 = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
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
        usuario.setSenha(passwordEncoder.encode(senhaPura));
        usuario.setPerfisAcesso(perfis);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    private String gerarCpfValido() {
        int[] cpf = new int[11];

        // 1. Gera os 9 primeiros dígitos aleatoriamente
        for (int i = 0; i < 9; i++) {
            cpf[i] = random.nextInt(10);
        }

        // 2. Calcula o primeiro dígito verificador (Posição 9)
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += cpf[i] * (10 - i);
        }
        int resto = soma % 11;
        cpf[9] = (resto < 2) ? 0 : (11 - resto);

        // 3. Calcula o segundo dígito verificador (Posição 10)
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += cpf[i] * (11 - i);
        }
        resto = soma % 11;
        cpf[10] = (resto < 2) ? 0 : (11 - resto);

        // 4. Converte o array para String
        StringBuilder sb = new StringBuilder();
        for (int num : cpf) {
            sb.append(num);
        }
        return sb.toString();
    }

    private String formatarNomeParaLogin(String nomeCompleto) {
        // Remove acentos (opcional, mas recomendado para logins)
        String nomeSemAcento = java.text.Normalizer.normalize(nomeCompleto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String[] partes = nomeSemAcento.toLowerCase().split(" ");

        if (partes.length >= 2) {
            // Pega o primeiro e o último nome
            return partes[0] + "." + partes[partes.length - 1];
        }
        return partes[0];
    }

    private List<ProfissionalSaude> gerarProfissionaisAleatorios(int qtd, Clinica clinica, List<Especialidade> especialidades) {
        List<ProfissionalSaude> salvas = new ArrayList<>();
        for (int i = 1; i <= qtd; i++) {
            String nome = sortearNomeRandomico();
            String cpf = gerarCpfValido();

            PessoaFisica pf = criarPessoa(nome, cpf, LocalDate.of(1960 + random.nextInt(35), random.nextInt(12) + 1, random.nextInt(28) + 1));

            ProfissionalSaude profissional = new ProfissionalSaude();
            profissional.setPessoaFisica(pf);
            profissional.setMatricula("MAT-" + (1000 + i));
            profissional.setUnidadeLotacao(clinica);

            // Define o conselho aleatoriamente
            TipoConselho conselho = random.nextBoolean() ? TipoConselho.CRM : TipoConselho.COFEN;
            profissional.setTipoConselho(conselho);
            profissional.setNumeroConselho((10000 + i) + "-BA");
            profissional.setUfConselho("BA");
            profissional.setAtivo(true);
            profissional.setDataAdmissao(LocalDate.now().minusDays(random.nextInt(1000)));

            profissional.getEspecialidades().add(especialidades.get(random.nextInt(especialidades.size())));
            salvas.add(profissionalSaudeRepository.save(profissional));

            String prefixo = (conselho == TipoConselho.CRM) ? "medico" : "enfermagem";

            String loginFormatado = prefixo + "." + formatarNomeParaLogin(nome) + "." + i + "@sghss.com";

            Usuario userPro = new Usuario();
            userPro.setPessoaFisica(pf);
            userPro.setLogin(loginFormatado);
            // Mantendo a mesma senha padronizada usando o encoder já injetado
            userPro.setSenha(passwordEncoder.encode("123456"));

            // Atribui o perfil baseado no conselho (assumindo que ROLE_ENFERMEIRO exista no seu enum)
            PerfilAcesso perfil = (conselho == TipoConselho.CRM) ? PerfilAcesso.ROLE_MEDICO : PerfilAcesso.ROLE_ENFERMEIRO;
            userPro.setPerfisAcesso(Set.of(perfil));
            userPro.setAtivo(true);

            usuarioRepository.save(userPro);
        }
        return salvas;
    }

    private List<Paciente> gerarPacientesAleatorios(int qtd) {
        List<Paciente> salvas = new ArrayList<>();
        String anoAtual = String.valueOf(LocalDate.now().getYear());

        for (int i = 1; i <= qtd; i++) {
            String nome = sortearNomeRandomico();
            String cpf = gerarCpfValido();
            String cartaoSus = String.format("8980000%08d", i);

            PessoaFisica pf = criarPessoa(nome, cpf, LocalDate.of(1940 + random.nextInt(70), random.nextInt(12) + 1, random.nextInt(28) + 1));

            Paciente paciente = new Paciente();
            paciente.setPessoaFisica(pf);
            paciente.setCartaoSus(cartaoSus);
            Paciente salvo = pacienteRepository.save(paciente);

            Prontuario prontuario = new Prontuario();
            prontuario.setNumeroProntuario("PEP-" + anoAtual + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            prontuario.setPaciente(salvo);
            prontuarioRepository.save(prontuario);

            salvas.add(salvo);

            String loginFormatado = "paciente." + formatarNomeParaLogin(nome) + "." + i + "@sghss.com";

            Usuario userPac = new Usuario();
            userPac.setPessoaFisica(pf);
            userPac.setLogin(loginFormatado);

            userPac.setSenha(passwordEncoder.encode("123456"));
            userPac.setPerfisAcesso(Set.of(PerfilAcesso.ROLE_PACIENTE));
            userPac.setAtivo(true);

            usuarioRepository.save(userPac);
        }
        return salvas;
    }
}