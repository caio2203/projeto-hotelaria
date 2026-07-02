import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.*;

/**
 * Tela principal do sistema (JavaFX). É por aqui que a aplicação sobe.
 *
 * Pessoal: quando o app abre ele tenta ler o arquivo salvo; se não existir
 * (primeira vez), o semearDadosIniciais() cria só o hotel vazio - o resto
 * o usuário cadastra pela interface.
 * Quando fecha a janela, o stop() salva tudo de novo. Tá tudo em java.util
 * (ArrayList/Stack/Queue), nada de estrutura feita na mão aqui.
 *
 * Se forem mexer no visual, cada aba está montada num método separado
 * (abaQuartos/abaHospedes/abaReservas) pra não dar conflito.
 *
 * @version 2.0
 */
public class HotelApp extends Application {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private SistemaCentral sistema;
    private Hotel hotel;

    private final TableView<Quarto> tabelaQuartos = new TableView<>();
    private final TableView<Hospede> tabelaHospedes = new TableView<>();
    private final TableView<Reserva> tabelaReservas = new TableView<>();
    private final TableView<Reserva> tabelaHistorico = new TableView<>();

    private final ComboBox<Hospede> comboHospede = new ComboBox<>();
    private final ComboBox<Quarto> comboQuarto = new ComboBox<>();
    private final Label labelFila = new Label();
    private final Label labelOcupacao = new Label();

    @Override
    public void start(Stage stage) {
        sistema = SistemaCentral.getInstancia();
        // tenta recuperar o que foi salvo na última vez; se não tiver nada, popula o exemplo
        if (!Persistencia.carregar(sistema) || sistema.getHoteis().isEmpty()) {
            semearDadosIniciais();
        }
        // por enquanto o sistema trabalha com um hotel só, então pego o primeiro
        hotel = sistema.getHoteis().get(0);

        TabPane abas = new TabPane();
        abas.getTabs().addAll(abaQuartos(), abaHospedes(), abaReservas());
        abas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        atualizarTudo();

        Scene scene = new Scene(abas, 1000, 700);
        stage.setTitle("Sistema de Reservas — " + hotel.getNome());
        stage.setScene(scene);
        stage.show();
    }

    // o próprio JavaFX chama esse método quando a janela fecha.
    // aproveito pra salvar tudo, senão a gente perde os dados ao sair.
    @Override
    public void stop() {
        Persistencia.salvar(sistema);
    }

    // -------------------------------------------------------------------------
    // Aba: Quartos
    // -------------------------------------------------------------------------

    private Tab abaQuartos() {
        coluna(tabelaQuartos, "Número", q -> String.valueOf(q.getNumero()));
        coluna(tabelaQuartos, "Tipo", q -> q.getTipo().toString());
        coluna(tabelaQuartos, "Capacidade", q -> String.valueOf(q.getCapacidadeMaxima()));
        coluna(tabelaQuartos, "Status", q -> q.getStatus().toString());

        TextField campoNumero = new TextField();
        campoNumero.setPromptText("Número");
        ComboBox<TipoQuarto> campoTipo = new ComboBox<>(FXCollections.observableArrayList(TipoQuarto.values()));
        campoTipo.setPromptText("Tipo");
        TextField campoCap = new TextField();
        campoCap.setPromptText("Capacidade");

        Button adicionar = new Button("Adicionar quarto");
        adicionar.setOnAction(e -> {
            try {
                int numero = Integer.parseInt(campoNumero.getText().trim());
                int cap = Integer.parseInt(campoCap.getText().trim());
                TipoQuarto tipo = campoTipo.getValue();
                if (tipo == null) {
                    erro("Selecione o tipo do quarto.");
                    return;
                }
                if (cap <= 0) {
                    erro("A capacidade deve ser maior que zero.");
                    return;
                }
                if (hotel.getQuartos().stream().anyMatch(q -> q.getNumero() == numero)) {
                    erro("Já existe um quarto com o número " + numero + ".");
                    return;
                }
                hotel.adicionarQuarto(new Quarto(hotel.getQuartos().size() + 1, numero, tipo, cap));
                campoNumero.clear();
                campoCap.clear();
                campoTipo.setValue(null);
                atualizarTudo();
            } catch (NumberFormatException ex) {
                erro("Número e capacidade devem ser inteiros.");
            }
        });

        Button bloquear = new Button("Bloquear (manutenção)");
        bloquear.setOnAction(e -> {
            Quarto q = tabelaQuartos.getSelectionModel().getSelectedItem();
            if (q == null) {
                erro("Selecione um quarto.");
                return;
            }
            q.bloquear();
            atualizarTudo();
        });

        Button liberar = new Button("Liberar");
        liberar.setOnAction(e -> {
            Quarto q = tabelaQuartos.getSelectionModel().getSelectedItem();
            if (q == null) {
                erro("Selecione um quarto.");
                return;
            }
            q.desbloquear();
            atualizarTudo();
        });

        HBox form = new HBox(8, campoNumero, campoTipo, campoCap, adicionar);
        HBox acoes = new HBox(8, bloquear, liberar);
        VBox box = new VBox(10, tabelaQuartos, form, acoes);
        box.setPadding(new Insets(12));
        return new Tab("Quartos", box);
    }

    // -------------------------------------------------------------------------
    // Aba: Hóspedes
    // -------------------------------------------------------------------------

    private Tab abaHospedes() {
        // Configuração da Tabela Principal de Hóspedes
        coluna(tabelaHospedes, "Nome", Hospede::getNome); //
        coluna(tabelaHospedes, "CPF", Hospede::getCpf); //
        coluna(tabelaHospedes, "Contato", Hospede::getContato); //
        tabelaHospedes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //

        // Configuração da Nova Tabela de Histórico de Hospedagens
        coluna(tabelaHistorico, "Reserva #", r -> String.valueOf(r.getId())); //[cite: 13]
        coluna(tabelaHistorico, "Quarto", r -> String.valueOf(r.getQuarto().getNumero())); //[cite: 13]
        coluna(tabelaHistorico, "Período", r -> r.getDataEntrada().format(FMT) + " → " + r.getDataSaida().format(FMT)); //[cite: 13]
        coluna(tabelaHistorico, "Status Final", r -> r.getStatus().toString()); //[cite: 13]

        // Evento: Quando clicar em um hóspede, carrega o histórico dele na tabela de baixo
        tabelaHospedes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novoHospede) -> {
            if (novoHospede != null) {
                // Criamos uma lista a partir da Stack do hóspede para exibir na tabela
                tabelaHistorico.setItems(FXCollections.observableArrayList(novoHospede.getHistorico()));
            } else {
                tabelaHistorico.getItems().clear();
            }
        });

        // Formulário de Cadastro Existente
        TextField campoNome = new TextField(); //[cite: 13]
        campoNome.setPromptText("Nome"); //[cite: 13]
        TextField campoCpf = new TextField(); //[cite: 13]
        campoCpf.setPromptText("CPF"); //[cite: 13]
        TextField campoContato = new TextField(); //[cite: 13]
        campoContato.setPromptText("Contato"); //[cite: 13]

        Button adicionar = new Button("Cadastrar hóspede"); //[cite: 13]
        adicionar.setOnAction(e -> { //[cite: 13]
            String nome = campoNome.getText().trim(); //[cite: 13]
            String cpf = campoCpf.getText().trim(); //[cite: 13]
            if (nome.isEmpty() || cpf.isEmpty()) { //[cite: 13]
                erro("Informe ao menos nome e CPF."); //[cite: 13]
                return; //[cite: 13]
            }

            boolean cpfJaExiste = hotel.getHospedes().stream()
                    .anyMatch(h -> h.getCpf().equalsIgnoreCase(cpf));

            if (cpfJaExiste) {
                erro("Já existe um hóspede cadastrado com o CPF: " + cpf);
                return;
            }

            // login/senha a gente gera automático por enquanto - quando tiver
            // tela de login (parte do Luis/Andrei) dá pra pedir isso direito
            int id = hotel.getHospedes().size() + 1; //[cite: 13]
            hotel.adicionarHospede(new Hospede(id, nome, "hospede" + id, "123", //[cite: 13]
                    cpf, campoContato.getText().trim())); //[cite: 13]
            campoNome.clear(); //[cite: 13]
            campoCpf.clear(); //[cite: 13]
            campoContato.clear(); //[cite: 13]
            atualizarTudo(); //[cite: 13]
        });

        HBox form = new HBox(8, campoNome, campoCpf, campoContato, adicionar); //[cite: 13]

        // Títulos visuais para organizar a tela
        Label labelLista = new Label("Lista de Hóspedes Cadastrados");
        labelLista.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label labelHistorico = new Label("Histórico de Estadias (Selecionar um hóspede acima)");
        labelHistorico.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #555;");

        // Adiciona todos os elementos verticalmente no painel
        VBox box = new VBox(10, labelLista, tabelaHospedes, form, labelHistorico, tabelaHistorico); //[cite: 13]
        box.setPadding(new Insets(12)); //[cite: 13]

        return new Tab("Hóspedes", box); //[cite: 13]
    }
    private Tab abaReservas() {
        coluna(tabelaReservas, "#", r -> String.valueOf(r.getId()));
        coluna(tabelaReservas, "Hóspede", r -> r.getHospede().getNome());
        coluna(tabelaReservas, "Quarto", r -> String.valueOf(r.getQuarto().getNumero()));
        coluna(tabelaReservas, "Entrada", r -> r.getDataEntrada().format(FMT));
        coluna(tabelaReservas, "Saída", r -> r.getDataSaida().format(FMT));
        coluna(tabelaReservas, "Status", r -> r.getStatus().toString());
        tabelaReservas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        DatePicker entrada = new DatePicker(LocalDate.now());
        entrada.setPrefWidth(160);
        DatePicker saida = new DatePicker(LocalDate.now().plusDays(1));
        saida.setPrefWidth(160);

        Button criar = new Button("Criar reserva");
        criar.setPrefWidth(120);
        criar.setOnAction(e -> {
            Hospede h = comboHospede.getValue();
            Quarto q = comboQuarto.getValue();
            if (h == null || q == null || entrada.getValue() == null || saida.getValue() == null) {
                erro("Escolha hóspede, quarto e as datas.");
                return;
            }
            if (!saida.getValue().isAfter(entrada.getValue())) {
                erro("A saída deve ser depois da entrada.");
                return;
            }
            // se o quarto estiver ocupado o próprio criarReserva joga o hóspede
            // na fila de espera e devolve null - por isso não guardo o retorno aqui
            hotel.criarReserva(h, q, entrada.getValue(), saida.getValue());
            atualizarTudo();
        });

        Button checkin = new Button("Check-in");
        checkin.setOnAction(e -> comReservaSelecionada(r -> hotel.realizarCheckIn(r)));

        Button checkout = new Button("Check-out");
        checkout.setOnAction(e -> comReservaSelecionada(r -> hotel.realizarCheckOut(r)));

        Button cancelar = new Button("Cancelar");
        cancelar.setOnAction(e -> comReservaSelecionada(r -> {
            // Guarda o status antes para saber se mudou
            StatusReserva statusAntigo = r.getStatus();

            // Chama o método do hotel que gere o cancelamento e a fila
            hotel.cancelarReserva(r);

            // Verifica se o cancelamento foi bem-sucedido
            if (r.getStatus() == StatusReserva.CANCELADA && statusAntigo != StatusReserva.CANCELADA) {
                new Alert(Alert.AlertType.INFORMATION, "Reserva #" + r.getId() + " cancelada com sucesso!").showAndWait();
            } else {
                erro("Não foi possível cancelar: o prazo de 24h expirou ou a reserva já não está pendente.");
            }
        }));

        HBox form = new HBox(8, comboHospede, comboQuarto);
        HBox acoes = new HBox(8, checkin, checkout, cancelar, criar , entrada, saida);
        labelFila.setStyle("-fx-font-weight: bold;");
        labelOcupacao.setStyle("-fx-font-weight: bold;");
        VBox box = new VBox(10, tabelaReservas, form, acoes, labelFila, labelOcupacao);
        box.setPadding(new Insets(12));

        BorderPane root = new BorderPane(box);
        return new Tab("Reservas", root);
    }

    private void comReservaSelecionada(java.util.function.Consumer<Reserva> acao) {
        Reserva r = tabelaReservas.getSelectionModel().getSelectedItem();
        if (r == null) {
            erro("Selecione uma reserva.");
            return;
        }
        acao.accept(r);
        atualizarTudo();
        // reseleciona a mesma reserva pra linha continuar visível já com o status novo
        tabelaReservas.getSelectionModel().select(r);
    }

    // -------------------------------------------------------------------------
    // Apoio
    // -------------------------------------------------------------------------

    // atalho pra montar coluna de texto sem repetir o mesmo código em toda tabela.
    // passa o título e uma função que pega o texto do objeto (ex: q -> q.getNome())
    private <T> void coluna(TableView<T> tabela, String titulo,
                            java.util.function.Function<T, String> extrator) {
        TableColumn<T, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(cd -> new SimpleStringProperty(extrator.apply(cd.getValue())));
        tabela.getColumns().add(col);
    }

    // sempre que mudar alguma coisa no modelo chama esse aqui pra redesenhar
    // as tabelas e os combos. é mais simples do que ficar atualizando item por item.
    private void atualizarTudo() {
        tabelaQuartos.setItems(FXCollections.observableArrayList(hotel.getQuartos()));
        tabelaHospedes.setItems(FXCollections.observableArrayList(hotel.getHospedes()));
        tabelaReservas.setItems(FXCollections.observableArrayList(hotel.getReservas()));
        comboHospede.setItems(FXCollections.observableArrayList(hotel.getHospedes()));
        comboQuarto.setItems(FXCollections.observableArrayList(hotel.getQuartos()));
        // força o redesenho das células na hora (sem depender de troca de foco),
        // pra reserva e quarto refletirem o status novo logo após o check-in
        tabelaReservas.refresh();
        tabelaQuartos.refresh();
        labelFila.setText("Fila de espera: " + hotel.getFilaEspera().size() + " hóspede(s)");
        labelOcupacao.setText(String.format("Taxa de ocupação atual: %.2f%%", hotel.getTaxaOcupacao()));

        tabelaHistorico.getItems().clear(); // Limpa o histórico visual para evitar dados fantasmas ao recarregar
    }

    private void erro(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    // só roda na primeiríssima vez, quando ainda não existe arquivo salvo.
    // cria apenas o hotel vazio: quartos, hóspedes e reservas ficam por conta
    // do usuário, direto pela interface.
    private void semearDadosIniciais() {
        sistema.adicionarHotel(new Hotel(1, "Grand Palace", "Vitória da Conquista - BA"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
