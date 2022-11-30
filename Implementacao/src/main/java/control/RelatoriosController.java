package control;

import dao.MedicaoClimaDAO;
import dao.MunicipioDAO;
import dto.TemperatureDataDTO;
import enums.Months;
import jdbc.PgConnectionFactory;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Named
@ManagedBean
@RequestScoped
public class RelatoriosController {
    private Connection connection = null;
    private String tipo;
    private String regiao;
    private List<String> cities = new ArrayList<>();
    private LineChartModel lineModel;
    private LineChartModel cartesianLinerModel;
    private String lastSelected;

    private void setConnection() {
        try {
            PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(String lastSelected) {
        this.lastSelected = lastSelected;
    }

    public boolean testLastSelected() {
        System.out.println(this.lastSelected);
        return this.lastSelected != null;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        if (this.cities.isEmpty()) {
            MunicipioDAO citiesDao = new MunicipioDAO(this.connection);
            this.cities = citiesDao.readCities();
        }
    }

    public void renderCity() {
        this.tipo = "C";
        this.getAllCities();
        this.createLineModel(this.lastSelected);
    }

    public void handleChange() {
        System.out.println("Atualizando cidades.");
    }

    private void createLineModel(String cityName) {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        if (this.lastSelected != null) {
            MedicaoClimaDAO weatherDao = new MedicaoClimaDAO(this.connection);
            List<TemperatureDataDTO> temperatureData = weatherDao.readTemperatureData(cityName);

            LineChartDataSet dataSet = new LineChartDataSet();
            data.addChartDataSet(dataSet);

            List<String> labels = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            Months[] month = Months.values();

            for (var e : temperatureData) {
                labels.add(generateLabel(e.getYear(), month[e.getMonth() - 1].getValue()));
                values.add(e.getMaxAvg());
            }

            dataSet.setData(values);
            dataSet.setFill(false);
            dataSet.setLabel("Média das temperaturas máximas");
            dataSet.setBorderColor("rgb(75, 192, 192)");
            dataSet.setTension(0.1);

            dataSet.setLabel("Média das temperaturas mínimas");
            dataSet.setBorderColor("rgb(255, 192, 192)");
            dataSet.setTension(0.1);

            data.setLabels(labels);

            // Options
            LineChartOptions options = new LineChartOptions();
            Title title = new Title();
            title.setDisplay(true);
            title.setText("Line Chart");
            options.setTitle(title);

            lineModel.setOptions(options);
            lineModel.setData(data);
        }
    }

    private String generateLabel(int year, String monthName) {
        return monthName + " - " + year;
    }
}
