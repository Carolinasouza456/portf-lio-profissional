package br.com.portfolio.modelo;

import java.util.List;

public class Projeto {

    private final String id;
    private final int ano;
    private final int mes;
    private final List<String> tecnologias;
    private final String repositorio;
    private final List<String> imagens;

    public Projeto(String id, int ano, int mes, List<String> tecnologias,
                   String repositorio, List<String> imagens) {
        this.id = id;
        this.ano = ano;
        this.mes = mes;
        this.tecnologias = tecnologias;
        this.repositorio = repositorio;
        this.imagens = imagens;
    }

    public String getId() {
        return id;
    }

    public int getAno() {
        return ano;
    }

    public int getMes() {
        return mes;
    }

    public List<String> getTecnologias() {
        return tecnologias;
    }

    public String getRepositorio() {
        return repositorio;
    }

    public List<String> getImagens() {
        return imagens;
    }
}
