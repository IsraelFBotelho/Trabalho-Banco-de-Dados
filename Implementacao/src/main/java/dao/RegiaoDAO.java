package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.Regiao;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegiaoDAO implements DAO<Regiao> {
    private Connection connection = null;

    public RegiaoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(List<Regiao> models) {
        try {
            String SQL = "INSERT INTO ambiente.regiao (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Regiao e : models) {
                if (e.getNome() != null) {
                    statement.setString(1, e.getNome());
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Regiao model) {

    }

    @Override
    public List<Regiao> read() {
        try {
            String SQL = "SELECT * FROM ambiente.regiao;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Regiao> regions = new ArrayList<>();

            while (results.next()) {
                Regiao e = new Regiao();
                e.setId(results.getInt(0));
                e.setNome(results.getString(1));

                regions.add(e);
            }

            return regions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<Regiao> models) {
        try {
            String SQL = "UPDATE ambiente.regiao SET nome = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Regiao e : models) {
                statement.setString(1, e.getNome());
                statement.setInt(2, e.getId());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            String SQL = "DELETE FROM ambiente.regiao WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRegionId(String name) {
        try {
            String SQL = "SELECT id FROM ambiente.regiao WHERE nome = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, name);

            ResultSet results = statement.executeQuery();

            if (results.next())
                return results.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public boolean isEmpty() {
        try {
            String SQL = "SELECT id FROM ambiente.regiao;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();

            return !results.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
