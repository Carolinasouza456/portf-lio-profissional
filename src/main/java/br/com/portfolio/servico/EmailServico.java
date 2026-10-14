package br.com.portfolio.servico;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.portfolio.modelo.FormularioContato;

@Service
public class EmailServico {

    public enum Resultado {
        ENVIADO, NAO_CONFIGURADO, FALHA
    }

    private static final Logger log = LoggerFactory.getLogger(EmailServico.class);
    private static final String URL_API = "https://api.resend.com/emails";

    private final String chaveApi;
    private final String remetente;
    private final String destino;
    private final ObjectMapper conversorJson = new ObjectMapper();
    private final HttpClient clienteHttp = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public EmailServico(
            @Value("${portfolio.email.resend-api-key}") String chaveApi,
            @Value("${portfolio.email.remetente}") String remetente,
            @Value("${portfolio.contato.email-destino}") String destino) {
        this.chaveApi = chaveApi;
        this.remetente = remetente;
        this.destino = destino;
    }

    public Resultado enviar(FormularioContato formulario) {
        if (chaveApi == null || chaveApi.isBlank()) {
            log.warn("RESEND_API_KEY não configurada: a mensagem não foi enviada.");
            return Resultado.NAO_CONFIGURADO;
        }

        try {
            String nomeLimpo = formulario.getNome().replaceAll("[\\r\\n]+", " ").trim();

            String corpoJson = conversorJson.writeValueAsString(Map.of(
                    "from", remetente,
                    "to", List.of(destino),
                    "reply_to", formulario.getEmail().trim(),
                    "subject", "Portfólio: nova mensagem de " + nomeLimpo,
                    "text", "Nome: " + nomeLimpo + "\n"
                            + "E-mail: " + formulario.getEmail().trim() + "\n\n"
                            + formulario.getMensagem()));

            HttpRequest requisicao = HttpRequest.newBuilder(URI.create(URL_API))
                    .timeout(Duration.ofSeconds(15))
                    .header("Authorization", "Bearer " + chaveApi)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(corpoJson))
                    .build();

            HttpResponse<String> resposta = clienteHttp.send(requisicao, HttpResponse.BodyHandlers.ofString());

            if (resposta.statusCode() >= 200 && resposta.statusCode() < 300) {
                return Resultado.ENVIADO;
            }
            log.error("Falha ao enviar e-mail. Status {}: {}", resposta.statusCode(), resposta.body());
            return Resultado.FALHA;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Resultado.FALHA;
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail", e);
            return Resultado.FALHA;
        }
    }
}
