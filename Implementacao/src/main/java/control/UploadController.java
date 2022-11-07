package control;

import com.opencsv.CSVReader;
import dao.*;
import jdbc.PgConnectionFactory;
import models.*;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;
import org.primefaces.util.EscapeUtils;

import javax.faces.bean.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

@Named
@ManagedBean
@RequestScoped
public class UploadController {
    private Connection connection = null;
    private String type;
    private List<Map<String, String>> csv;
    private UploadedFile file;
    private UploadedFiles files;
    private Map<String, String> data;
    private String dropZoneText = "Drop zone p:inputTextarea demo.";

    public List<Map<String, String>> getCsv() {
        return csv;
    }

    public void setCsv(List<Map<String, String>> csv) {
        this.csv = csv;
    }

    private void setConnection() {
        try {
            PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void treatCsv() {
        this.data = new HashMap<String, String>();

        try {
            CSVReader csvReader = new CSVReader(
                    new InputStreamReader(
                            new ByteArrayInputStream(file.getContent()), "UTF-8"), ';');

            this.csv = new ArrayList<Map<String, String>>();
            String[] headers = csvReader.readNext();

            if (headers.length == 2) {
                csvReader = new CSVReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(file.getContent()), "ISO-8859-1"), ';');
                headers = csvReader.readNext();
                this.type = "clima";
                this.data.put("regiao", headers[1]);
                headers = csvReader.readNext();
                this.data.put("uf", headers[1]);
                headers = csvReader.readNext();
                this.data.put("nome", headers[1]);
                headers = csvReader.readNext();
                this.data.put("codigo", headers[1]);
                headers = csvReader.readNext();
                headers = csvReader.readNext();
                headers = csvReader.readNext();
                headers = csvReader.readNext();
                headers = csvReader.readNext();
            } else {
                String st1 = headers[9].replaceAll("[^0-9]", "");
                this.data.put("ano", st1);
                this.type = "desmatamento";
            }

            String[] columns = null;

            System.out.println(headers[0]);

            while ((columns = csvReader.readNext()) != null) {
                Map<String, String> campos = new HashMap<String, String>();

                for (int i = 0; i < columns.length; i++) {
                    campos.put(headers[i], columns[i]);
                }

                this.csv.add(campos);
            }

            this.setConnection();
            this.createHistory();
            this.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload() {
        if (file != null) {
            treatCsv();
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void uploadMultiple() {
        if (files != null) {
            for (UploadedFile f : files.getFiles()) {
                FacesMessage message = new FacesMessage("Successful", f.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void handleFileUploadTextarea(FileUploadEvent event) {
        String jsVal = "PF('textarea').jq.val";
        String fileName = EscapeUtils.forJavaScript(event.getFile().getFileName());
        PrimeFaces.current().executeScript(jsVal + "(" + jsVal + "() + '\\n\\n" + fileName + " uploaded.')");
    }

    public void handleFilesUpload(FilesUploadEvent event) {
        for (UploadedFile f : event.getFiles().getFiles()) {
            FacesMessage message = new FacesMessage("Successful", f.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private void createHistory() {
        HistoricoDAO historyDao = new HistoricoDAO(this.connection);
        List<Historico> list = new ArrayList<>();
        Historico history = new Historico();
        Date date = new Date(System.currentTimeMillis());

        history.setTipo(this.type);
        history.setData(date);
        history.setNome(this.file.getFileName());

        list.add(history);
        historyDao.create(list);
    }

    private void create() {
        populateBaseTables();

        if (this.type.equals("clima")) {
            createCity();
            createWeatherStation();
            createClimateMeasurement();
        } else {
            createGeographicalArea();
        }
    }

    private void createCity() {
        MunicipioDAO cityDao = new MunicipioDAO(this.connection);
        EstadoDAO stateDao = new EstadoDAO(this.connection);
        List<Municipio> cities = new ArrayList<>();

        int stateId = stateDao.getStateIdByAcronym(this.data.get("uf"));

        if (stateId != -1) {
            Municipio city = new Municipio();

            city.setNome(this.data.get("nome"));
            city.setIdEstado(stateId);
            cities.add(city);

            cityDao.create(cities);
        }
    }

    private void createCity(String name, String stateAcronym) {
        MunicipioDAO cityDao = new MunicipioDAO(this.connection);
        EstadoDAO stateDao = new EstadoDAO(this.connection);
        int stateId = stateDao.getStateIdByAcronym(stateAcronym);

        if (stateId != -1) {
            Municipio city = new Municipio();

            city.setNome(name);
            city.setIdEstado(stateId);
            cityDao.create(city);
        }
    }

    private void createWeatherStation() {
        MunicipioDAO cityDao = new MunicipioDAO(this.connection);
        EstacaoMetereologicaDAO weatherStationDao = new EstacaoMetereologicaDAO(this.connection);
        List<EstacaoMetereologica> weatherStations = new ArrayList<>();

        int cityId = cityDao.getCityIdByName(this.data.get("nome"));

        if (cityId != -1) {
            EstacaoMetereologica weatherStation = new EstacaoMetereologica();

            weatherStation.setCodigo(this.data.get("codigo"));
            weatherStation.setNome(this.data.get("nome"));
            weatherStation.setIdMunicipio(cityId);
            weatherStations.add(weatherStation);

            weatherStationDao.create(weatherStations);
        }
    }

    private void createClimateMeasurement() {
        MedicaoClimaDAO climateMeasurementDao = new MedicaoClimaDAO(this.connection);
        List<MedicaoClima> climateMeasurements = new ArrayList<>();
        String weatherStationId = this.data.get("codigo");

        for (Map<String, String> e : this.csv) {
            MedicaoClima climateMeasurement = new MedicaoClima();

            Date date = Date.valueOf(e.get("DATA (YYYY-MM-DD)"));
            Time time = Time.valueOf((e.get("HORA (UTC)")) + ":00");
            float min = Float.parseFloat(e.get("TEMPERATURA MÍNIMA NA HORA ANT. (AUT) (°C)").replaceAll(",", "."));
            float max = Float.parseFloat(e.get("TEMPERATURA MÁXIMA NA HORA ANT. (AUT) (°C)").replaceAll(",", "."));

            climateMeasurement.setHora(time);
            climateMeasurement.setData(date);
            climateMeasurement.setTemperaturaMaxima(max);
            climateMeasurement.setTemperaturaMinima(min);
            climateMeasurement.setCodigoEstacaoMetereologica(weatherStationId);

            climateMeasurements.add(climateMeasurement);
        }

        climateMeasurementDao.create(climateMeasurements);
    }

    private void createGeographicalArea() {
        AreaGeograficaDAO geographicalAreaDao = new AreaGeograficaDAO(this.connection);
        MunicipioDAO cityDao = new MunicipioDAO(this.connection);
        List<AreaGeografica> geographicalAreas = new ArrayList<>();

        for (Map<String, String> e : this.csv) {
            createCity(e.get("Municipio"), e.get("Estado"));

//            if (cityId != -1) {
//                EstacaoMetereologica weatherStation = new EstacaoMetereologica();
//
//                weatherStation.setCodigo(this.data.get("codigo"));
//                weatherStation.setNome(this.data.get("nome"));
//                weatherStation.setIdMunicipio(cityId);
//                weatherStations.add(weatherStation);
//
//                weatherStationDao.create(weatherStations);
//            }
        }
    }

    private void populateBaseTables() {
        RegiaoDAO regionDao = new RegiaoDAO(this.connection);
        EstadoDAO stateDao = new EstadoDAO(this.connection);

        if (regionDao.isEmpty()) {
            // Insere a lista com todas as regiões do Brasil.
            Regiao region = new Regiao();
            regionDao.create(region.getRegionsList());
        }

        if (stateDao.isEmpty()) {
            // Insere a lista com todos os estados do Brasil.
            Estado state = new Estado();
            stateDao.create(state.getStatesList(this.connection));
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFiles getFiles() {
        return files;
    }

    public void setFiles(UploadedFiles files) {
        this.files = files;
    }

    public String getDropZoneText() {
        return dropZoneText;
    }

    public void setDropZoneText(String dropZoneText) {
        this.dropZoneText = dropZoneText;
    }
}
