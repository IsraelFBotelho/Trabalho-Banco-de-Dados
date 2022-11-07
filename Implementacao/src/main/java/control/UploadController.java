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
                headers[0] = "data";
                headers[1] = "hora";
            } else {
                String st1 = headers[9].replaceAll("[^0-9]", "");
                headers[10] = headers[10].replaceAll("[^A-Za-z]", "");
                headers[11] = headers[11].replaceAll("[^A-Za-z]", "");
                headers[12] = headers[12].replaceAll("[^A-Za-z]", "");
                headers[13] = headers[13].replaceAll("[^A-Za-z]", "");
                headers[14] = headers[14].replaceAll("[^A-Za-z]", "");
                headers[15] = headers[15].replaceAll("[^A-Za-z]", "");

                this.data.put("ano", st1);
                this.type = "desmatamento";
            }

            String[] columns = null;

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
        } else
            createDeforestationInfo();
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

    private int createCity(String name, String stateAcronym) {
        MunicipioDAO cityDao = new MunicipioDAO(this.connection);
        EstadoDAO stateDao = new EstadoDAO(this.connection);
        int stateId = stateDao.getStateIdByAcronym(stateAcronym);

        if (stateId != -1) {
            Municipio city = new Municipio();

            city.setNome(name);
            city.setIdEstado(stateId);

            cityDao.create(city);
        }

        return cityDao.getCityIdByName(name);
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

            Date date = Date.valueOf(e.get("data").replaceAll("/","-"));

            Time time;

            if(e.get("hora").length() == 5){
                time = Time.valueOf((e.get("hora"))+":00");
            }else{
                String aux = e.get("hora").replaceAll("[^0-9]", "");
                aux = aux.substring(0,2) + ':' + aux.substring(2,4);
                time = Time.valueOf((aux)+":00");
            }

            String str1;
            String str2;

            if(e.get("TEMPERATURA MÍNIMA NA HORA ANT. (AUT) (°C)") == ""){
                str1 = "-9999";
            }else{
                str1 = e.get("TEMPERATURA MÍNIMA NA HORA ANT. (AUT) (°C)");
            }

            if(e.get("TEMPERATURA MÁXIMA NA HORA ANT. (AUT) (°C)") == ""){
                str2 = "-9999";
            }else{
                str2 = e.get("TEMPERATURA MÁXIMA NA HORA ANT. (AUT) (°C)");
            }

            float min = Float.parseFloat(str1.replaceAll(",", "."));
            float max = Float.parseFloat(str2.replaceAll(",", "."));

            climateMeasurement.setHora(time);
            climateMeasurement.setData(date);
            climateMeasurement.setTemperaturaMaxima(max);
            climateMeasurement.setTemperaturaMinima(min);
            climateMeasurement.setCodigoEstacaoMetereologica(weatherStationId);

            climateMeasurements.add(climateMeasurement);
        }

        climateMeasurementDao.create(climateMeasurements);
    }

    // Insere informações do CSV de desmatamento.
    private void createDeforestationInfo() {
        for (Map<String, String> e : this.csv) {
            int cityId = createCity(e.get("Municipio"), e.get("Estado"));
            int geographicalAreaId = createGeographicalArea(e, cityId);
            createForest(e, cityId);
            createDeforestation(e, geographicalAreaId);
        }
    }

    // Insere informações de áreas geográficas.
    private int createGeographicalArea(Map<String, String> map, int cityId) {
        AreaGeograficaDAO geographicalAreaDao = new AreaGeograficaDAO(this.connection);

        if (cityId != -1) {
            AreaGeografica geographicalArea = new AreaGeografica();

            geographicalArea.setAreaTotal(Float.parseFloat(map.get("AreaKm2")));
            geographicalArea.setIdMunicipio(cityId);

            geographicalAreaDao.create(geographicalArea);
        }

        return geographicalAreaDao.getGeographicalAreaIdByCityId(cityId);
    }

    // Insere informações de florestas.
    private void createForest(Map<String, String> map, int cityId) {
        if (cityId == -1)
            return;

        AreaGeograficaDAO geographicalAreaDao = new AreaGeograficaDAO(this.connection);
        FlorestaDAO forestDao = new FlorestaDAO(this.connection);
        int geographicalAreaId = geographicalAreaDao.getGeographicalAreaIdByCityId(cityId);

        if (geographicalAreaId != -1) {
            Floresta forest = new Floresta();

            forest.setAreaFloresta(Float.parseFloat(map.get("Floresta")));
            forest.setAreaNaoFloresta(Float.parseFloat(map.get("NaoFloresta")));
            forest.setAno(Integer.parseInt(this.data.get("ano")));
            forest.setIdAreaGeografica(geographicalAreaId);

            forestDao.create(forest);
        }
    }

    // Insere informações de desmatamentos.
    private void createDeforestation(Map<String, String> map, int geographicalAreaId) {
        if (geographicalAreaId == -1)
            return;

        DesmatamentoDAO deforestationDao = new DesmatamentoDAO(this.connection);
        FlorestaDAO forestDao = new FlorestaDAO(this.connection);
        int forestId = forestDao.getForestIdByYearAndGeographicalAreaId(
                Integer.parseInt(this.data.get("ano")), geographicalAreaId);

        if (forestId != -1) {
            Desmatamento deforestation = new Desmatamento();

            deforestation.setTaxaIncremento(Float.parseFloat(map.get("Incremento")));
            deforestation.setAreaDesmatada(Float.parseFloat(map.get("Desmatado" + this.data.get("ano"))));
            deforestation.setIdFloresta(forestId);

            deforestationDao.create(deforestation);
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
