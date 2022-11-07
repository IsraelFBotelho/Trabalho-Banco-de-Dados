package models;

import java.util.ArrayList;
import java.util.List;

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

    public void setNameByAcronym(String acronym) {
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

    public String getNameByAcronym(String acronym) {
        switch (acronym) {
            case "CO":
                return "CENTRO-OESTE";

            case "N":
                return "NORTE";

            case "S":
                return "SUL";

            case "NE":
                return "NORDESTE";

            case "SE":
                return "SUDESTE";

            default:
                return null;
        }
    }

    public List<Regiao> getRegionsList() {
        String[] regionsName = {"CENTRO-OESTE", "NORTE", "SUL", "NORDESTE", "SUDESTE"};
        List<Regiao> regionsList = new ArrayList<>();

        for (String r : regionsName) {
            Regiao region = new Regiao();

            region.setNome(r);
            regionsList.add(region);
        }

        return regionsList;
    }
}
