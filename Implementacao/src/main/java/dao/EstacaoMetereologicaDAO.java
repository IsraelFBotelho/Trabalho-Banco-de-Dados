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

    public EstacaoMetereologicaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(List<EstacaoMetereologica> models) {
        List<EstacaoMetereologica> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.estacao_metereologica (codigo, nome, id_municipio) VALUES (?, ?, ?) ";

            PreparedStatement statement = connection.prepareStatement(SQL);

            for (EstacaoMetereologica e : models) {
                EstacaoMetereologica ex = exist.stream().filter((x) -> x.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);

                if (ex == null) {
                    statement.setString(1, e.getCodigo());
                    statement.setString(2, e.getNome());
                    statement.setInt(3, e.getIdMunicipio());
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
    public void create(EstacaoMetereologica model) {

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
                e.setCodigo(results.getString(1));
                e.setNome(results.getString(2));
                e.setIdMunicipio(results.getInt(3));

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

    public void update(EstacaoMetereologica model) {
        try {
            String SQL = "UPDATE ambiente.estacao_metereologica SET nome = ?, id_municipio = ? WHERE codigo = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, model.getNome());
            statement.setInt(2, model.getIdMunicipio());
            statement.setString(3, model.getCodigo());
            statement.execute();

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
