package br.com.portfolio.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.portfolio.modelo.FormularioContato;
import br.com.portfolio.servico.EmailServico;
import jakarta.validation.Valid;

/** Página de contato: ícones clicáveis e formulário com envio de e-mail. */
@Controller
public class ContatoController {

    private final EmailServico emailServico;
    private final String email;
    private final String whatsapp;
    private final String whatsappExibicao;
    private final String github;
    private final String linkedin;

    public ContatoController(
            EmailServico emailServico,
            @Value("${portfolio.contato.email-destino}") String email,
            @Value("${portfolio.contato.whatsapp}") String whatsapp,
            @Value("${portfolio.contato.whatsapp-exibicao}") String whatsappExibicao,
            @Value("${portfolio.contato.github}") String github,
            @Value("${portfolio.contato.linkedin}") String linkedin) {
        this.emailServico = emailServico;
        this.email = email;
        this.whatsapp = whatsapp;
        this.whatsappExibicao = whatsappExibicao;
        this.github = github;
        this.linkedin = linkedin;
    }

    /** Dados que toda renderização da página de contato precisa. */
    private void prepararPagina(Model modelo) {
        modelo.addAttribute("paginaAtiva", "contato");
        modelo.addAttribute("email", email);
        modelo.addAttribute("whatsapp", whatsapp);
        modelo.addAttribute("whatsappExibicao", whatsappExibicao);
        modelo.addAttribute("github", github);
        modelo.addAttribute("linkedin", linkedin);
    }

    @GetMapping("/contato")
    public String exibir(Model modelo) {
        prepararPagina(modelo);
        if (!modelo.containsAttribute("formulario")) {
            modelo.addAttribute("formulario", new FormularioContato());
        }
        return "contato";
    }

    @PostMapping("/contato")
    public String enviar(
            @Valid @ModelAttribute("formulario") FormularioContato formulario,
            BindingResult erros,
            RedirectAttributes redirecionamento,
            Model modelo) {

        // Se houver erro de validação, mostra a mesma página com as mensagens
        if (erros.hasErrors()) {
            prepararPagina(modelo);
            return "contato";
        }

        EmailServico.Resultado resultado = emailServico.enviar(formulario);

        // Padrão "post-redirect-get": evita reenviar o formulário ao atualizar a página
        redirecionamento.addFlashAttribute("resultadoEnvio", resultado.name());
        return "redirect:/contato";
    }
}
