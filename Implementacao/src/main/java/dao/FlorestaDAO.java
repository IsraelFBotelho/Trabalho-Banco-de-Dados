package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.Floresta;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlorestaDAO implements DAO<Floresta> {
    private Connection connection = null;

    public FlorestaDAO() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<Floresta> models) {
        try {
            String SQL = "INSERT INTO ambiente.floresta (area_floresta, area_nao_floresta, ano, id_area_geografica) " +
                    "VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);
            int updateId;

            for (Floresta e : models) {
                if ((updateId = exists(e)) == -1) {
                    statement.setFloat(1, e.getAreaFloresta());
                    statement.setFloat(2, e.getAreaNaoFloresta());
                    statement.setInt(3, e.getAno());
                    statement.setInt(4, e.getIdAreaGeografica());
                    statement.execute();
                } else {
                    e.setId(updateId);
                    update(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Floresta> read() {
        try {
            String SQL = "SELECT * FROM ambiente.floresta;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Floresta> forests = new ArrayList<>();

            while (results.next()) {
                Floresta e = new Floresta();
                e.setId(results.getInt(0));
                e.setAreaFloresta(results.getFloat(1));
                e.setAreaNaoFloresta(results.getFloat(2));
                e.setAno(results.getInt(3));
                e.setIdAreaGeografica(results.getInt(4));

                forests.add(e);
            }

            return forests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<Floresta> models) {
        try {
            String SQL = "UPDATE ambiente.floresta " +
                    "SET area_floresta = ?, area_nao_floresta = ?, ano = ?, id_area_geografica = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Floresta e : models) {
                statement.setFloat(1, e.getAreaFloresta());
                statement.setFloat(2, e.getAreaNaoFloresta());
                statement.setInt(3, e.getAno());
                statement.setInt(4, e.getIdAreaGeografica());
                statement.setInt(5, e.getId());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(Floresta model) {
        try {
            String SQL = "UPDATE ambiente.floresta " +
                    "SET area_floresta = ?, area_nao_floresta = ?, ano = ?, id_area_geografica = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setFloat(1, model.getAreaFloresta());
            statement.setFloat(2, model.getAreaNaoFloresta());
            statement.setInt(3, model.getAno());
            statement.setInt(4, model.getIdAreaGeografica());
            statement.setInt(5, model.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            String SQL = "DELETE FROM ambiente.floresta WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int exists(Floresta model) {
        try {
            String SQL = "SELECT id FROM ambiente.floresta WHERE ano = ? AND id_area_geografica = ? LIMIT 1;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet result = statement.executeQuery();

            if (result.next())
                return result.getInt(0);

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
