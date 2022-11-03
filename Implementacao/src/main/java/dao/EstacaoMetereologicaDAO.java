package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.EstacaoMetereologica;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstacaoMetereologicaDAO implements DAO<EstacaoMetereologica> {
    private Connection connection = null;

    public EstacaoMetereologicaDAO() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<EstacaoMetereologica> models) {
        try {
            String SQL = "INSERT INTO ambiente.estacao_metereologica (codigo, nome, id_municipio) VALUES (?, ?, ?) " +
                    "ON CONFLICT (codigo) DO UPDATE SET nome = ?, id_municipio = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (EstacaoMetereologica e : models) {
                statement.setString(1, e.getCodigo());
                statement.setString(2, e.getNome());
                statement.setInt(3, e.getIdMunicipio());
                statement.setString(4, e.getNome());
                statement.setInt(5, e.getIdMunicipio());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EstacaoMetereologica> read() {
        try {
            String SQL = "SELECT * FROM ambiente.estacao_metereologica;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<EstacaoMetereologica> weatherStations = new ArrayList<>();

            while (results.next()) {
                EstacaoMetereologica e = new EstacaoMetereologica();
                e.setCodigo(results.getString(0));
                e.setNome(results.getString(1));
                e.setIdMunicipio(results.getInt(2));

                weatherStations.add(e);
            }

            return weatherStations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<EstacaoMetereologica> models) {
        try {
            String SQL = "UPDATE ambiente.estacao_metereologica SET nome = ?, id_municipio = ? WHERE codigo = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (EstacaoMetereologica e : models) {
                statement.setString(1, e.getNome());
                statement.setInt(2, e.getIdMunicipio());
                statement.setString(3, e.getCodigo());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            String SQL = "DELETE FROM ambiente.estacao_metereologica WHERE codigo = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
