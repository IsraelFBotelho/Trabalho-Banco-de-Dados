package control;

import dao.DesmatamentoDAO;
import dao.MedicaoClimaDAO;
import dao.MunicipioDAO;
import dto.DeforestationDataDTO;
import dto.TemperatureDataDTO;
import enums.Months;
import jdbc.PgConnectionFactory;
import org.primefaces.model.charts.ChartData;
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
    private String temperatureCity = null;
    private List<String> temperatureCities;
    private String deforestationCity = null;
    private List<String> deforestationCities;
    private LineChartModel lineModel = new LineChartModel();
    private LineChartModel lineModel2 = new LineChartModel();

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    public void setLineModel2(LineChartModel lineModel2) {
        this.lineModel2 = lineModel2;
    }

    public String getDeforestationCity() {
        return deforestationCity;
    }

    public void setDeforestationCity(String deforestationCity) {
        this.deforestationCity = deforestationCity;
    }

    public List<String> getDeforestationCities() {
        return deforestationCities;
    }

    public void setDeforestationCities(List<String> deforestationCities) {
        this.deforestationCities = deforestationCities;
    }

    public String getTemperatureCity() {
        return temperatureCity;
    }

    public void setTemperatureCity(String temperatureCity) {
        if (temperatureCity.equals("")) this.temperatureCity = null;
        else this.temperatureCity = temperatureCity;
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
        this.clearAll();
        this.getAllCities();
    }

    private void clearAll() {
        this.lineModel = new LineChartModel();
        this.lineModel2 = new LineChartModel();
        this.temperatureCity = null;
        this.deforestationCity = null;
        this.temperatureCities = null;
    }

    public List<String> getTemperatureCities() {
        return this.temperatureCities;
    }

    public void setTemperatureCities(List<String> temperatureCities) {
        this.temperatureCities = temperatureCities;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    @PostConstruct
    public void init() {
        this.setConnection();
    }

    private void getAllCities() {
        MunicipioDAO citiesDao = new MunicipioDAO(this.connection);

        if (this.type.equals("T")) this.temperatureCities = citiesDao.readTemperatureCities();
        else if (this.type.equals("D")) this.deforestationCities = citiesDao.readDeforestationCities();
        else if (this.type.equals("R")) {
            this.temperatureCities = citiesDao.readTemperatureCities();
            this.deforestationCities = citiesDao.readDeforestationCities();
        }
    }

    public void renderTemperature() {
        if (this.temperatureCity == null) {
            this.lineModel = new LineChartModel();
            return;
        }

        this.createTemperatureChart();
    }

    public void renderDeforestation() {
        if (this.deforestationCity == null) {
            this.lineModel2 = new LineChartModel();
            return;
        }

        this.createDeforestationChart();
    }

    private void createTemperatureChart() {
        ChartData data = new ChartData();
        LineChartDataSet dataSet = new LineChartDataSet();
        MedicaoClimaDAO weatherDao = new MedicaoClimaDAO(this.connection);
        List<TemperatureDataDTO> temperatureData = weatherDao.readTemperatureData(this.temperatureCity);
        List<Object> maxValues = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        Months[] month = Months.values();

        for (var e : temperatureData) {
            labels.add(generateLabel(e.getYear(), month[e.getMonth() - 1].getValue()));
            maxValues.add(e.getMax());
        }

        dataSet.setData(maxValues);
        dataSet.setLabel("Temperatura máxima");
        dataSet.setFill(false);
        dataSet.setBorderColor("rgb(200, 0, 0)");
        dataSet.setTension(0.1);

        data.addChartDataSet(dataSet);
        data.setLabels(labels);
        lineModel.setData(data);

        LineChartOptions options = new LineChartOptions();

        Title title = new Title();
        title.setDisplay(true);
        title.setText(this.temperatureCity);
        options.setTitle(title);

        lineModel.setOptions(options);
    }

    private void createDeforestationChart() {
        ChartData data = new ChartData();
        LineChartDataSet dataSet = new LineChartDataSet();
        DesmatamentoDAO dao = new DesmatamentoDAO(this.connection);
        List<DeforestationDataDTO> deforestationData = dao.readDeforestationData(this.deforestationCity);
        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (var e : deforestationData) {
            labels.add(String.valueOf(e.getYear()));
            values.add(e.getDeforestation());
        }

        dataSet.setData(values);
        dataSet.setLabel("Taxa de desmatamento");
        dataSet.setFill(false);
        dataSet.setBorderColor("rgb(0, 0, 200)");
        dataSet.setTension(0.1);

        data.addChartDataSet(dataSet);
        data.setLabels(labels);
        lineModel2.setData(data);

        LineChartOptions options = new LineChartOptions();

        Title title = new Title();
        title.setDisplay(true);
        title.setText(this.deforestationCity);
        options.setTitle(title);

        lineModel2.setOptions(options);
    }

    private String generateLabel(int year, String monthName) {
        return monthName + " - " + year;
    }
}
