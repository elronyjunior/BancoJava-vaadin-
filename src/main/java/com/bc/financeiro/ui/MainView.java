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
import com.vaadin.flow.component.dialog.Dialog;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private String contaTitular;
    private String contaObjetivo;
    private String contaCpf;
    private String cartaoNomeImpresso;
    private String cartaoNumero;
    private String cartaoValidade;
    private String seguroNome;
    private String seguroCartaoContemplado;
    private String seguroObservacao;

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
    private final List<CartaoRegistrado> cartoesRegistrados = new ArrayList<>();

    private ProdutoFinanceiroFactory factoryAtual;
    private Select<String> pacoteSelect;
    private Select<String> contaTipoSelect;

    public MainView() {
        factoryAtual = new PacotePessoaFisicaFactory();

        setWidthFull();
        setHeight(null);
        setPadding(true);
        setSpacing(true);
        addClassName("main-view");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        H1 titulo = new H1("Banco Digital com Abstract Factory");
        titulo.addClassName("page-title");

        Paragraph descricao = new Paragraph(
                "Simule a montagem de produtos financeiros por perfil de cliente, combinando conta, cartão e seguro em uma experiência bancária organizada, visual e fácil de testar.");
        descricao.addClassName("page-subtitle");

        Div heroBadge = new Div(new Span("Experiência bancária organizada com Abstract Factory"));
        heroBadge.addClassName("hero-badge");

        Div heroContent = new Div(titulo, descricao);
        heroContent.addClassName("hero-content");

        Div heroStats = new Div(
                criarHeroStat("2", "perfis disponíveis"),
                criarHeroStat("3", "produtos integrados"),
                criarHeroStat("Tempo real", "histórico atualizado")
        );
        heroStats.addClassName("hero-stats");

        Div heroSection = new Div(heroBadge, heroContent, heroStats);
        heroSection.addClassName("hero-section");
        heroSection.setWidthFull();

        Tab tabCriar = new Tab("Produtos");
        Tab tabOperar = new Tab("Operações");
        Tab tabHistorico = new Tab("Histórico");

        Tabs tabs = new Tabs();
        tabs.add(tabCriar, tabOperar, tabHistorico);
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
        content.addClassName("content-shell");

        Div footer = criarRodape();
        footer.setWidthFull();

        add(heroSection, tabs, content, footer);
        atualizarFactory();
        atualizarResumoProdutos();
        atualizarOperacaoResumo();
        atualizarHistorico();
setMargin(true);
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
        criarProdutosLayout.addClassName("tab-panel");

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
        contaTipoSelect.setItemLabelGenerator(this::formatarTipoConta);

        pacoteSelect.addValueChangeListener(event -> atualizarFactory());

        HorizontalLayout seletores = new HorizontalLayout(pacoteSelect, contaTipoSelect);
        seletores.setWidthFull();
        seletores.setPadding(false);
        seletores.setSpacing(false);
        seletores.setMargin(false);
        seletores.getStyle().set("flex-wrap", "wrap");
        seletores.addClassName("selection-row");

        pacoteResumo.addClassName("package-banner");

        Div configuracaoHeader = new Div(subtitulo);
        configuracaoHeader.addClassName("section-header");

        Div configuracaoCard = new Div(seletores, pacoteResumo);
        configuracaoCard.addClassName("config-card");

        Div cardsLayout = new Div(
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
        cardsLayout.addClassName("product-grid");

        Div resumoLayout = new Div(contaResumo, cartaoResumo, seguroResumo);
        resumoLayout.setWidthFull();
        resumoLayout.addClassName("summary-grid");

        criarProdutosLayout.add(configuracaoHeader, configuracaoCard, cardsLayout, resumoLayout);
    }

    private Button criarBotaoConta() {
        Button criarConta = new Button("Criar Conta", event -> abrirDialogConta());
        criarConta.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        criarConta.addClassName("product-action");
        criarConta.setWidthFull();
        return criarConta;
    }

    private Button criarBotaoCartao() {
        Button criarCartao = new Button("Criar Cartão", event -> abrirDialogCartao());
        criarCartao.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        criarCartao.addClassName("product-action");
        criarCartao.setWidthFull();
        return criarCartao;
    }

    private Button criarBotaoSeguro() {
        Button criarSeguro = new Button("Criar Seguro", event -> abrirDialogSeguro());
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
        operacoesLayout.addClassName("tab-panel");

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
        depositar.addClassName("operation-button");
        depositar.addClassName("deposit-button");

        Button sacar = new Button("Sacar", event -> executarOperacao(valorField, false));
        sacar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        sacar.addClassName("operation-button");
        sacar.addClassName("withdraw-button");

        HorizontalLayout botoesLayout = new HorizontalLayout(valorField, depositar, sacar);
        botoesLayout.setPadding(false);
        botoesLayout.setSpacing(false);
        botoesLayout.setMargin(false);
        botoesLayout.setAlignItems(Alignment.END);
        botoesLayout.getStyle().set("flex-wrap", "wrap");
        botoesLayout.addClassName("selection-row");

        Div operacoesCard = new Div(botoesLayout);
        operacoesCard.addClassName("operations-card");

        operacoesLayout.add(subtitulo, operacaoContaResumo, operacoesCard);
    }

    private void configurarHistorico() {
        historicoLayout.setWidthFull();
        historicoLayout.setPadding(false);
        historicoLayout.setSpacing(true);
        historicoLayout.addClassName("panel-section");
        historicoLayout.addClassName("tab-panel");

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
            contaResumo.add(
                    criarBadge(contaAtual.getTipoConta()),
                            criarGrupoInfo("Dados principais",
                            criarInfoItem("Titular", contaTitular),
                            criarInfoItem("CPF", contaCpf),
                            criarInfoItem("Objetivo", contaObjetivo)),
                    criarGrupoInfo("Conta",
                            criarInfoItem("Tipo", contaAtual.getTipoConta()),
                            criarInfoItem("Resumo", resumoConta(contaAtual)),
                            criarInfoItem("Saldo e limites", detalhesConta(contaAtual)),
                            criarInfoItem("Descrição", descricaoConta(contaAtual)))
            );
        }

        cartaoResumo.add(new H3("Cartão atual"));
        if (cartaoAtual == null) {
            cartaoResumo.add(new Paragraph("Nenhum cartão emitido."));
        } else {
            cartaoResumo.add(
                    criarBadge(cartaoAtual.getBandeira()),
                    criarGrupoInfo("Dados do cartão",
                            criarInfoItem("Nome impresso", cartaoNomeImpresso),
                            criarInfoItem("Número", cartaoNumero),
                            criarInfoItem("Validade", cartaoValidade)),
                    criarGrupoInfo("Condições",
                            criarInfoItem("Bandeira", cartaoAtual.getBandeira()),
                            criarInfoItem("Limite", formatarMoeda(cartaoAtual.getLimiteCredito())),
                            criarInfoItem("Pacote", factoryAtual.getNomePacote()),
                            criarInfoItem("Observação", "Limite disponível conforme o perfil selecionado."))
            );
        }

        seguroResumo.add(new H3("Seguro atual"));
        if (seguroAtual == null) {
            seguroResumo.add(new Paragraph("Nenhum seguro contratado."));
        } else {
            seguroResumo.add(
                    criarBadge(seguroAtual.getTipoApolice()),
                    criarGrupoInfo("Dados do seguro",
                            criarInfoItem("Nome", seguroNome),
                            criarInfoItem("Cartão contemplado", seguroCartaoContemplado),
                            criarInfoItem("Observação", seguroObservacao)),
                    criarGrupoInfo("Cobertura",
                            criarInfoItem("Tipo", seguroAtual.getTipoApolice()),
                            criarInfoItem("Prêmio", formatarMoeda(seguroAtual.getValorPremio())),
                            criarInfoItem("Cobertura", formatarMoeda(seguroAtual.getValorCobertura())),
                            criarInfoItem("Histórico", "Prêmio e contratação registrados na linha do tempo."))
            );
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
                criarGrupoInfo("Situação atual",
                        criarInfoItem("Titular", contaTitular),
                        criarInfoItem("Resumo", resumoConta(contaAtual)),
                        criarInfoItem("Saldo e limites", detalhesConta(contaAtual)),
                        criarInfoItem("Perfil", descricaoConta(contaAtual)))
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

    private Div criarGrupoInfo(String titulo, Div... itens) {
        Div grupo = new Div();
        grupo.addClassName("info-group");
        Span tituloGrupo = new Span(titulo);
        tituloGrupo.addClassName("info-group-title");
        grupo.add(tituloGrupo);
        for (Div item : itens) {
            grupo.add(item);
        }
        return grupo;
    }

    private Div criarInfoItem(String label, String valor) {
        Div item = new Div();
        item.addClassName("info-item");
        Span labelSpan = new Span(label);
        labelSpan.addClassName("info-label");
        Span valueSpan = new Span(valor == null || valor.isBlank() ? "Nao informado" : valor);
        valueSpan.addClassName("info-value");
        item.add(labelSpan, valueSpan);
        return item;
    }

    private void abrirDialogConta() {
        Dialog dialog = criarDialogPadrao("Criar conta", "Preencha os dados antes de gerar a conta.");

        TextField titularField = new TextField("Titular");
        titularField.setRequiredIndicatorVisible(true);
        titularField.setWidthFull();
        titularField.setValue(contaTitular == null ? "" : contaTitular);

        TextField cpfField = new TextField("CPF");
        cpfField.setRequiredIndicatorVisible(true);
        cpfField.setWidthFull();
        cpfField.setMaxLength(14);
        cpfField.setPlaceholder("000.000.000-00");
        cpfField.setValueChangeMode(ValueChangeMode.EAGER);
        cpfField.setValue(contaCpf == null ? "" : contaCpf);
        cpfField.addValueChangeListener(event -> {
            String formatado = formatarCpf(event.getValue());
            if (!formatado.equals(event.getValue())) {
                cpfField.setValue(formatado);
            }
        });

        TextArea objetivoField = new TextArea("(Opcional) Objetivo da conta");
        objetivoField.setWidthFull();
        objetivoField.setMaxLength(180);
        objetivoField.setValue(contaObjetivo == null ? "" : contaObjetivo);

        Button cancelar = new Button("Cancelar", event -> dialog.close());
        Button confirmar = new Button("Confirmar criação", event -> {
            if (titularField.isEmpty() || contarDigitos(cpfField.getValue()) != 11) {
                Notification.show("Preencha nome e CPF válido antes de criar a conta.", 3000, Notification.Position.MIDDLE);
                return;
            }
            try {
                contaAtual = factoryAtual.criarConta(contaTipoSelect.getValue());
                contaTitular = titularField.getValue();
                contaCpf = cpfField.getValue();
                contaObjetivo = objetivoField.getValue();
                registrarEvento("Criação de conta", 0.0,
                        contaAtual.getTipoConta() + " criada para " + contaTitular + " no pacote " + factoryAtual.getNomePacote());
                atualizarResumoProdutos();
                atualizarOperacaoResumo();
                atualizarHistorico();
                dialog.close();
                Notification.show("Conta criada com sucesso.", 2000, Notification.Position.TOP_CENTER);
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        confirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(criarDialogFormulario(titularField, cpfField, objetivoField, cancelar, confirmar));
        dialog.open();
    }

    private void abrirDialogCartao() {
        Dialog dialog = criarDialogPadrao("Criar cartão", "Informe os dados do cartão antes da emissão.");
        Cartao cartaoPreview = factoryAtual.criarCartao();

        TextField nomeImpressoField = new TextField("Nome impresso");
        nomeImpressoField.setRequiredIndicatorVisible(true);
        nomeImpressoField.setWidthFull();
        nomeImpressoField.setValue(cartaoNomeImpresso == null ? valorPadraoNomePessoa() : cartaoNomeImpresso);

        TextField numeroField = new TextField("Número do cartão");
        numeroField.setWidthFull();
        numeroField.setReadOnly(true);
        numeroField.setValue(cartaoNumero == null ? cartaoPreview.getNumero() : cartaoNumero);

        TextField validadeField = new TextField("Validade automática");
        validadeField.setWidthFull();
        validadeField.setReadOnly(true);
        validadeField.setValue(cartaoValidade == null ? cartaoPreview.getValidade() : cartaoValidade);

        Button cancelar = new Button("Cancelar", event -> dialog.close());
        Button confirmar = new Button("Confirmar criação", event -> {
            if (nomeImpressoField.isEmpty()) {
                Notification.show("Preencha o nome impresso antes de criar o cartão.", 3000, Notification.Position.MIDDLE);
                return;
            }
            cartaoAtual = cartaoPreview;
            cartaoNomeImpresso = nomeImpressoField.getValue();
            cartaoNumero = numeroField.getValue();
            cartaoValidade = validadeField.getValue();
            cartoesRegistrados.add(new CartaoRegistrado(cartaoNomeImpresso, cartaoNumero, cartaoValidade, cartaoAtual.getBandeira()));
            registrarEvento("Criação de cartão", cartaoAtual.getLimiteCredito(),
                    "Cartão " + cartaoAtual.getBandeira() + " emitido para " + cartaoNomeImpresso + " no pacote " + factoryAtual.getNomePacote());
            atualizarResumoProdutos();
            atualizarHistorico();
            dialog.close();
            Notification.show("Cartão criado com sucesso.", 2000, Notification.Position.TOP_CENTER);
        });
        confirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(criarDialogFormulario(nomeImpressoField, numeroField, validadeField, cancelar, confirmar));
        dialog.open();
    }

    private void abrirDialogSeguro() {
        Dialog dialog = criarDialogPadrao("Criar seguro", "Colete as informações do seguro antes da contratação.");

        TextField nomeField = new TextField("Nome");
        nomeField.setRequiredIndicatorVisible(true);
        nomeField.setWidthFull();
        nomeField.setValueChangeMode(ValueChangeMode.EAGER);
        nomeField.setValue(seguroNome == null ? valorPadraoNomePessoa() : seguroNome);

        Select<String> cartaoContempladoSelect = new Select<>();
        cartaoContempladoSelect.setLabel("Cartão contemplado pelo seguro");
        cartaoContempladoSelect.setRequiredIndicatorVisible(true);
        cartaoContempladoSelect.setWidthFull();
        cartaoContempladoSelect.setPlaceholder("Selecione um cartão da pessoa");
        atualizarCartoesSeguro(cartaoContempladoSelect, nomeField.getValue(), seguroCartaoContemplado);
        nomeField.addValueChangeListener(event ->
                atualizarCartoesSeguro(cartaoContempladoSelect, event.getValue(), cartaoContempladoSelect.getValue()));

        TextArea observacaoField = new TextArea("(Opcional) Observações");
        observacaoField.setWidthFull();
        observacaoField.setMaxLength(180);
        observacaoField.setValue(seguroObservacao == null ? "" : seguroObservacao);

        Button cancelar = new Button("Cancelar", event -> dialog.close());
        Button confirmar = new Button("Confirmar criação", event -> {
            if (nomeField.isEmpty() || cartaoContempladoSelect.isEmpty()) {
                Notification.show("Preencha os campos obrigatórios antes de criar o seguro.", 3000, Notification.Position.MIDDLE);
                return;
            }
            seguroAtual = factoryAtual.criarSeguro();
            seguroNome = nomeField.getValue();
            seguroCartaoContemplado = cartaoContempladoSelect.getValue();
            seguroObservacao = observacaoField.getValue();
            registrarEvento("Criação de seguro", seguroAtual.getValorPremio(),
                    "Seguro " + seguroAtual.getTipoApolice() + " criado para " + seguroNome + " no pacote " + factoryAtual.getNomePacote());
            atualizarResumoProdutos();
            atualizarHistorico();
            dialog.close();
            Notification.show("Seguro criado com sucesso.", 2000, Notification.Position.TOP_CENTER);
        });
        confirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(criarDialogFormulario(nomeField, cartaoContempladoSelect, observacaoField, cancelar, confirmar));
        dialog.open();
    }

    private void atualizarCartoesSeguro(Select<String> select, String nomePessoa, String valorAtual) {
        String nomeBase = nomePessoa == null || nomePessoa.isBlank() ? valorPadraoNomePessoa() : nomePessoa;

        List<String> cartoesDaPessoa = cartoesRegistrados.stream()
                .filter(cartao -> cartaoPertenceAoNome(cartao, nomeBase))
                .map(CartaoRegistrado::descricao)
                .toList();

        if (cartoesDaPessoa.isEmpty()) {
            cartoesDaPessoa = cartoesRegistrados.stream()
                    .map(CartaoRegistrado::descricao)
                    .toList();
        }

        select.setItems(cartoesDaPessoa);
        if (valorAtual != null && cartoesDaPessoa.contains(valorAtual)) {
            select.setValue(valorAtual);
            return;
        }
        if (cartoesDaPessoa.isEmpty()) {
            select.clear();
            return;
        }
        select.setValue(cartoesDaPessoa.getFirst());
    }

    private boolean cartaoPertenceAoNome(CartaoRegistrado cartao, String nomePessoa) {
        if (nomePessoa == null || nomePessoa.isBlank()) {
            return false;
        }
        return cartao.nomeTitular().trim().equalsIgnoreCase(nomePessoa.trim());
    }

    private String valorPadraoNomePessoa() {
        return contaTitular == null ? "" : contaTitular;
    }

    private String formatarCpf(String valor) {
        String digitos = valor == null ? "" : valor.replaceAll("\\D", "");
        if (digitos.length() > 11) {
            digitos = digitos.substring(0, 11);
        }

        StringBuilder cpf = new StringBuilder();
        for (int i = 0; i < digitos.length(); i++) {
            if (i == 3 || i == 6) {
                cpf.append('.');
            } else if (i == 9) {
                cpf.append('-');
            }
            cpf.append(digitos.charAt(i));
        }
        return cpf.toString();
    }

    private int contarDigitos(String valor) {
        return valor == null ? 0 : valor.replaceAll("\\D", "").length();
    }

    private String formatarTipoConta(String tipoConta) {
        if (tipoConta == null || tipoConta.isBlank()) {
            return "";
        }
        return tipoConta.substring(0, 1).toUpperCase() + tipoConta.substring(1);
    }

    private String resumoConta(Conta conta) {
        String visualizacao = conta.visualizar();
        String[] partes = visualizacao.split(" - ");
        return partes.length > 0 ? partes[0] : visualizacao;
    }

    private String detalhesConta(Conta conta) {
        String visualizacao = conta.visualizar();
        String[] partes = visualizacao.split(" - ");
        if (partes.length <= 1) {
            return visualizacao;
        }
        return String.join(" | ", java.util.Arrays.copyOfRange(partes, 1, partes.length));
    }

    private String formatarMoeda(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }

    private record CartaoRegistrado(String nomeTitular, String numero, String validade, String bandeira) {
        private String descricao() {
            return bandeira + " - " + numero + " - validade " + validade;
        }
    }

    private Dialog criarDialogPadrao(String titulo, String descricao) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(titulo);
        dialog.setModal(true);
        dialog.setDraggable(false);
        dialog.setResizable(false);
        dialog.addClassName("entity-dialog");
        dialog.getElement().setAttribute("aria-label", titulo);
        dialog.add(new Paragraph(descricao));
        return dialog;
    }

    private VerticalLayout criarDialogFormulario(
            com.vaadin.flow.component.Component campoUm,
            com.vaadin.flow.component.Component campoDois,
            com.vaadin.flow.component.Component campoTres,
            Button cancelar,
            Button confirmar) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setWidthFull();
        layout.addClassName("dialog-form");

        HorizontalLayout footer = new HorizontalLayout(cancelar, confirmar);
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.END);
        footer.addClassName("dialog-actions");

        layout.add(campoUm, campoDois, campoTres, footer);
        return layout;
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
