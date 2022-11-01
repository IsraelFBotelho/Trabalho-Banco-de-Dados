package control;

import com.opencsv.CSVReader;
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
import java.util.*;

@Named
@ManagedBean
@RequestScoped
public class UploadController {

    public List<Map<String,String>> getCsv() {
        return csv;
    }

    public void setCsv(List<Map<String,String>> csv) {
        this.csv = csv;
    }

    private List<Map<String,String>> csv;
    private UploadedFile file;
    private UploadedFiles files;
    private String dropZoneText = "Drop zone p:inputTextarea demo.";

    public void treatCsv(){
        try {


            CSVReader csvReader = new CSVReader(
                    new InputStreamReader(
                            new ByteArrayInputStream(file.getContent())), ';');

            this.csv = new ArrayList<Map<String,String>>();

            String[] headers = csvReader.readNext();
            String[] colunas = null;

            while ((colunas = csvReader.readNext()) != null) {

                Map<String, String> campos = new HashMap<String, String>();

                for(int i = 0; i < colunas.length; i++){
                    campos.put(headers[i], colunas[i]);
                }
                this.csv.add(campos);
            }

        }
        catch (Exception e ){
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
