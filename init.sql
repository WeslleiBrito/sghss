CREATE TABLE pacientes (
    id UUID PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    historico_clinico TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE medicos (
    id UUID PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    crm VARCHAR(20) UNIQUE NOT NULL,
    especialidade VARCHAR(100) NOT NULL
);

CREATE TABLE consultas (
    id UUID PRIMARY KEY,
    paciente_id UUID NOT NULL,
    medico_id UUID NOT NULL,
    data_hora_agendamento TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    observacoes_clinicas TEXT,
    CONSTRAINT fk_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes (id),
    CONSTRAINT fk_medico FOREIGN KEY (medico_id) REFERENCES medicos (id)
);

CREATE TABLE prescricoes (
    id UUID PRIMARY KEY,
    consulta_id UUID NOT NULL,
    medicamento VARCHAR(150) NOT NULL,
    dosagem_instrucoes TEXT NOT NULL,
    CONSTRAINT fk_consulta_prescricao FOREIGN KEY (consulta_id) REFERENCES consultas (id) ON DELETE CASCADE
);

CREATE TABLE exames (
    id UUID PRIMARY KEY,
    consulta_id UUID NOT NULL,
    tipo_exame VARCHAR(150) NOT NULL,
    data_solicitacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_consulta_exame FOREIGN KEY (consulta_id) REFERENCES consultas (id) ON DELETE CASCADE
);