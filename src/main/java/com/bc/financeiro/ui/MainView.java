package com.bc.financeiro.ui;

import com.bc.financeiro.Cartao;
import com.bc.financeiro.Conta;
import com.bc.financeiro.ContaCorrente;
import com.bc.financeiro.ContaInvestimento;
import com.bc.financeiro.ContaPoupanca;
import com.bc.financeiro.GerenciadorTransacoes;
import com.bc.financeiro.PacotePessoaFisicaFactory;
import com.bc.financeiro.PacotePessoaJuridicaFactory;
import com.bc.financeiro.ProdutoFinanceiroFactory;
import com.bc.financeiro.Seguro;
import com.bc.financeiro.Transacao;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Route("")
public class MainView extends VerticalLayout {

    private static final DateTimeFormatter HISTORICO_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("pt", "BR"))
                    .withZone(ZoneId.systemDefault());

    private Conta contaAtual;
    private Cartao cartaoAtual;
    private Seguro seguroAtual;

    private final VerticalLayout criarProdutosLayout = new VerticalLayout();
    private final VerticalLayout operacoesLayout = new VerticalLayout();
    private final VerticalLayout historicoLayout = new VerticalLayout();

    private final Div pacoteResumo = new Div();
    private final Div contaResumo = new Div();
    private final Div cartaoResumo = new Div();
    private final Div seguroResumo = new Div();
    private final Div operacaoContaResumo = new Div();
    private final Div historicoLista = new Div();
    private final Span historicoVazio = new Span("Nenhuma movimentação registrada até o momento.");

    private ProdutoFinanceiroFactory factoryAtual;
    private Select<String> pacoteSelect;
    private Select<String> contaTipoSelect;

    public MainView() {
        factoryAtual = new PacotePessoaFisicaFactory();

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        addClassName("main-view");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        H1 titulo = new H1("Banco Digital com Factory");
        titulo.addClassName("page-title");

        Paragraph descricao = new Paragraph(
                "Este projeto demonstra a aplicação do padrão Abstract Factory em um banco digital, organizando contas, cartões e seguros de acordo com cada perfil financeiro.");
        descricao.addClassName("page-subtitle");

        Div heroBadge = new Div(new Span("Experiência bancária organizada com Abstract Factory"));
        heroBadge.addClassName("hero-badge");

        Div heroContent = new Div(titulo, descricao);
        heroContent.addClassName("hero-content");

        Div heroStats = new Div(
                criarHeroStat("2", "pacotes financeiros"),
                criarHeroStat("3", "produtos por pacote"),
                criarHeroStat("100%", "histórico visível")
        );
        heroStats.addClassName("hero-stats");

        Div heroSection = new Div(heroBadge, heroContent, heroStats);
        heroSection.addClassName("hero-section");
        heroSection.setWidthFull();

        Tab tabCriar = new Tab("Produtos");
        Tab tabOperar = new Tab("Operações");
        Tab tabHistorico = new Tab("Histórico");

        Tabs tabs = new Tabs(tabCriar, tabOperar, tabHistorico);
        tabs.setWidthFull();
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.addClassName("top-nav");

        configurarCriarProdutos();
        configurarOperacoes();
        configurarHistorico();

        Div content = new Div(criarProdutosLayout);
        content.setWidthFull();

        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            if (tabs.getSelectedTab() == tabCriar) {
                content.add(criarProdutosLayout);
            } else if (tabs.getSelectedTab() == tabOperar) {
                content.add(operacoesLayout);
            } else {
                content.add(historicoLayout);
            }
        });

        Div footer = criarRodape();
        footer.setWidthFull();

        add(heroSection, tabs, content, footer);
        atualizarFactory();
        atualizarResumoProdutos();
        atualizarOperacaoResumo();
        atualizarHistorico();
    }

    private Div criarHeroStat(String valor, String legenda) {
        Div stat = new Div();
        stat.addClassName("hero-stat");
        Span valorSpan = new Span(valor);
        valorSpan.addClassName("hero-stat-value");
        Span legendaSpan = new Span(legenda);
        legendaSpan.addClassName("hero-stat-label");
        stat.add(valorSpan, legendaSpan);
        return stat;
    }

    private Div criarRodape() {
        Div footer = new Div();
        footer.addClassName("page-footer");

        Div brand = new Div();
        brand.addClassName("footer-brand");
        Span brandTitle = new Span("Banco Digital com Factory");
        brandTitle.addClassName("footer-title");
        brand.add(brandTitle, new Paragraph("Interface pensada para destacar conta, cartão, seguro e histórico."));

        Div meta = new Div();
        meta.addClassName("footer-meta");
        meta.add(new Span("Pacotes: Pessoa Física e Pessoa Jurídica"), new Span("Layout responsivo para notebook e mobile"));

        footer.add(brand, meta);
        return footer;
    }

    private void configurarCriarProdutos() {
        criarProdutosLayout.setWidthFull();
        criarProdutosLayout.setPadding(false);
        criarProdutosLayout.setSpacing(true);
        criarProdutosLayout.addClassName("panel-section");

        H2 subtitulo = new H2("Configuração do pacote");
        subtitulo.addClassName("section-title");

        pacoteSelect = new Select<>();
        pacoteSelect.setLabel("Pacote");
        pacoteSelect.setItems("Pessoa Física", "Pessoa Jurídica");
        pacoteSelect.setValue("Pessoa Física");
        pacoteSelect.setWidth("260px");

        contaTipoSelect = new Select<>();
        contaTipoSelect.setLabel("Tipo de conta");
        contaTipoSelect.setWidth("260px");

        pacoteSelect.addValueChangeListener(event -> atualizarFactory());

        HorizontalLayout seletores = new HorizontalLayout(pacoteSelect, contaTipoSelect);
        seletores.setWidthFull();
        seletores.getStyle().set("flex-wrap", "wrap");
        seletores.addClassName("selection-row");

        pacoteResumo.addClassName("package-banner");

        Div configuracaoHeader = new Div(subtitulo);
        configuracaoHeader.addClassName("section-header");

        Div configuracaoCard = new Div(seletores, pacoteResumo);
        configuracaoCard.addClassName("config-card");

        HorizontalLayout cardsLayout = new HorizontalLayout(
                criarPainelProduto("Conta",
                        "A conta segue o pacote selecionado e respeita os tipos disponíveis na factory.",
                        criarBotaoConta()),
                criarPainelProduto("Cartão",
                        "O cartão muda junto com o pacote, exibindo bandeira e limite compatíveis.",
                        criarBotaoCartao()),
                criarPainelProduto("Seguro",
                        "O seguro criado também acompanha o contexto do cliente e aparece no histórico.",
                        criarBotaoSeguro()));
        cardsLayout.setWidthFull();
        cardsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        cardsLayout.getStyle().set("flex-wrap", "wrap");
        cardsLayout.addClassName("product-grid");

        HorizontalLayout resumoLayout = new HorizontalLayout(contaResumo, cartaoResumo, seguroResumo);
        resumoLayout.setWidthFull();
        resumoLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        resumoLayout.getStyle().set("flex-wrap", "wrap");
        resumoLayout.addClassName("summary-grid");

        criarProdutosLayout.add(configuracaoHeader, configuracaoCard, cardsLayout, resumoLayout);
    }

    private Button criarBotaoConta() {
        Button criarConta = new Button("Criar Conta", event -> {
            try {
                contaAtual = factoryAtual.criarConta(contaTipoSelect.getValue());
                registrarEvento("Criação de conta", 0.0,
                        contaAtual.getTipoConta() + " criada no pacote " + factoryAtual.getNomePacote());
                atualizarResumoProdutos();
                atualizarOperacaoResumo();
                atualizarHistorico();
                Notification.show("Conta criada com sucesso.", 2000, Notification.Position.TOP_CENTER);
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        criarConta.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        criarConta.addClassName("product-action");
        criarConta.setWidthFull();
        return criarConta;
    }

    private Button criarBotaoCartao() {
        Button criarCartao = new Button("Criar Cartão", event -> {
            cartaoAtual = factoryAtual.criarCartao();
            registrarEvento("Criação de cartão", cartaoAtual.getLimiteCredito(),
                    "Cartão " + cartaoAtual.getBandeira() + " emitido para " + factoryAtual.getNomePacote());
            atualizarResumoProdutos();
            atualizarHistorico();
            Notification.show("Cartão criado com sucesso.", 2000, Notification.Position.TOP_CENTER);
        });
        criarCartao.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        criarCartao.addClassName("product-action");
        criarCartao.setWidthFull();
        return criarCartao;
    }

    private Button criarBotaoSeguro() {
        Button criarSeguro = new Button("Criar Seguro", event -> {
            seguroAtual = factoryAtual.criarSeguro();
            registrarEvento("Criação de seguro", seguroAtual.getValorPremio(),
                    "Seguro " + seguroAtual.getTipoApolice() + " vinculado ao pacote " + factoryAtual.getNomePacote());
            atualizarResumoProdutos();
            atualizarHistorico();
            Notification.show("Seguro criado com sucesso.", 2000, Notification.Position.TOP_CENTER);
        });
        criarSeguro.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        criarSeguro.addClassName("product-action");
        criarSeguro.setWidthFull();
        return criarSeguro;
    }

    private Div criarPainelProduto(String titulo, String descricao, Button acao) {
        Div painel = new Div();
        painel.addClassName("product-panel");

        H3 heading = new H3(titulo);
        Paragraph texto = new Paragraph(descricao);
        texto.addClassName("muted-text");

        painel.add(heading, texto, acao);
        return painel;
    }

    private void configurarOperacoes() {
        operacoesLayout.setWidthFull();
        operacoesLayout.setPadding(false);
        operacoesLayout.setSpacing(true);
        operacoesLayout.addClassName("panel-section");

        H2 subtitulo = new H2("Operações da conta ativa");
        subtitulo.addClassName("section-title");

        operacaoContaResumo.addClassName("account-spotlight");

        NumberField valorField = new NumberField("Valor");
        valorField.setMin(0.0);
        valorField.setStep(10.0);
        valorField.setWidth("240px");
        valorField.setValue(100.0);

        Button depositar = new Button("Depositar", event -> executarOperacao(valorField, true));
        depositar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        Button sacar = new Button("Sacar", event -> executarOperacao(valorField, false));
        sacar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout botoesLayout = new HorizontalLayout(valorField, depositar, sacar);
        botoesLayout.setAlignItems(Alignment.END);
        botoesLayout.getStyle().set("flex-wrap", "wrap");
        botoesLayout.addClassName("selection-row");

        operacoesLayout.add(subtitulo, operacaoContaResumo, botoesLayout);
    }

    private void configurarHistorico() {
        historicoLayout.setWidthFull();
        historicoLayout.setPadding(false);
        historicoLayout.setSpacing(true);
        historicoLayout.addClassName("panel-section");

        H2 subtitulo = new H2("Linha do tempo financeira");
        subtitulo.addClassName("section-title");

        historicoLista.addClassName("timeline-list");
        historicoVazio.addClassName("muted-text");

        Scroller scroller = new Scroller(historicoLista);
        scroller.setWidthFull();
        scroller.setHeight("420px");
        scroller.addClassName("history-scroller");

        historicoLayout.add(subtitulo, scroller);
    }

    private void executarOperacao(NumberField valorField, boolean deposito) {
        if (contaAtual == null) {
            Notification.show("Crie uma conta antes de operar.", 3000, Notification.Position.MIDDLE);
            return;
        }
        Double valor = valorField.getValue();
        if (valor == null || valor <= 0) {
            Notification.show("Informe um valor válido.", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            if (deposito) {
                contaAtual.depositar(valor);
                Notification.show("Depósito realizado.", 2000, Notification.Position.TOP_CENTER);
            } else {
                contaAtual.sacar(valor);
                Notification.show("Saque realizado.", 2000, Notification.Position.TOP_CENTER);
            }
            atualizarResumoProdutos();
            atualizarOperacaoResumo();
            atualizarHistorico();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void atualizarFactory() {
        factoryAtual = criarFactory(pacoteSelect.getValue());
        List<String> tiposDisponiveis = factoryAtual.getTiposContaDisponiveis();
        contaTipoSelect.setItems(tiposDisponiveis);
        if (contaTipoSelect.getValue() == null || !tiposDisponiveis.contains(contaTipoSelect.getValue())) {
            contaTipoSelect.setValue(tiposDisponiveis.getFirst());
        }
        pacoteResumo.removeAll();
        pacoteResumo.add(
                criarBadge(factoryAtual.getNomePacote()),
                new H3("Regras do pacote"),
                new Paragraph(factoryAtual.getDescricaoPacote()),
                new Paragraph("Contas disponíveis: " + String.join(", ", tiposDisponiveis))
        );
    }

    private ProdutoFinanceiroFactory criarFactory(String pacote) {
        if ("Pessoa Jurídica".equals(pacote)) {
            return new PacotePessoaJuridicaFactory();
        }
        return new PacotePessoaFisicaFactory();
    }

    private void atualizarResumoProdutos() {
        contaResumo.removeAll();
        cartaoResumo.removeAll();
        seguroResumo.removeAll();

        contaResumo.addClassName("summary-card");
        cartaoResumo.addClassName("summary-card");
        seguroResumo.addClassName("summary-card");

        contaResumo.add(new H3("Conta atual"));
        if (contaAtual == null) {
            contaResumo.add(new Paragraph("Nenhuma conta criada."));
        } else {
            contaResumo.add(criarBadge(contaAtual.getTipoConta()), new Paragraph(contaAtual.visualizar()), new Paragraph(descricaoConta(contaAtual)));
        }

        cartaoResumo.add(new H3("Cartão atual"));
        if (cartaoAtual == null) {
            cartaoResumo.add(new Paragraph("Nenhum cartão emitido."));
        } else {
            cartaoResumo.add(criarBadge(cartaoAtual.getBandeira()), new Paragraph(cartaoAtual.toString()),
                    new Paragraph("Limite disponível para o pacote " + factoryAtual.getNomePacote() + "."));
        }

        seguroResumo.add(new H3("Seguro atual"));
        if (seguroAtual == null) {
            seguroResumo.add(new Paragraph("Nenhum seguro contratado."));
        } else {
            seguroResumo.add(criarBadge(seguroAtual.getTipoApolice()), new Paragraph(seguroAtual.toString()),
                    new Paragraph("Cobertura exibida com o prêmio registrado no histórico."));
        }
    }

    private void atualizarOperacaoResumo() {
        operacaoContaResumo.removeAll();
        if (contaAtual == null) {
            operacaoContaResumo.add(
                    new H3("Nenhuma conta ativa"),
                    new Paragraph("Crie uma conta no pacote selecionado para habilitar depósitos e saques.")
            );
            return;
        }

        operacaoContaResumo.add(
                criarBadge(contaAtual.getTipoConta()),
                new H3("Conta pronta para movimentação"),
                new Paragraph(contaAtual.visualizar()),
                new Paragraph(descricaoConta(contaAtual))
        );
    }

    private void atualizarHistorico() {
        historicoLista.removeAll();

        List<Transacao> historico = GerenciadorTransacoes.getInstancia().getHistorico();
        if (historico.isEmpty()) {
            historicoLista.add(historicoVazio);
            return;
        }

        for (int i = historico.size() - 1; i >= 0; i--) {
            historicoLista.add(criarItemHistorico(historico.get(i)));
        }
    }

    private Div criarItemHistorico(Transacao transacao) {
        Div item = new Div();
        item.addClassName("timeline-item");

        Span titulo = new Span(transacao.getTipo());
        titulo.addClassName("timeline-title");

        Span valor = new Span("R$ " + String.format("%.2f", transacao.getValor()));
        valor.addClassName("timeline-value");

        Paragraph descricao = new Paragraph(transacao.getDescricao());
        descricao.addClassName("timeline-description");

        Span horario = new Span(HISTORICO_FORMATTER.format(transacao.getMomento()));
        horario.addClassName("timeline-time");

        HorizontalLayout cabecalho = new HorizontalLayout(titulo, valor);
        cabecalho.setWidthFull();
        cabecalho.setJustifyContentMode(JustifyContentMode.BETWEEN);
        cabecalho.setAlignItems(Alignment.CENTER);

        item.add(cabecalho, descricao, horario);
        return item;
    }

    private Span criarBadge(String texto) {
        Span badge = new Span(texto);
        badge.addClassName("status-badge");
        return badge;
    }

    private void registrarEvento(String tipo, double valor, String descricao) {
        GerenciadorTransacoes.getInstancia().registrar(new Transacao(tipo, valor, descricao));
    }

    private String descricaoConta(Conta conta) {
        if (conta instanceof ContaCorrente) {
            return "Indicada para uso diário, com cheque especial para apoiar saques acima do saldo.";
        }
        if (conta instanceof ContaPoupanca) {
            return "Pensada para reserva financeira, com foco em saldo e rendimento.";
        }
        if (conta instanceof ContaInvestimento) {
            return "Estrutura voltada para ativos e estratégia de alocação.";
        }
        return "Conta pronta para movimentações.";
    }
}
