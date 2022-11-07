package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.AreaGeografica;
import models.Regiao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaGeograficaDAO implements DAO<AreaGeografica> {
    private Connection connection = null;

    public AreaGeograficaDAO() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<AreaGeografica> models) {

        List<AreaGeografica> exist = this.read();

        try {
            String SQL = "INSERT INTO ambiente.area_geografica (area_total, id_municipio) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (AreaGeografica e : models) {

                AreaGeografica ex = exist.stream().filter((x) -> x.getIdMunicipio() == e.getIdMunicipio()).findFirst().orElse(null);

                if(ex == null) {

                    statement.setFloat(1, e.getAreaTotal());
                    statement.setInt(2, e.getIdMunicipio());
                    statement.execute();
                }else{
                    this.update(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AreaGeografica> read() {
        try {
            String SQL = "SELECT * FROM ambiente.area_geografica;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<AreaGeografica> areas = new ArrayList<>();

            while (results.next()) {
                AreaGeografica e = new AreaGeografica();
                e.setId(results.getInt(0));
                e.setAreaTotal(results.getFloat(1));
                e.setIdMunicipio(results.getInt(2));

                areas.add(e);
            }

            return areas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<AreaGeografica> models) {
        try {
            String SQL = "UPDATE ambiente.area_geografica SET area_total = ?, id_municipio = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            for (AreaGeografica e : models) {
                statement.setFloat(1, e.getAreaTotal());
                statement.setInt(2, e.getIdMunicipio());
                statement.setInt(3, e.getId());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(AreaGeografica model){
        try {
            String SQL = "UPDATE ambiente.area_geografica SET area_total = ? WHERE id_municipio = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setFloat(1, model.getAreaTotal());
            statement.setInt(2, model.getIdMunicipio());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            String SQL = "DELETE FROM ambiente.area_desmatamento WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
