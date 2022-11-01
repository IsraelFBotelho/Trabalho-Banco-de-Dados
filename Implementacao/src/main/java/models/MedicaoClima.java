package models;

import java.sql.Time;
import java.util.Date;

public class MedicaoClima {
    private Date dt;
    private Time hora;
    private String codigoEstacaoMetereologica;
    private float temperaturaMinima;
    private float temperaturaMaxima;

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getCodigoEstacaoMetereologica() {
        return codigoEstacaoMetereologica;
    }

    public void setCodigoEstacaoMetereologica(String codigoEstacaoMetereologica) {
        this.codigoEstacaoMetereologica = codigoEstacaoMetereologica;
    }

    public float getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public void setTemperaturaMinima(float temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }

    public float getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public void setTemperaturaMaxima(float temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }
}
