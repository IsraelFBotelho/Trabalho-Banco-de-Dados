CREATE SCHEMA ambiente;

CREATE TABLE ambiente.regiao (
    id INT AUTO_INCREMENT,
    nome VARCHAR(25),
    CONSTRAINT pk_regiao PRIMARY KEY(id),
    CONSTRAINT uk_regiao_nome UNIQUE(nome)
);

CREATE TABLE ambiente.estado (
    id INT AUTO_INCREMENT,
    sigla VARCHAR(2),
    nome VARCHAR(50),
    id_regiao INT,
    CONSTRAINT pk_estado PRIMARY KEY(id),
    CONSTRAINT uk_estado_sigla_nome UNIQUE (sigla,nome),
    CONSTRAINT fk_estado_regiao FOREIGN KEY(id_regiao) REFERENCES ambiente.regiao(id)
);

CREATE TABLE ambiente.municipio (
    id INT AUTO_INCREMENT,
    nome VARCHAR(100),
    id_estado INT,
    CONSTRAINT pk_municipio PRIMARY KEY(id),
    CONSTRAINT uk_municipio_nome UNIQUE(nome),
    CONSTRAINT fk_municipio_estado FOREIGN KEY(id_estado) REFERENCES ambiente.estado(id)
);

CREATE TABLE ambiente.estacao_metereologica (
    codigo VARCHAR(50),
    nome VARCHAR(100),
    id_municipio INT,
    CONSTRAINT pk_estacoes_metereologicas PRIMARY KEY(codigo),
    CONSTRAINT fk_estacoes_metereologicas_municipio FOREIGN KEY(id_municipio) REFERENCES ambiente.municipio(id)
);

CREATE TABLE ambiente.medicao_clima (
    dt DATE,
    hora TIME,
    codigo_estacao_metereologica VARCHAR(100),
    temperatura_minina FLOAT,
    temperatura_maxima FLOAT,
    CONSTRAINT fk_medicoes_clima_estacao_metereologica FOREIGN KEY(codigo_estacao_metereologica) REFERENCES ambiente.estacao_metereologica(codigo),
    CONSTRAINT pk_medicoes_clima PRIMARY KEY(codigo_estacao_metereologica, dt, hora)
);

CREATE TABLE ambiente.area_geografica (
    id INT AUTO_INCREMENT,
    area_total FLOAT,
    id_municipio INT,
    CONSTRAINT pk_area_geografica PRIMARY KEY(id),
    CONSTRAINT fk_area_geografica_municipio FOREIGN KEY(id_municipio) REFERENCES ambiente.municipio(id)
);

CREATE TABLE ambiente.floresta (
    id INT AUTO_INCREMENT,
    area_floresta FLOAT,
    area_nao_floresta FLOAT,
    ano INT,
    id_area_geografica INT,
    CONSTRAINT pk_floresta PRIMARY KEY(id),
    CONSTRAINT fk_floresta_area_geografica FOREIGN KEY(id_area_geografica) REFERENCES ambiente.area_geografica(id)
);

CREATE TABLE ambiente.desmatamento (
    taxa_incremento FLOAT,
    area_desmatada FLOAT,
    id_floresta INT,
    CONSTRAINT fk_desmatamento_floresta FOREIGN KEY(id_floresta) REFERENCES ambiente.floresta(id),
    CONSTRAINT pk_desmatamento PRIMARY KEY(id_floresta)
);