# Portfólio Profissional — Carolina Souza

Site de portfólio profissional desenvolvido em **Java com Spring Boot**, com páginas **Sobre Mim (português e inglês)**, **Projetos (linha do tempo)**, **Experiências** e **Contato (com envio de e-mail)**. 

Projeto do **Laboratório 01 — Portfólio Profissional** da disciplina *Desenvolvimento e Integração de Aplicações Web* (DIAW), Engenharia de Software, PUC Minas.

---

## Índice

- [Links Úteis](#links-úteis)
- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades Principais](#funcionalidades-principais)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [Testes](#testes)
- [Documentações Utilizadas](#documentações-utilizadas)
- [Autores](#autores)

---

## Links Úteis

- **Site publicado na nuvem (Render):** 
- **Repositório:** <https://github.com/Carolinasouza456/portf-lio-profissional>

## Sobre o Projeto

O objetivo é apresentar minha trajetória, habilidades, projetos e formas de contato de maneira moderna e acessível. O site foi pensado para ser simples de manter: os textos ficam em arquivos de mensagens (português e inglês) e as listas de projetos e experiências ficam em classes de serviço, sem necessidade de banco de dados.

As escolhas principais foram:

- **Renderização no servidor (Spring Boot + Thymeleaf):** páginas rápidas, boas para SEO e sem a complexidade de um front-end separado.
- **Internacionalização (i18n):** um único conjunto de páginas, com os textos trocados entre português e inglês pelo botão **PT | EN** do menu. A escolha fica salva em um cookie.
- **Envio de e-mail por API HTTP (Resend):** a hospedagem gratuita do Render bloqueia portas SMTP, então o formulário usa a API do Resend, que funciona local e na nuvem.

---

## Funcionalidades Principais

- **Sobre Mim:** apresentação em português e inglês com formação, área de atuação, interesses, objetivos, idiomas e tecnologias.
- **Projetos:** linha do tempo do mais antigo ao mais recente. Cada projeto mostra nome, descrição, tecnologias, link do GitHub e imagens das telas, que podem ser ampliadas.
- **Experiências:** lista com instituição, cargo/atividade, período e descrição. Ainda não tenho nenhuma, então o site consta com um layout de como ficaria.
- **Contato:** ícones clicáveis (e-mail, WhatsApp, GitHub e LinkedIn) e formulário com nome, e-mail e mensagem, validado no navegador e no servidor.
- **Design responsivo:** menu recolhível no celular, layout em colunas que se ajusta ao tamanho da tela.
- **Acessibilidade:** navegação por teclado, foco visível, textos alternativos nas imagens e respeito à preferência por menos animações.

---

## Tecnologias Utilizadas

### Front-end

- **Thymeleaf 3** (templates HTML renderizados no servidor)
- **HTML5, CSS3 e JavaScript** puros (sem frameworks de front-end)
- **Fontes:** Cormorant Garamond e Jost (Google Fonts)

### Back-end

- **Java 25**
- **Spring Boot 3.5.16** (Spring MVC)
- **Bean Validation** (validação do formulário)
- **API do Resend** (envio de e-mail, via `java.net.http.HttpClient`)

### Infraestrutura

- **Maven 3.9+**
- **Docker** (build em duas etapas)
- **Render** (hospedagem gratuita)
- **GitHub** (código e versionamento)

### Dependências do projeto (`pom.xml`)

| Dependência | Para que serve |
| --- | --- |
| `spring-boot-starter-web` | Servidor web e controllers MVC |
| `spring-boot-starter-thymeleaf` | Templates HTML |
| `spring-boot-starter-validation` | Validação dos campos do formulário |
| `spring-boot-starter-test` | Testes (JUnit 5 e MockMvc) |

---

## Arquitetura

O projeto segue o padrão **MVC** em camadas simples:

```
Navegador ──► Controller ──► Serviço ──► Modelo
                 │
                 └──► Template Thymeleaf (HTML) + messages_*.properties (textos PT/EN)
```

- **controller:** recebe as requisições (`PaginaController` para Sobre, Projetos e Experiências; `ContatoController` para o formulário).
- **servico:** guarda a lista de projetos e experiências e faz o envio de e-mail (`EmailServico`).
- **modelo:** classes de dados (`Projeto`, `Experiencia`, `FormularioContato`).
- **configuracao:** troca de idioma com `?lang=pt` ou `?lang=en`, salva em cookie.
---

### Variáveis de Ambiente

| Variável | Descrição | Exemplo |
| --- | --- | --- |
| `RESEND_API_KEY` | Chave da API do Resend (necessária para o envio de e-mail) | `re_xxxxxxxx` |
| `EMAIL_DESTINO` | E-mail que recebe as mensagens (o mesmo da conta no Resend) | `carolina.amsouza0405@gmail.com` |
| `REPOSITORIO_LOPUTYN` | Link do repositório do projeto da galeria Loputyn | `https://github.com/Carolinasouza456/...` |
| `EMAIL_REMETENTE` | Remetente exibido no e-mail (opcional) | `Portfolio Carolina <onboarding@resend.dev>` |
| `PORT` | Porta do servidor (o Render define sozinho) | `8080` |


## Estrutura de Pastas

```
.
├── Dockerfile                  # Build em duas etapas (Maven + JRE 25)
├── render.yaml                 # Configuração opcional do Render
├── pom.xml                     # Dependências e build
├── .env.example                # Nomes das variáveis de ambiente
├── README.md
├── LICENSE
├── docs/
│   └── img/                    # Prints e wireframes usados neste README
└── src
    ├── main
    │   ├── java/br/com/portfolio
    │   │   ├── PortfolioApplication.java
    │   │   ├── configuracao/   # Troca de idioma (PT/EN)
    │   │   ├── controller/     # PaginaController, ContatoController
    │   │   ├── modelo/         # Projeto, Experiencia, FormularioContato
    │   │   └── servico/        # ProjetoServico, ExperienciaServico, EmailServico
    │   └── resources
    │       ├── application.properties
    │       ├── messages.properties      # Textos em português
    │       ├── messages_en.properties   # Textos em inglês
    │       ├── templates/               # Páginas Thymeleaf
    │       │   ├── fragmentos/base.html # Cabeçalho, menu e rodapé
    │       │   ├── sobre.html
    │       │   ├── projetos.html
    │       │   ├── experiencias.html
    │       │   └── contato.html
    │       └── static/
    │           ├── css/estilo.css
    │           ├── js/site.js
    │           └── img/             # Foto e prints dos projetos
    └── test/java/br/com/portfolio   # Testes das páginas e do formulário
```

## Testes

```bash
mvn test
```

Os testes (JUnit 5 e MockMvc) verificam:

- as quatro páginas respondem com sucesso;
- a troca de idioma para inglês funciona;
- a linha do tempo mostra o projeto cadastrado;
- o formulário rejeita dados inválidos e, sem chave do Resend, avisa que o envio não está configurado.

---

## Documentações Utilizadas

- [Spring Boot](https://docs.spring.io/spring-boot/index.html)
- [Thymeleaf](https://www.thymeleaf.org/documentation.html)
- [Resend — API de e-mail](https://resend.com/docs/api-reference/emails/send-email)
- [Render — Deploy com Docker](https://render.com/docs/docker)

---

## Autores

| Nome | GitHub | E-mail |
| --- | --- | --- |
| Carolina Almeida Mendes de Souza | [Carolinasouza456](https://github.com/Carolinasouza456) | [carolina.amsouza0405@gmail.com](mailto:carolina.amsouza0405@gmail.com) |

---