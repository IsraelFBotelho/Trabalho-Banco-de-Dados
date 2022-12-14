package dao;

import dto.DeforestationDataDTO;
import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.Desmatamento;
import models.Regiao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DesmatamentoDAO implements DAO<Desmatamento> {
    private Connection connection = null;

    public DesmatamentoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(List<Desmatamento> models) {
        List<Desmatamento> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.desmatamento (taxa_incremento, area_desmatada, id_floresta) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Desmatamento e : models) {
                Desmatamento ex = exist.stream().filter((x) -> x.getIdFloresta() == e.getIdFloresta()).findFirst().orElse(null);

                if (ex == null) {
                    statement.setFloat(1, e.getTaxaIncremento());
                    statement.setFloat(2, e.getAreaDesmatada());
                    statement.setInt(3, e.getIdFloresta());
                    statement.execute();
                } else {
                    update(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Desmatamento model) {
        List<Desmatamento> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.desmatamento (taxa_incremento, area_desmatada, id_floresta) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);
            Desmatamento ex = exist.stream().filter((x) -> x.getIdFloresta() == model.getIdFloresta()).findFirst().orElse(null);

            if (ex == null) {
                statement.setFloat(1, model.getTaxaIncremento());
                statement.setFloat(2, model.getAreaDesmatada());
                statement.setInt(3, model.getIdFloresta());
                statement.execute();
            } else {
                update(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Desmatamento> read() {
        try {
            String SQL = "SELECT * FROM ambiente.desmatamento;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Desmatamento> deforestation = new ArrayList<>();

            while (results.next()) {
                Desmatamento e = new Desmatamento();
                e.setTaxaIncremento(results.getFloat(1));
                e.setAreaDesmatada(results.getFloat(2));
                e.setIdFloresta(results.getInt(3));

                deforestation.add(e);
            }

            return deforestation;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DeforestationDataDTO> readDeforestationData(String cityName) {
        try {
            String SQL = "SELECT d.area_desmatada, f.ano, m.nome " +
                    "FROM ambiente.desmatamento d INNER JOIN ambiente.floresta f ON d.id_floresta = f.id " +
                    "INNER JOIN ambiente.area_geografica ag ON f.id_area_geografica = ag.id " +
                    "INNER JOIN ambiente.municipio m ON ag.id_municipio = m.id WHERE LOWER(m.nome) = ? " +
                    "ORDER BY f.ano;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, cityName.toLowerCase());

            ResultSet results = statement.executeQuery();
            List<DeforestationDataDTO> deforestation = new ArrayList<>();

            while (results.next()) {
                DeforestationDataDTO e = new DeforestationDataDTO();

                e.setDeforestation(results.getFloat(1));
                e.setYear(results.getInt(2));
                e.setCityName(results.getString(3));

                deforestation.add(e);
            }

            return deforestation;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<Desmatamento> models) {
        try {
            String SQL = "UPDATE ambiente.desmatamento SET taxa_incremento = ?, area_desmatada = ? WHERE id_floresta = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Desmatamento e : models) {
                statement.setFloat(1, e.getTaxaIncremento());
                statement.setFloat(2, e.getAreaDesmatada());
                statement.setInt(3, e.getIdFloresta());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Desmatamento model) {
        try {
            String SQL = "UPDATE ambiente.desmatamento SET taxa_incremento = ?, area_desmatada = ? WHERE id_floresta = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setFloat(1, model.getTaxaIncremento());
            statement.setFloat(2, model.getAreaDesmatada());
            statement.setInt(3, model.getIdFloresta());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            String SQL = "DELETE FROM ambiente.desmatamento WHERE id_floresta = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
