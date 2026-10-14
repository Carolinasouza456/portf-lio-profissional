package br.com.portfolio.servico;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.portfolio.modelo.Experiencia;

@Service
public class ExperienciaServico {

    private final List<Experiencia> experiencias = List.of(
            new Experiencia("exemplo", true));

    public List<Experiencia> listar() {
        return experiencias;
    }
}
