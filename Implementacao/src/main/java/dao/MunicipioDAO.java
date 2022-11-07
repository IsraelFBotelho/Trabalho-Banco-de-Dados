package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.Municipio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MunicipioDAO implements DAO<Municipio> {
    private Connection connection = null;

    public MunicipioDAO() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<Municipio> models) {
        List<Municipio> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.municipio (nome, id_estado) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Municipio e : models) {
                Municipio ex = exist.stream().filter(x -> x.getNome().equals(e.getNome())).findFirst().orElse(null);

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

            statement.setString(1, name);

            ResultSet results = statement.executeQuery();

            if (results.next())
                return results.getInt(1);

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
