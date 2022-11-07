package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.Estado;
import models.Historico;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO implements DAO<Historico> {

    private Connection connection = null;

    public HistoricoDAO(){
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<Historico> models) {

        try {
            String SQL = "INSERT INTO ambiente.historico (nome, tipo, data) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Historico e : models) {
                statement.setString(1, e.getNome());
                statement.setString(2, e.getTipo());
                statement.setDate(3, e.getData());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Historico> read() {
        try {
            String SQL = "SELECT * FROM ambiente.historico;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<Historico> states = new ArrayList<>();

            while (results.next()) {
                Historico e = new Historico();
                e.setNome(results.getString(2));
                e.setTipo(results.getString(3));
                e.setData(results.getDate(4));

                states.add(e);
            }

            return states;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public void update(List<Historico> models) {
        // do nothing
    }

    @Override
    public void delete(String id) {
        // do nothing
    }
}
