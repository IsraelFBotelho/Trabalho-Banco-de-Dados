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

        List<Estado> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.estado (sigla, nome, id_regiao) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Estado e : models) {

                Estado ex = exist.stream().filter((x) -> x.getSigla() == e.getSigla()).findFirst().orElse(null);

                if (ex == null) {

                    statement.setString(1, e.getSigla().toUpperCase());
                    statement.setString(2, e.getNome().toUpperCase());
                    statement.setInt(3, e.getIdRegiao());

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
    public List<Estado> read() {
        try {
            String SQL = "SELECT * FROM ambiente.estado;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Estado> states = new ArrayList<>();

            while (results.next()) {
                Estado e = new Estado();
                e.setId(results.getInt(1));
                e.setSigla(results.getString(2));
                e.setNome(results.getString(3));
                e.setIdRegiao(results.getInt(4));

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

    public void update(Estado model) {
        try {
            String SQL = "UPDATE ambiente.estado SET nome = ?, id_regiao = ? WHERE sigla = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, model.getNome());
            statement.setInt(2, model.getIdRegiao());
            statement.setString(3, model.getSigla());
            statement.execute();

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

    public int getStateIdByAcronym(String acronym) {
        try {
            String SQL = "SELECT id FROM ambiente.estado WHERE sigla = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, acronym);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt(1);
            }

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        try {
            String SQL = "SELECT id FROM ambiente.estado;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();

            return !results.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
