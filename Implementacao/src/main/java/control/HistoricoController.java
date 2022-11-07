package control;

import dao.HistoricoDAO;
import models.Historico;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@ManagedBean
@RequestScoped
public class HistoricoController {

    public List<Historico> getInfo() {
        return info;
    }

    public void setInfo(List<Historico> info) {
        this.info = info;
    }

    private List<Historico> info;

    public void get(){
        System.out.println("ativou");
        HistoricoDAO dao = new HistoricoDAO();
        info = dao.read();
    }

}
