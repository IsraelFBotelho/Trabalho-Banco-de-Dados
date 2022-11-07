package models;

import dao.RegiaoDAO;

import java.util.ArrayList;
import java.util.List;

public class Estado {
    private int id;
    private String sigla;
    private String nome;
    private int idRegiao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdRegiao() {
        return idRegiao;
    }

    public void setIdRegiao(int idRegiao) {
        this.idRegiao = idRegiao;
    }

    public List<Estado> getStatesList() {
        List<Estado> statesList = new ArrayList<>();
        String states = "AC;Acre;N;" +
                "AL;Alagoas;NE;" +
                "AP;Amapá;N;" +
                "AM;Amazonas;N;" +
                "BA;Bahia;NE;" +
                "CE;Ceará;NE;" +
                "ES;Espírito Santo;SE;" +
                "GO;Goiás;CO;" +
                "MA;Maranhão;NE;" +
                "MT;Mato Grosso;CO;" +
                "MS;Mato Grosso do Sul;CO;" +
                "MG;Minas Gerais;SE;" +
                "PA;Pará;N;" +
                "PB;Paraíba;NE;" +
                "PR;Paraná;S;" +
                "PE;Pernambubo;NE;" +
                "PI;Piauí;NE;" +
                "RJ;Rio de Janeiro;SE;" +
                "RN;Rio Grande do Norte;NE;" +
                "RS;Rio Grande do Sul;S;" +
                "RO;Rondônia;N;" +
                "RR;Roraima;N;" +
                "SC;Santa Catarina;S;" +
                "SP;São Paulo;SE;" +
                "SE;Sergipe;NE;" +
                "TO;Tocantins;N;" +
                "DF;Distrito Federal;CO";

        RegiaoDAO regionDao = new RegiaoDAO();
        Regiao region = new Regiao();
        String[] splited = states.split(";");
        int count = 0;

        while (count < splited.length) {
            Estado s = new Estado();

            s.setSigla(splited[count]);
            s.setNome(splited[count + 1]);
            s.setIdRegiao(regionDao.getRegionId(region.getNameByAcronym(splited[count + 2])));

            statesList.add(s);
            count += 3;
        }

        return statesList;
    }
}
