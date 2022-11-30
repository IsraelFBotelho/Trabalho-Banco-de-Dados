package dao;

import dto.TemperatureDataDTO;
import interfaces.DAO;
import models.MedicaoClima;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicaoClimaDAO implements DAO<MedicaoClima> {
    private Connection connection = null;

    public MedicaoClimaDAO(Connection connection) {
        this.connection = connection;
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
                if (e.getTemperaturaMinima() != -9999 && e.getTemperaturaMaxima() != -9999) {
                    MedicaoClima ex = exist.stream().filter((x) -> x.getHora().equals(e.getHora()) && x.getData().equals(e.getData()) && x.getCodigoEstacaoMetereologica().equals(e.getCodigoEstacaoMetereologica())).findFirst().orElse(null);

                    if (ex == null) {
                        statement = connection.prepareStatement(SQL);
                        statement.setDate(1, e.getData());
                        statement.setTime(2, e.getHora());
                        statement.setString(3, e.getCodigoEstacaoMetereologica());
                        statement.setFloat(4, e.getTemperaturaMinima());
                        statement.setFloat(5, e.getTemperaturaMaxima());
                        statement.execute();
                    } else {
                        statement = connection.prepareStatement(SQLupdate);
                        statement.setFloat(1, e.getTemperaturaMinima());
                        statement.setFloat(2, e.getTemperaturaMaxima());
                        statement.setDate(3, e.getData());
                        statement.setTime(4, e.getHora());
                        statement.setString(5, e.getCodigoEstacaoMetereologica());
                        statement.execute();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(MedicaoClima model) {

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
                e.setData(results.getDate(1));
                e.setHora(results.getTime(2));
                e.setCodigoEstacaoMetereologica(results.getString(3));
                e.setTemperaturaMinima(results.getFloat(4));
                e.setTemperaturaMaxima(results.getFloat(5));

                weathers.add(e);
            }

            return weathers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TemperatureDataDTO> readTemperatureData(String cityName) {
        try {
            String SQL = "SELECT EXTRACT(YEAR FROM mc.data), EXTRACT(MONTH FROM mc.data), em.id_municipio, m.nome, AVG(mc.temperatura_maxima), " +
                    "MAX(mc.temperatura_maxima), AVG(mc.temperatura_minima), MIN(mc.temperatura_minima) " +
                    "FROM ambiente.medicao_clima mc INNER JOIN ambiente.estacao_metereologica em " +
                    "ON mc.codigo_estacao_metereologica = em.codigo INNER JOIN ambiente.municipio m " +
                    "ON em.id_municipio = m.id " +
                    "WHERE em.id_municipio = (" +
                    "SELECT id_municipio FROM ambiente.estacao_metereologica WHERE LOWER(nome) = ?) " +
                    "GROUP BY em.id_municipio, m.nome, EXTRACT(YEAR FROM mc.data), EXTRACT(MONTH FROM mc.data);";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setString(1, cityName.toLowerCase());

            ResultSet results = statement.executeQuery();
            List<TemperatureDataDTO> temperatures = new ArrayList<>();

            while (results.next()) {
                TemperatureDataDTO temperature = new TemperatureDataDTO();

                temperature.setYear(results.getInt(1));
                temperature.setMonth(results.getInt(2));
                temperature.setCityId(results.getInt(3));
                temperature.setCityName(results.getString(4));
                temperature.setMaxAvg(results.getFloat(5));
                temperature.setMax(results.getFloat(6));
                temperature.setMinAvg(results.getFloat(7));
                temperature.setMin(results.getFloat(8));
                temperatures.add(temperature);
            }

            return temperatures;
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
