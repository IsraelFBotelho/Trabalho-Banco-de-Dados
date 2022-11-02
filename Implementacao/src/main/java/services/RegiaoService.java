package services;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.Regiao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RegiaoService implements DAO<Regiao> {
    private Connection connection = null;

    public RegiaoService() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<Regiao> models) {
        try {
            String SQL = "INSERT INTO ambiente.regiao (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (Regiao e : models) {
                statement.setString(1, e.getNome());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read() {

    }

    @Override
    public void update(List<Regiao> models) {

    }

    @Override
    public void delete(int id) {

    }
}
