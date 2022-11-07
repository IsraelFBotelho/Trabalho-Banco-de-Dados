package control;

import dao.HistoricoDAO;
import jdbc.PgConnectionFactory;
import models.Historico;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Named
@ManagedBean
@RequestScoped
public class HistoricoController {
    private Connection connection = null;
    private List<Historico> info;

    public List<Historico> getInfo() {
        return info;
    }

    public void setInfo(List<Historico> info) {
        this.info = info;
    }

    private void setConnection() {
        try {
            PgConnectionFactory pgConnectionFactory = new PgConnectionFactory();
            this.connection = pgConnectionFactory.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void get() {
        this.setConnection();

        HistoricoDAO dao = new HistoricoDAO(this.connection);
        info = dao.read();
    }
}
