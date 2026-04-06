package com.bc.financeiro.ui;

import com.bc.financeiro.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.card.Card;

@Route("")
public class MainView extends VerticalLayout {

    private Conta contaAtual;
    private Cartao cartaoAtual;
    private Seguro seguroAtual;
    private final TextArea resultadoArea = new TextArea("Informações do Produto");
    private final TextArea historicoArea = new TextArea("Histórico de Transações");

    private final VerticalLayout criarProdutosLayout = new VerticalLayout();
    private final VerticalLayout operacoesLayout = new VerticalLayout();
    private final VerticalLayout historicoLayout = new VerticalLayout();

    public MainView() {
        setWidthFull();
        setPadding(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 titulo = new H1("Banco Digital - Gerenciamento Financeiro");
        titulo.getStyle().set("color", "#2E8B57").set("text-align", "center");

        // Tabs para navegação
        Tab tabCriar = new Tab("Criar Produtos");
        Tab tabOperar = new Tab("Operações");
        Tab tabHistorico = new Tab("Histórico");

        Tabs tabs = new Tabs(tabCriar, tabOperar, tabHistorico);
        tabs.setWidthFull();

        // Layout para Criar Produtos
        configurarCriarProdutos();

        // Layout para Operações
        configurarOperacoes();

        // Layout para Histórico
        configurarHistorico();

        // Container para conteúdo das tabs
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

        add(titulo, tabs, content);
    }

    private void configurarCriarProdutos() {
        criarProdutosLayout.setWidthFull();
        criarProdutosLayout.setPadding(true);
        criarProdutosLayout.setSpacing(true);

        H2 subtitulo = new H2("Selecione o Pacote e Crie Seus Produtos");
        criarProdutosLayout.add(subtitulo);

        Select<String> pacoteSelect = new Select<>();
        pacoteSelect.setLabel("Pacote");
        pacoteSelect.setItems("Pessoa Física", "Pessoa Jurídica");
        pacoteSelect.setValue("Pessoa Física");
        pacoteSelect.setWidth("300px");

        HorizontalLayout cardsLayout = new HorizontalLayout();
        cardsLayout.setWidthFull();
        cardsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        cardsLayout.setSpacing(true);

        // Card para Conta
        Card contaCard = new Card();
        contaCard.setWidth("300px");
        contaCard.setHeight("200px");
        VerticalLayout contaContent = new VerticalLayout();
        contaContent.add(new H2("Conta"));
        ComboBox<String> contaTipo = new ComboBox<>("Tipo");
        contaTipo.setItems("corrente", "poupanca", "investimento");
        contaTipo.setValue("corrente");
        Button criarConta = new Button("Criar Conta", event -> {
            ProdutoFinanceiroFactory factory = criarFactory(pacoteSelect.getValue());
            contaAtual = factory.criarConta();
            resultadoArea.setValue("Conta criada: " + contaAtual.visualizar());
            Notification.show("Conta criada com sucesso!", 2000, Notification.Position.TOP_CENTER);
        });
        criarConta.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        contaContent.add(contaTipo, criarConta);
        contaCard.add(contaContent);
        cardsLayout.add(contaCard);

        // Card para Cartão
        Card cartaoCard = new Card();
        cartaoCard.setWidth("300px");
        cartaoCard.setHeight("200px");
        VerticalLayout cartaoContent = new VerticalLayout();
        cartaoContent.add(new H2("Cartão"));
        Button criarCartao = new Button("Criar Cartão", event -> {
            ProdutoFinanceiroFactory factory = criarFactory(pacoteSelect.getValue());
            cartaoAtual = factory.criarCartao();
            resultadoArea.setValue("Cartão criado: " + cartaoAtual.toString());
            Notification.show("Cartão criado com sucesso!", 2000, Notification.Position.TOP_CENTER);
        });
        criarCartao.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cartaoContent.add(criarCartao);
        cartaoCard.add(cartaoContent);
        cardsLayout.add(cartaoCard);

        // Card para Seguro
        Card seguroCard = new Card();
        seguroCard.setWidth("300px");
        seguroCard.setHeight("200px");
        VerticalLayout seguroContent = new VerticalLayout();
        seguroContent.add(new H2("Seguro"));
        Button criarSeguro = new Button("Criar Seguro", event -> {
            ProdutoFinanceiroFactory factory = criarFactory(pacoteSelect.getValue());
            seguroAtual = factory.criarSeguro();
            resultadoArea.setValue("Seguro criado: " + seguroAtual.toString());
            Notification.show("Seguro criado com sucesso!", 2000, Notification.Position.TOP_CENTER);
        });
        criarSeguro.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        seguroContent.add(criarSeguro);
        seguroCard.add(seguroContent);
        cardsLayout.add(seguroCard);

        resultadoArea.setWidthFull();
        resultadoArea.setHeight("150px");
        resultadoArea.setReadOnly(true);

        criarProdutosLayout.add(pacoteSelect, cardsLayout, resultadoArea);
    }

    private void configurarOperacoes() {
        operacoesLayout.setWidthFull();
        operacoesLayout.setPadding(true);
        operacoesLayout.setSpacing(true);

        H2 subtitulo = new H2("Operações na Conta");
        operacoesLayout.add(subtitulo);

        NumberField valorField = new NumberField("Valor");
        valorField.setMin(0);
        valorField.setPrefixComponent(null);
        valorField.setValue(0.0);
        valorField.setWidth("200px");

        HorizontalLayout botoesLayout = new HorizontalLayout();
        botoesLayout.setSpacing(true);

        Button depositar = new Button("Depositar", event -> {
            if (contaAtual == null) {
                Notification.show("Crie uma conta primeiro.", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (valorField.getValue() == null || valorField.getValue() <= 0) {
                Notification.show("Informe um valor válido.", 3000, Notification.Position.MIDDLE);
                return;
            }
            contaAtual.depositar(valorField.getValue());
            resultadoArea.setValue(contaAtual.visualizar());
            atualizarHistorico();
            Notification.show("Depósito realizado!", 2000, Notification.Position.TOP_CENTER);
        });
        depositar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        Button sacar = new Button("Sacar", event -> {
            if (contaAtual == null) {
                Notification.show("Crie uma conta primeiro.", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (valorField.getValue() == null || valorField.getValue() <= 0) {
                Notification.show("Informe um valor válido.", 3000, Notification.Position.MIDDLE);
                return;
            }
            try {
                contaAtual.sacar(valorField.getValue());
                resultadoArea.setValue(contaAtual.visualizar());
                atualizarHistorico();
                Notification.show("Saque realizado!", 2000, Notification.Position.TOP_CENTER);
            } catch (IllegalStateException e) {
                Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        sacar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        botoesLayout.add(depositar, sacar);

        operacoesLayout.add(valorField, botoesLayout, resultadoArea);
    }

    private void configurarHistorico() {
        historicoLayout.setWidthFull();
        historicoLayout.setPadding(true);
        historicoLayout.setSpacing(true);

        H2 subtitulo = new H2("Histórico de Transações");
        historicoLayout.add(subtitulo);

        historicoArea.setWidthFull();
        historicoArea.setHeight("300px");
        historicoArea.setReadOnly(true);
        atualizarHistorico();

        historicoLayout.add(historicoArea);
    }

    private ProdutoFinanceiroFactory criarFactory(String pacote) {
        if ("Pessoa Jurídica".equals(pacote)) {
            return new PacotePessoaJuridicaFactory();
        }
        return new PacotePessoaFisicaFactory();
    }

    private void atualizarHistorico() {
        StringBuilder builder = new StringBuilder();
        for (Transacao transacao : GerenciadorTransacoes.getInstancia().getHistorico()) {
            builder.append(transacao.toString()).append("\n");
        }
        historicoArea.setValue(builder.toString());
    }
}
