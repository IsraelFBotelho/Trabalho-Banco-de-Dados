package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.Estado;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO implements DAO<Estado> {
    private Connection connection = null;

    public EstadoDAO() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<Estado> models) {
        try {
            String SQL = "INSERT INTO ambiente.estado (sigla, nome, id_regiao) VALUES (?, ?, ?) ON CONFLICT (sigla, nome) DO NOTHING;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Estado e : models) {
                statement.setString(1, e.getSigla());
                statement.setString(2, e.getNome());
                statement.setInt(3, e.getIdRegiao());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Estado> read() {
        try {
            String SQL = "SELECT * FROM ambiente.estado;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Estado> states = new ArrayList<>();

            while (results.next()) {
                Estado e = new Estado();
                e.setId(results.getInt(0));
                e.setSigla(results.getString(1));
                e.setNome(results.getString(2));
                e.setIdRegiao(results.getInt(3));

                states.add(e);
            }

            return states;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<Estado> models) {
        try {
            String SQL = "UPDATE ambiente.estado SET sigla = ?, nome = ?, id_regiao = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Estado e : models) {
                statement.setString(1, e.getSigla());
                statement.setString(2, e.getNome());
                statement.setInt(3, e.getIdRegiao());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            String SQL = "DELETE FROM ambiente.estado WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
