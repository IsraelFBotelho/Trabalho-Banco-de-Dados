-- Regiões
INSERT INTO
    ambiente.regiao (nome)
VALUES
    ('Norte'),
    ('Nordeste'),
    ('Centro-Oeste'),
    ('Sudeste'),
    ('Sul');

-- Estados
INSERT INTO
    ambiente.estado (sigla, nome, id_regiao)
VALUES
    -- Norte (1)
    ('AM', 'Amazonas', 1),
    ('RR', 'Roraima', 1),
    ('AP', 'Amapá', 1),
    ('PA', 'Pará', 1),
    ('TO', 'Tocantins', 1),
    ('RO', 'Rondônia', 1),
    ('AC', 'Acre', 1),
    -- Nordeste (2)
    ('MA', 'Maranhão', 2),
    ('PI', 'Piauí', 2),
    ('CE', 'Ceará', 2),
    ('RN', 'Rio Grande do Norte', 2),
    ('PE', 'Pernambuco', 2),
    ('PB', 'Paraíba', 2),
    ('SE', 'Sergipe', 2),
    ('AL', 'Alagoas', 2),
    ('BA', 'Bahia', 2),
    -- Centro-Oeste (3)
    ('MT', 'Mato Grosso', 3),
    ('MS', 'Mato Grosso do Sul', 3),
    ('GO', 'Goiás', 3),
    -- Sudeste (4)
    ('SP', 'São Paulo', 4),
    ('RJ', 'Rio de Janeiro', 4),
    ('ES', 'Espírito Santo', 4),
    ('MG', 'Belo Horizonte', 4),
    -- Sul (5)
    ('PR', 'Paraná', 5),
    ('RS', 'Rio Grande do Sul', 5),
    ('SC', 'Santa Catarina', 5);

-- Municípios
INSERT INTO
    ambiente.municipio (nome, id_estado)
VALUES
    ('Belém', 4),
    ('Palmas', 5),
    ('Castanhal', 4);

-- Estações metereológicas
INSERT INTO
    ambiente.estacao_metereologica (codigo, nome, id_municipio)
VALUES
    ('A201', 'Belém', 1),
    ('A009', 'Palmas', 2),
    ('A202', 'Castanhal', 3);

-- Medição clima
INSERT INTO
    ambiente.medicao_clima (
        data,
        hora,
        codigo_estacao_metereologica,
        temperatura_minima,
        temperatura_maxima
    )
VALUES
    -- Belém 2005
    ('2005-01-01', '12:00:00', 'A201', 28, 26),
    ('2005-01-02', '12:00:00', 'A201', 28, 25.5),
    ('2005-01-03', '12:00:00', 'A201', 27.2, 24.9),
    ('2005-01-04', '12:00:00', 'A201', 28.2, 25.9),
    ('2005-01-05', '12:00:00', 'A201', 28.1, 26.2),
    -- Belém 2010
    ('2010-01-01', '12:00:00', 'A201', 25.8, 24.8),
    ('2010-01-02', '12:00:00', 'A201', 25.2, 24.1),
    ('2010-01-03', '12:00:00', 'A201', 26.8, 25.2),
    ('2010-01-04', '12:00:00', 'A201', 27.7, 26.3),
    ('2010-01-05', '12:00:00', 'A201', 28.4, 26.4),
    -- Belém 2015
    ('2015-01-01', '12:00:00', 'A201', 27.9, 25.9),
    ('2015-01-02', '12:00:00', 'A201', 28, 26.2),
    ('2015-01-03', '12:00:00', 'A201', 28.7, 27.2),
    ('2015-01-04', '12:00:00', 'A201', 28, 26.3),
    ('2015-01-05', '12:00:00', 'A201', 24.5, 23.7),
    -- Belém 2020
    ('2020-01-01', '12:00:00', 'A201', 27.5, 25.4),
    ('2020-01-02', '12:00:00', 'A201', 26.2, 24.5),
    ('2020-01-03', '12:00:00', 'A201', 26.5, 25.3),
    -- ('2020-01-04', '12:00:00', 'A201', 0, 0),
    ('2020-01-05', '12:00:00', 'A201', 27, 25.7),
    -- Palmas 2005
    ('2005-01-01', '12:00:00', 'A009', 26.9, 25.3),
    ('2005-01-02', '12:00:00', 'A009', 25.1, 24.2),
    ('2005-01-03', '12:00:00', 'A009', 24, 23.4),
    ('2005-01-04', '12:00:00', 'A009', 29, 27.3),
    ('2005-01-05', '12:00:00', 'A009', 27.7, 25.8),
    -- Palmas 2010
    ('2010-01-01', '12:00:00', 'A009', 24.6, 23.7),
    ('2010-01-02', '12:00:00', 'A009', 22.9, 21.8),
    ('2010-01-03', '12:00:00', 'A009', 25.2, 22.6),
    ('2010-01-04', '12:00:00', 'A009', 28.2, 25.8),
    ('2010-01-05', '12:00:00', 'A009', 29.1, 26.1),
    -- Palmas 2015
    ('2015-01-01', '12:00:00', 'A009', 28, 25.9),
    ('2015-01-02', '12:00:00', 'A009', 29.8, 28.6),
    ('2015-01-03', '12:00:00', 'A009', 29.4, 27.3),
    ('2015-01-04', '12:00:00', 'A009', 23.1, 22),
    ('2015-01-05', '12:00:00', 'A009', 27.6, 25.9),
    -- Palmas 2020
    ('2020-01-01', '12:00:00', 'A009', 26.7, 25.3),
    ('2020-01-02', '12:00:00', 'A009', 26, 25.8),
    ('2020-01-03', '12:00:00', 'A009', 26.3, 25.6),
    ('2020-01-04', '12:00:00', 'A009', 23.5, 23.2),
    ('2020-01-05', '12:00:00', 'A009', 24, 23.6);

-- Área Geográfica
INSERT INTO
    ambiente.area_geografica (area_total, id_municipio)
VALUES
    (1070, 1),
    (2227, 2);

-- Floresta
INSERT INTO
    ambiente.floresta (
        area_floresta,
        area_nao_floresta,
        ano,
        id_area_geografica
    )
VALUES
    -- Belém
    (0, 7.1, 2005, 1),
    (164, 69.1, 2010, 1),
    (217.2, 7.1, 2015, 1),
    (229.4, 7.1, 2020, 1),
    -- Palmas
    (0, 2205, 2005, 2),
    (0, 2205, 2010, 2),
    (0, 2205, 2015, 2),
    (0, 2205, 2020, 2);

INSERT INTO
    ambiente.desmatamento (taxa_incremento, area_desmatada, id_floresta)
VALUES
    -- Belém
    (1.1, 243.9, 1),
    (0.9, 248.4, 2),
    (0.3, 249.3, 3),
    (0.2, 252.2, 4),
    -- Palmas
    (0, 0, 5),
    (0, 0, 6),
    (0, 0, 7),
    (0, 0, 8);
