package br.com.portfolio.modelo;

public class Experiencia {

    private final String id;
    private final boolean exemplo;

    public Experiencia(String id, boolean exemplo) {
        this.id = id;
        this.exemplo = exemplo;
    }

    public String getId() {
        return id;
    }


    public boolean isExemplo() {
        return exemplo;
    }
}
