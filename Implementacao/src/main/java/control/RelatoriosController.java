package control;

import dao.MedicaoClimaDAO;
import dao.MunicipioDAO;
import dto.TemperatureDataDTO;
import enums.Months;
import jdbc.PgConnectionFactory;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class RelatoriosController implements Serializable {
    private Connection connection = null;
    private String type;
    private String regiao;
    private String city = null;
    private List<String> cities;

    private String cityDesmatado;

    public String getCityDesmatado() {
        return cityDesmatado;
    }

    public void setCityDesmatado(String cityDesmatado) {
        this.cityDesmatado = cityDesmatado;
    }

    public List<String> getCitiesDemastado() {
        return citiesDemastado;
    }

    public void setCitiesDemastado(List<String> citiesDemastado) {
        this.citiesDemastado = citiesDemastado;
    }

    private List<String> citiesDemastado;
    private LineChartModel lineModel = new LineChartModel();

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city.equals("")) this.city = null;
        else this.city = city;
    }

    private void setConnection() {
        try {
            PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.lineModel = new LineChartModel();
        this.city = null;
        this.cityDesmatado = null;

        if (this.type.equals("T")) this.getAllCities();
    }

    public List<String> getCities() {
        return this.cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    @PostConstruct
    public void init() {
        this.setConnection();
    }

    private void getAllCities() {
        if (this.cities == null || this.cities.isEmpty()) {
            MunicipioDAO citiesDao = new MunicipioDAO(this.connection);
            this.cities = citiesDao.readCities();
        }
    }

    public void renderCity() {
        if (this.city != null) {
            System.out.println("Generating graphics for city " + this.city);

            this.createLineModel();
        } else this.lineModel = new LineChartModel();
    }

    public void renderCityDesmatado() {
        if (this.city != null) {
            System.out.println("Generating graphics for city " + this.city);

            this.createLineModelDesmatado();
        } else this.lineModel = new LineChartModel();
    }

    private void createLineModel() {
        ChartData data = new ChartData();
        LineChartDataSet dataSet2 = new LineChartDataSet();
        MedicaoClimaDAO weatherDao = new MedicaoClimaDAO(this.connection);
        List<TemperatureDataDTO> temperatureData = weatherDao.readTemperatureData(this.city);
        List<Object> maxAvgValues = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Months[] month = Months.values();

        for (var e : temperatureData) {
            labels.add(generateLabel(e.getYear(), month[e.getMonth() - 1].getValue()));
            maxAvgValues.add(e.getMaxAvg());
        }

        dataSet2.setData(maxAvgValues);
        dataSet2.setLabel("Média máxima");
        dataSet2.setFill(false);
        dataSet2.setBorderColor("rgb(255, 0, 0)");
        dataSet2.setTension(0.1);


        data.addChartDataSet(dataSet2);
        data.setLabels(labels);
        lineModel.setData(data);

        LineChartOptions options = new LineChartOptions();

        Title title = new Title();
        title.setDisplay(true);
        title.setText(this.city);
        options.setTitle(title);

        lineModel.setOptions(options);
    }

    private void createLineModelDesmatado() {

        // Area Desmatada por ano

        ChartData data = new ChartData();
        LineChartDataSet dataSet = new LineChartDataSet();
        LineChartDataSet dataSet2 = new LineChartDataSet();
        MedicaoClimaDAO weatherDao = new MedicaoClimaDAO(this.connection);
        List<TemperatureDataDTO> temperatureData = weatherDao.readTemperatureData(this.city);
        List<Object> maxAvgValues = new ArrayList<>();
        List<Object> teste = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Months[] month = Months.values();

        for (var e : temperatureData) {
            labels.add(generateLabel(e.getYear(), month[e.getMonth() - 1].getValue()));
            maxAvgValues.add(e.getMaxAvg());
        }

        teste.add(12);
        teste.add(15);
        teste.add(12);
        teste.add(15);
        dataSet.setData(teste);

        dataSet2.setData(maxAvgValues);
        dataSet2.setLabel("Média máxima");
        dataSet2.setFill(false);
        dataSet2.setBorderColor("rgb(255, 0, 0)");
        dataSet2.setTension(0.1);


        data.addChartDataSet(dataSet);
        data.addChartDataSet(dataSet2);
        data.setLabels(labels);
        lineModel.setData(data);

        LineChartOptions options = new LineChartOptions();

        Title title = new Title();
        title.setDisplay(true);
        title.setText(this.city);
        options.setTitle(title);

        lineModel.setOptions(options);
    }

    private String generateLabel(int year, String monthName) {
        return monthName + " - " + year;
    }
}
