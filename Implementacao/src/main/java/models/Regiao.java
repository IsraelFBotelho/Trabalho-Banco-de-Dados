package models;

public class Regiao {
    private int id;
    private String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNomeByAcronym(String acronym) {
        switch (acronym) {
            case "CO": {
                this.nome = "CENTRO-OESTE";
                break;
            }

            case "N": {
                this.nome = "NORTE";
                break;
            }

            case "S": {
                this.nome = "SUL";
                break;
            }

            case "NE": {
                this.nome = "NORDESTE";
                break;
            }

            case "SE": {
                this.nome = "SUDESTE";
                break;
            }

            default: {
                this.nome = null;
                break;
            }
        }
    }
}
