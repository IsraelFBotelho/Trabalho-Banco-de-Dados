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
        try {
            String SQL = "INSERT INTO ambiente.municipio (nome, id_estado) VALUES (?, ?) " +
                    "ON CONFLICT (nome) DO UPDATE SET id_estado = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Municipio e : models) {
                statement.setString(1, e.getNome());
                statement.setInt(2, e.getIdEstado());
                statement.setInt(3, e.getIdEstado());
                statement.execute();
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
                e.setId(results.getInt(0));
                e.setNome(results.getString(1));
                e.setIdEstado(results.getInt(2));

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
}
