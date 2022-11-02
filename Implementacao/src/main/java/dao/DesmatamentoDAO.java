package dao;

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

    public DesmatamentoDAO() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<Desmatamento> models) {
        try {
            String SQL = "INSERT INTO ambiente.desmatamento (taxa_incremento, area_desmatada, id_floresta) VALUES (?, ?, ?);";
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

    @Override
    public List<Desmatamento> read() {
        try {
            String SQL = "SELECT * FROM ambiente.desmatamento;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Desmatamento> deforestations = new ArrayList<>();

            while (results.next()) {
                Desmatamento e = new Desmatamento();
                e.setTaxaIncremento(results.getFloat(0));
                e.setAreaDesmatada(results.getFloat(1));
                e.setIdFloresta(results.getInt(2));

                deforestations.add(e);
            }

            return deforestations;
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
