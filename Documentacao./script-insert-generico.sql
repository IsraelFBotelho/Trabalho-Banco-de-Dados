-- Regiões
INSERT INTO ambiente.regiao (nome) VALUES (_);

--Estados
INSERT INTO ambiente.estado (sigla, nome, id_regiao) VALUES (_, _, _);

-- Municípios
INSERT INTO ambiente.municipio (nome, id_estado) VALUES (_, _);

-- Estações metereológicas
INSERT INTO
    ambiente.estacao_metereologica (codigo, nome, id_municipio) VALUES (_, _, _);

-- Medição clima
INSERT INTO ambiente.medicao_clima (
        dt,
        hora,
        codigo_estacao_metereologica,
        temperatura_minina,
        temperatura_maxima
    ) VALUES (_, _, _, _, _);

-- Área Geográfica
INSERT INTO ambiente.area_geografica (area_total, id_municipio) VALUES (_, _);

-- Floresta
INSERT INTO
    ambiente.floresta (
        area_floresta,
        area_nao_floresta,
        ano,
        id_area_geografica
    )
VALUES (_, _, _, _);

-- Desmatamento
INSERT INTO ambiente.desmatamento (taxa_incremento, area_desmatada, id_floresta) VALUES (_, _, _);
