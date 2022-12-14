package dao;

import interfaces.DAO;
import models.Municipio;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MunicipioDAO implements DAO<Municipio> {
    private Connection connection = null;

    public MunicipioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(List<Municipio> models) {
        List<Municipio> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.municipio (nome, id_estado) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Municipio e : models) {
                Municipio ex = exist.stream().filter((x) -> x.getNome().equals(e.getNome().toUpperCase()) && x.getIdEstado() == e.getIdEstado()).findFirst().orElse(null);

                if (ex == null) {
                    statement.setString(1, e.getNome().toUpperCase());
                    statement.setInt(2, e.getIdEstado());
                    statement.execute();
                } else {
                    this.update(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Municipio model) {
        List<Municipio> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.municipio (nome, id_estado) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);
            Municipio ex = exist.stream().filter((x) -> x.getNome().equals(model.getNome().toUpperCase()) && x.getIdEstado() == model.getIdEstado()).findFirst().orElse(null);

            if (ex == null) {
                statement.setString(1, model.getNome().toUpperCase());
                statement.setInt(2, model.getIdEstado());
                statement.execute();
            } else {
                this.update(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Municipio> read() {
        try {
            String SQL = "SELECT * FROM ambiente.municipio;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Municipio> cities = new ArrayList<>();

            while (results.next()) {
                Municipio e = new Municipio();

                e.setId(results.getInt(1));
                e.setNome(results.getString(2));
                e.setIdEstado(results.getInt(3));

                cities.add(e);
            }

            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readTemperatureCities() {
        try {
            String SQL = "SELECT DISTINCT m.nome FROM ambiente.medicao_clima mc " +
                    "INNER JOIN ambiente.estacao_metereologica em ON mc.codigo_estacao_metereologica = em.codigo\n" +
                    "INNER JOIN ambiente.municipio m ON em.id_municipio = m.id " +
                    "ORDER BY m.nome;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<String> cities = new ArrayList<>();

            while (results.next()) {
                String city = results.getString(1).toLowerCase();
                cities.add(StringUtils.capitalize(city));
            }

            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readDeforestationCities() {
        try {
            String SQL = "SELECT DISTINCT m.nome " +
                    "FROM ambiente.desmatamento d " +
                    "INNER JOIN ambiente.floresta f ON d.id_floresta = f.id " +
                    "INNER JOIN ambiente.area_geografica ag ON f.id_area_geografica = ag.id " +
                    "INNER JOIN ambiente.municipio m ON ag.id_municipio = m.id " +
                    "ORDER BY m.nome;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<String> cities = new ArrayList<>();

            while (results.next()) {
                String city = results.getString(1).toLowerCase();
                cities.add(StringUtils.capitalize(city));
            }

            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readRelatedCities() {
        try {
            String SQL = "SELECT DISTINCT m.nome FROM ambiente.desmatamento d " +
                    "INNER JOIN ambiente.floresta f ON d.id_floresta = f.id " +
                    "INNER JOIN ambiente.area_geografica ag ON f.id_area_geografica = ag.id " +
                    "INNER JOIN ambiente.estacao_metereologica em ON ag.id_municipio = em.id_municipio " +
                    "INNER JOIN ambiente.municipio m ON em.id_municipio = m.id " +
                    "ORDER BY m.nome;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<String> cities = new ArrayList<>();

            while (results.next()) {
                String city = results.getString(1).toLowerCase();
                cities.add(StringUtils.capitalize(city));
            }

            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<Municipio> models) {
        try {
            String SQL = "UPDATE ambiente.municipio SET nome = ?, id_estado = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Municipio e : models) {
                statement.setString(1, e.getNome());
                statement.setInt(2, e.getIdEstado());
                statement.setInt(3, e.getId());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Municipio model) {
        try {
            String SQL = "UPDATE ambiente.municipio SET id_estado = ? WHERE nome = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, model.getIdEstado());
            statement.setString(2, model.getNome());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            String SQL = "DELETE FROM ambiente.municipio WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCityIdByName(String name) {
        try {
            String SQL = "SELECT id FROM ambiente.municipio WHERE nome = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, name.toUpperCase());

            ResultSet results = statement.executeQuery();

            if (results.next()) return results.getInt(1);

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
