package models;

public class Desmatamento {
    private float taxaIncremento;
    private float areaDesmatada;
    private int idFloresta;

    public float getTaxaIncremento() {
        return taxaIncremento;
    }

    public void setTaxaIncremento(float taxaIncremento) {
        this.taxaIncremento = taxaIncremento;
    }

    public float getAreaDesmatada() {
        return areaDesmatada;
    }

    public void setAreaDesmatada(float areaDesmatada) {
        this.areaDesmatada = areaDesmatada;
    }

    public int getIdFloresta() {
        return idFloresta;
    }

    public void setIdFloresta(int idFloresta) {
        this.idFloresta = idFloresta;
    }
}
