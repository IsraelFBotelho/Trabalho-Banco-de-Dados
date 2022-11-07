package dao;

import interfaces.DAO;
import jdbc.PgConnectionFactory;
import models.MedicaoClima;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicaoClimaDAO implements DAO<MedicaoClima> {
    private Connection connection = null;

    public MedicaoClimaDAO() {
        PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();

        try {
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(List<MedicaoClima> models) {
        try {

            List<MedicaoClima> exist = this.read();

            String SQL = "INSERT INTO ambiente.medicao_clima (data, hora, codigo_estacao_metereologica, temperatura_minima, temperatura_maxima)" +
                    "VALUES (?, ?, ?, ?, ?)";


            String SQLupdate = "UPDATE ambiente.medicao_clima SET temperatura_minima = ?, temperatura_maxima = ? WHERE data = ? AND hora = ? AND codigo_estacao_metereologica = ? ";

            PreparedStatement statement;

            for (MedicaoClima e : models) {

                MedicaoClima ex = exist.stream().filter((x) -> x.getHora() == e.getHora() && x.getDt() == e.getDt() && x.getCodigoEstacaoMetereologica() == e.getCodigoEstacaoMetereologica()).findFirst().orElse(null);

                if(ex == null){
                    statement = connection.prepareStatement(SQL);
                    statement.setDate(1, e.getDt());
                    statement.setTime(2, e.getHora());
                    statement.setString(3, e.getCodigoEstacaoMetereologica());
                    statement.setFloat(4, e.getTemperaturaMinima());
                    statement.setFloat(5, e.getTemperaturaMaxima());
                }else{
                    statement = connection.prepareStatement(SQLupdate);
                    statement.setFloat(1, e.getTemperaturaMinima());
                    statement.setFloat(2, e.getTemperaturaMaxima());
                    statement.setDate(3, e.getDt());
                    statement.setTime(4, e.getHora());
                    statement.setString(5, e.getCodigoEstacaoMetereologica());
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MedicaoClima> read() {
        try {
            String SQL = "SELECT * FROM ambiente.medicao_clima;";
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet results = statement.executeQuery();
            List<MedicaoClima> weathers = new ArrayList<>();

            while (results.next()) {
                MedicaoClima e = new MedicaoClima();
                e.setDt(results.getDate(0));
                e.setHora(results.getTime(1));
                e.setCodigoEstacaoMetereologica(results.getString(2));
                e.setTemperaturaMinima(results.getFloat(3));
                e.setTemperaturaMaxima(results.getFloat(4));

                weathers.add(e);
            }

            return weathers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(List<MedicaoClima> models) {
        // do nothing
    }

    @Override
    public void delete(String id) {
        // do nothing
    }
}
