package models;

import java.sql.Time;
import java.util.Date;

public class MedicaoClima {
    private java.sql.Date dt;
    private java.sql.Time hora;
    private String codigoEstacaoMetereologica;
    private float temperaturaMinima;
    private float temperaturaMaxima;

    public java.sql.Date getDt() {
        return dt;
    }

    public void setDt(java.sql.Date dt) {
        this.dt = dt;
    }

    public java.sql.Time getHora() {
        return hora;
    }

    public void setHora(java.sql.Time hora) {
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
