package br.com.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.portfolio.servico.ExperienciaServico;
import br.com.portfolio.servico.ProjetoServico;

/** Páginas de conteúdo: Sobre Mim, Projetos e Experiências. */
@Controller
public class PaginaController {

    private final ProjetoServico projetoServico;
    private final ExperienciaServico experienciaServico;

    public PaginaController(ProjetoServico projetoServico, ExperienciaServico experienciaServico) {
        this.projetoServico = projetoServico;
        this.experienciaServico = experienciaServico;
    }

    @GetMapping("/")
    public String sobre(Model modelo) {
        modelo.addAttribute("paginaAtiva", "sobre");
        return "sobre";
    }

    @GetMapping("/projetos")
    public String projetos(Model modelo) {
        modelo.addAttribute("paginaAtiva", "projetos");
        modelo.addAttribute("projetosPorAno", projetoServico.agruparPorAno());
        return "projetos";
    }

    @GetMapping("/experiencias")
    public String experiencias(Model modelo) {
        modelo.addAttribute("paginaAtiva", "experiencias");
        modelo.addAttribute("experiencias", experienciaServico.listar());
        return "experiencias";
    }
}
