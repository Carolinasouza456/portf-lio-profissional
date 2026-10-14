package br.com.portfolio.configuracao;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Configura a troca de idioma (português / inglês).
 * O visitante escolhe com ?lang=pt ou ?lang=en e a escolha fica salva em um cookie.
 */
@Configuration
public class ConfiguracaoIdioma implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolvedor = new CookieLocaleResolver("idioma");
        resolvedor.setDefaultLocale(Locale.forLanguageTag("pt-BR"));
        return resolvedor;
    }

    @Bean
    public LocaleChangeInterceptor interceptadorDeIdioma() {
        LocaleChangeInterceptor interceptador = new LocaleChangeInterceptor();
        interceptador.setParamName("lang");
        return interceptador;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(interceptadorDeIdioma());
    }
}
