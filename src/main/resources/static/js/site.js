// Scripts do portfólio: menu no celular, animação de entrada e ampliação de imagens.

(function () {
    "use strict";

    // ---------- Menu no celular ----------
    var botaoMenu = document.querySelector(".menu-botao");
    var menu = document.getElementById("menu-principal");

    if (botaoMenu && menu) {
        botaoMenu.addEventListener("click", function () {
            var aberto = menu.classList.toggle("aberto");
            botaoMenu.setAttribute("aria-expanded", String(aberto));
        });

        // Fecha o menu ao clicar em um link
        menu.addEventListener("click", function (evento) {
            if (evento.target.closest("a")) {
                menu.classList.remove("aberto");
                botaoMenu.setAttribute("aria-expanded", "false");
            }
        });
    }

    // ---------- Animação de entrada ----------
    var elementos = document.querySelectorAll(".aparecer");

    if ("IntersectionObserver" in window) {
        var observador = new IntersectionObserver(function (entradas) {
            entradas.forEach(function (entrada) {
                if (entrada.isIntersecting) {
                    entrada.target.classList.add("visivel");
                    observador.unobserve(entrada.target);
                }
            });
        }, { threshold: 0.08 });

        elementos.forEach(function (elemento) {
            observador.observe(elemento);
        });
    } else {
        elementos.forEach(function (elemento) {
            elemento.classList.add("visivel");
        });
    }

    // ---------- Ampliação das imagens dos projetos ----------
    var botoesImagem = document.querySelectorAll("button[data-galeria]");
    if (botoesImagem.length === 0) {
        return;
    }

    var textoFechar = document.documentElement.lang === "en" ? "Close" : "Fechar";
    var textoAnterior = document.documentElement.lang === "en" ? "Previous" : "Anterior";
    var textoProxima = document.documentElement.lang === "en" ? "Next" : "Próxima";

    var camada = document.createElement("div");
    camada.className = "ampliacao";
    camada.setAttribute("role", "dialog");
    camada.setAttribute("aria-modal", "true");
    camada.innerHTML =
        '<button type="button" class="ampliacao__fechar" aria-label="' + textoFechar + '">&times;</button>' +
        '<button type="button" class="ampliacao__anterior" aria-label="' + textoAnterior + '">&#8249;</button>' +
        '<img alt="">' +
        '<button type="button" class="ampliacao__proxima" aria-label="' + textoProxima + '">&#8250;</button>' +
        '<p class="ampliacao__legenda"></p>';
    document.body.appendChild(camada);

    var imagemAmpliada = camada.querySelector("img");
    var legenda = camada.querySelector(".ampliacao__legenda");
    var botaoFechar = camada.querySelector(".ampliacao__fechar");
    var botaoAnterior = camada.querySelector(".ampliacao__anterior");
    var botaoProxima = camada.querySelector(".ampliacao__proxima");

    var grupoAtual = [];
    var posicao = 0;
    var botaoOrigem = null;

    function imagensDoGrupo(nomeGrupo) {
        var lista = [];
        document.querySelectorAll('.projeto__miniaturas button[data-galeria="' + nomeGrupo + '"]')
            .forEach(function (botao) {
                lista.push({ src: botao.dataset.src, alt: botao.dataset.alt });
            });
        return lista;
    }

    function mostrar() {
        var item = grupoAtual[posicao];
        imagemAmpliada.src = item.src;
        imagemAmpliada.alt = item.alt;
        legenda.textContent = (posicao + 1) + " / " + grupoAtual.length;

        var varias = grupoAtual.length > 1;
        botaoAnterior.hidden = !varias;
        botaoProxima.hidden = !varias;
    }

    function abrir(botao) {
        grupoAtual = imagensDoGrupo(botao.dataset.galeria);
        posicao = 0;
        for (var i = 0; i < grupoAtual.length; i++) {
            if (grupoAtual[i].src === botao.dataset.src) {
                posicao = i;
                break;
            }
        }
        botaoOrigem = botao;
        mostrar();
        camada.classList.add("aberta");
        document.body.style.overflow = "hidden";
        botaoFechar.focus();
    }

    function fechar() {
        camada.classList.remove("aberta");
        document.body.style.overflow = "";
        if (botaoOrigem) {
            botaoOrigem.focus();
        }
    }

    function mover(passo) {
        posicao = (posicao + passo + grupoAtual.length) % grupoAtual.length;
        mostrar();
    }

    botoesImagem.forEach(function (botao) {
        botao.addEventListener("click", function () {
            abrir(botao);
        });
    });

    botaoFechar.addEventListener("click", fechar);
    botaoAnterior.addEventListener("click", function () { mover(-1); });
    botaoProxima.addEventListener("click", function () { mover(1); });

    camada.addEventListener("click", function (evento) {
        if (evento.target === camada) {
            fechar();
        }
    });

    document.addEventListener("keydown", function (evento) {
        if (!camada.classList.contains("aberta")) {
            return;
        }
        if (evento.key === "Escape") {
            fechar();
        } else if (evento.key === "ArrowLeft") {
            mover(-1);
        } else if (evento.key === "ArrowRight") {
            mover(1);
        }
    });
})();
