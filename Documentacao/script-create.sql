DROP SCHEMA IF EXISTS ambiente CASCADE;
CREATE SCHEMA ambiente;

CREATE TABLE ambiente.regiao (
    id SERIAL,
    nome VARCHAR(25) NOT NULL,
    CONSTRAINT pk_regiao PRIMARY KEY(id),
    CONSTRAINT uk_regiao_nome UNIQUE(nome)
);

CREATE TABLE ambiente.estado (
    id SERIAL,
    sigla VARCHAR(2) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    id_regiao INT,
    CONSTRAINT pk_estado PRIMARY KEY(id),
    CONSTRAINT uk_estado_sigla UNIQUE (sigla),
    CONSTRAINT uk_estado_nome UNIQUE (nome),
    CONSTRAINT fk_estado_regiao FOREIGN KEY(id_regiao) REFERENCES ambiente.regiao(id)
);

CREATE TABLE ambiente.municipio (
    id SERIAL,
    nome VARCHAR(100) NOT NULL,
    id_estado INT,
    CONSTRAINT pk_municipio PRIMARY KEY(id),
    CONSTRAINT uk_municipio_nome UNIQUE(nome),
    CONSTRAINT fk_municipio_estado FOREIGN KEY(id_estado) REFERENCES ambiente.estado(id)
);

CREATE TABLE ambiente.estacao_metereologica (
    codigo VARCHAR(50),
    nome VARCHAR(100) NOT NULL,
    id_municipio INT,
    CONSTRAINT pk_estacoes_metereologicas PRIMARY KEY(codigo),
    CONSTRAINT fk_estacoes_metereologicas_municipio FOREIGN KEY(id_municipio) REFERENCES ambiente.municipio(id)
);

CREATE TABLE ambiente.medicao_clima (
    data DATE,
    hora TIME,
    codigo_estacao_metereologica VARCHAR(100),
    temperatura_minima FLOAT,
    temperatura_maxima FLOAT,
    CONSTRAINT fk_medicoes_clima_estacao_metereologica FOREIGN KEY(codigo_estacao_metereologica) REFERENCES ambiente.estacao_metereologica(codigo),
    CONSTRAINT pk_medicoes_clima PRIMARY KEY(codigo_estacao_metereologica, data, hora)
);

CREATE TABLE ambiente.area_geografica (
    id SERIAL,
    area_total FLOAT,
    id_municipio INT,
    CONSTRAINT pk_area_geografica PRIMARY KEY(id),
    CONSTRAINT fk_area_geografica_municipio FOREIGN KEY(id_municipio) REFERENCES ambiente.municipio(id)
);

CREATE TABLE ambiente.floresta (
    id SERIAL,
    area_floresta FLOAT,
    area_nao_floresta FLOAT,
    ano INT,
    id_area_geografica INT,
    CONSTRAINT pk_floresta PRIMARY KEY(id),
    CONSTRAINT uk_floresta_ano UNIQUE(ano),
    CONSTRAINT fk_floresta_area_geografica FOREIGN KEY(id_area_geografica) REFERENCES ambiente.area_geografica(id)
);

CREATE TABLE ambiente.desmatamento (
    taxa_incremento FLOAT,
    area_desmatada FLOAT,
    id_floresta INT,
    CONSTRAINT fk_desmatamento_floresta FOREIGN KEY(id_floresta) REFERENCES ambiente.floresta(id),
    CONSTRAINT pk_desmatamento PRIMARY KEY(id_floresta)
);

CREATE TABLE ambiente.historico (
    id SERIAL,
    nome VARCHAR(100),
    tipo VARCHAR(15),
    data DATE,
    CONSTRAINT pk_historico PRIMARY KEY (id)
);