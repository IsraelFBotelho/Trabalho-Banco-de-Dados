package models;

public class Floresta {
    private int id;
    private float areaFloresta;
    private float areaNaoFloresta;
    private int ano;
    private int idAreaGeografica;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAreaFloresta() {
        return areaFloresta;
    }

    public void setAreaFloresta(float areaFloresta) {
        this.areaFloresta = areaFloresta;
    }

    public float getAreaNaoFloresta() {
        return areaNaoFloresta;
    }

    public void setAreaNaoFloresta(float areaNaoFloresta) {
        this.areaNaoFloresta = areaNaoFloresta;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getIdAreaGeografica() {
        return idAreaGeografica;
    }

    public void setIdAreaGeografica(int idAreaGeografica) {
        this.idAreaGeografica = idAreaGeografica;
    }
}
