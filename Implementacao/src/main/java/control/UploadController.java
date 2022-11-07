package control;

import com.opencsv.CSVReader;
import dao.*;
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
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.*;

@Named
@ManagedBean
@RequestScoped
public class UploadController {
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

    public void treatCsv() {
        this.data = new HashMap<String, String>();

        try {
            CSVReader csvReader = new CSVReader(
                    new InputStreamReader(
                            new ByteArrayInputStream(file.getContent()), "ISO-8859-1"), ';');

            this.csv = new ArrayList<Map<String, String>>();

            String[] headers = csvReader.readNext();

            if (headers.length == 2) {
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
        HistoricoDAO historyDao = new HistoricoDAO();
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
        createCity();
        createWeatherStation();
        createClimateMeasurement();
    }

    private void createCity() {
        MunicipioDAO cityDao = new MunicipioDAO();
        EstadoDAO stateDao = new EstadoDAO();
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

    private void createWeatherStation() {
        MunicipioDAO cityDao = new MunicipioDAO();
        EstacaoMetereologicaDAO weatherStationDao = new EstacaoMetereologicaDAO();
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
        MedicaoClima climateMeasurement = new MedicaoClima();
        MedicaoClimaDAO climateMeasurementDao = new MedicaoClimaDAO();
        List<MedicaoClima> climateMeasurements = new ArrayList<>();
        String weatherStationId = this.data.get("codigo");

        System.out.println(this.csv.get(209).get("DATA (YYYY-MM-DD)"));
    }

    private void populateBaseTables() {
        RegiaoDAO regionDao = new RegiaoDAO();
        EstadoDAO stateDao = new EstadoDAO();

        if (regionDao.isEmpty()) {
            // Insere a lista com todas as regiões do Brasil.
            Regiao region = new Regiao();
            regionDao.create(region.getRegionsList());
        }

        if (stateDao.isEmpty()) {
            // Insere a lista com todos os estados do Brasil.
            Estado state = new Estado();
            stateDao.create(state.getStatesList());
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
