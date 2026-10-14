package br.com.portfolio;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

// A chave do Resend fica vazia nos testes, para nunca enviar e-mail de verdade
@SpringBootTest(properties = "portfolio.email.resend-api-key=")
@AutoConfigureMockMvc
class PaginasTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void paginaSobreEmPortuguesPorPadrao() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("sobre"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Sobre mim")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Golang")));
    }

    @Test
    void paginaSobreEmIngles() throws Exception {
        mockMvc.perform(get("/").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("About me")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Software Engineering student")));
    }

    @Test
    void paginaProjetosMostraLinhaDoTempo() throws Exception {
        mockMvc.perform(get("/projetos"))
                .andExpect(status().isOk())
                .andExpect(view().name("projetos"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Galeria Loputyn")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("2026")));
    }

    @Test
    void paginaExperienciasMostraExemplo() throws Exception {
        mockMvc.perform(get("/experiencias"))
                .andExpect(status().isOk())
                .andExpect(view().name("experiencias"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Exemplo")));
    }

    @Test
    void paginaContatoMostraCanais() throws Exception {
        mockMvc.perform(get("/contato"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("mailto:carolina.amsouza0405@gmail.com")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("wa.me/5573999667676")));
    }

    @Test
    void formularioInvalidoMostraErros() throws Exception {
        mockMvc.perform(post("/contato")
                        .param("nome", "")
                        .param("email", "nao-e-email")
                        .param("mensagem", "curta"))
                .andExpect(status().isOk())
                .andExpect(view().name("contato"))
                .andExpect(model().attributeHasFieldErrors("formulario", "nome", "email", "mensagem"));
    }

    @Test
    void formularioValidoSemChaveDeEmailAvisaQueNaoEstaConfigurado() throws Exception {
        mockMvc.perform(post("/contato")
                        .param("nome", "Visitante")
                        .param("email", "visitante@exemplo.com")
                        .param("mensagem", "Olá, gostei do seu portfólio!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .flash().attribute("resultadoEnvio", "NAO_CONFIGURADO"));
    }
}
