package control;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@ManagedBean
@RequestScoped
public class HistoricoController {

    public List<Map<String, String>> getInfo() {
        return info;
    }

    public void setInfo(List<Map<String, String>> info) {
        this.info = info;
    }

    private List<Map<String,String>> info;

    public void get(){
        System.out.println("ativou");
//        Dao para buscar as info do historico
    }

}
