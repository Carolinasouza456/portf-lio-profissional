package br.com.portfolio.servico;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.portfolio.modelo.Projeto;

@Service
public class ProjetoServico {

    private final List<Projeto> projetos;

    public ProjetoServico(
            @Value("${portfolio.projetos.loputyn-repositorio}") String repositorioLoputyn) {

        this.projetos = List.of(
                new Projeto(
                        "loputyn",
                        2026,
                        6,
                        List.of("HTML5", "CSS3", "JavaScript"),
                        repositorioLoputyn,
                        List.of(
                                "/img/loputyn-destaques.jpg",
                                "/img/loputyn-obras.jpg",
                                "/img/loputyn-linha-do-tempo.jpg",
                                "/img/loputyn-sobre.jpg")));
    }


    public List<Projeto> listarOrdenados() {
        return projetos.stream()
                .sorted(Comparator.comparingInt(Projeto::getAno).thenComparingInt(Projeto::getMes))
                .toList();
    }

    public Map<Integer, List<Projeto>> agruparPorAno() {
        return listarOrdenados().stream()
                .collect(Collectors.groupingBy(Projeto::getAno, TreeMap::new, Collectors.toList()));
    }
}
