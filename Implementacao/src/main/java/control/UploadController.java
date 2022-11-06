package control;

import com.opencsv.CSVReader;
import dao.EstadoDAO;
import dao.RegiaoDAO;
import jdbc.PgConnectionFactory;
import models.Estado;
import models.Regiao;
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
import java.sql.SQLException;
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

        this.data = new HashMap<String,String>();

        try {
            CSVReader csvReader = new CSVReader(
                    new InputStreamReader(
                            new ByteArrayInputStream(file.getContent())), ';');

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
                String st1=headers[9].replaceAll("[^0-9]", "");
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
