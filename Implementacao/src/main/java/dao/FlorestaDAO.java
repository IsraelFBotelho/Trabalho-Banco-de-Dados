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

    public FlorestaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(List<Floresta> models) {
        List<Floresta> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.floresta (area_floresta, area_nao_floresta, ano, id_area_geografica) " +
                    "VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);
            int updateId;

            for (Floresta e : models) {

                Floresta ex = exist.stream().filter((x) -> x.getIdAreaGeografica() == e.getIdAreaGeografica() && x.getAno() == e.getAno()).findFirst().orElse(null);

                if (ex == null) {

                    statement.setFloat(1, e.getAreaFloresta());
                    statement.setFloat(2, e.getAreaNaoFloresta());
                    statement.setInt(3, e.getAno());
                    statement.setInt(4, e.getIdAreaGeografica());
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
    public void create(Floresta model) {
        List<Floresta> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.floresta (area_floresta, area_nao_floresta, ano, id_area_geografica) " +
                    "VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);
            Floresta ex = exist.stream().filter(
                    (x) -> x.getIdAreaGeografica() == model.getIdAreaGeografica()
                            && x.getAno() == model.getAno()).findFirst().orElse(null);

            if (ex == null) {
                statement.setFloat(1, model.getAreaFloresta());
                statement.setFloat(2, model.getAreaNaoFloresta());
                statement.setInt(3, model.getAno());
                statement.setInt(4, model.getIdAreaGeografica());
                statement.execute();
            } else {
                update(model);
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
                e.setId(results.getInt(1));
                e.setAreaFloresta(results.getFloat(2));
                e.setAreaNaoFloresta(results.getFloat(3));
                e.setAno(results.getInt(4));
                e.setIdAreaGeografica(results.getInt(5));

                forests.add(e);
            }

            return forests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<Floresta> models) {
    }

    public void update(Floresta model) {
        try {
            String SQL = "UPDATE ambiente.floresta " +
                    "SET area_floresta = ?, area_nao_floresta = ? WHERE ano = ? AND id_area_geografica = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setFloat(1, model.getAreaFloresta());
            statement.setFloat(2, model.getAreaNaoFloresta());
            statement.setInt(3, model.getAno());
            statement.setInt(4, model.getIdAreaGeografica());
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

    public int getForestIdByYearAndGeographicalAreaId(int year, int geographicalAreaId) {
        try {
            String SQL = "SELECT id FROM ambiente.floresta WHERE ano = ? AND id_area_geografica = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, year);
            statement.setInt(2, geographicalAreaId);

            ResultSet results = statement.executeQuery();

            if (results.next())
                return results.getInt(1);

            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
