package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * O coração do sistema. Cada hotel cuida do próprio estoque de quartos, das
 * reservas e da fila de espera dele - um hotel não enxerga o do outro.
 *
 * Sobre as coleções (tudo java.util):
 * - ArrayList pra quartos, reservas e hóspedes, porque a gente percorre muito;
 * - Queue (LinkedList) pra fila de espera, que é FIFO mesmo: quem pediu primeiro
 *   é o primeiro a ser chamado quando libera um quarto.
 *
 * @version 1.0
 */
public class Hotel implements Serializable {

    private int id;
    private String nome;
    private String localizacao;
    private ArrayList<Quarto> quartos;
    private ArrayList<Reserva> reservas;
    private ArrayList<Hospede> hospedes;
    private Queue<Hospede> filaEspera;
    private Administrador gerente;
    private ArrayList<Administrador> recepcionistas;

    // contador interno para gerar IDs únicos de reserva
    private int contadorReservas = 1;

    /**
     * Cria o hotel com as listas e fila já inicializadas e vazias.
     *
     * @param id         identificador único
     * @param nome       nome do hotel
     * @param localizacao cidade ou endereço
     */
    public Hotel(int id, String nome, String localizacao) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.quartos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.hospedes = new ArrayList<>();
        this.filaEspera = new LinkedList<>();
        this.recepcionistas = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Gerenciamento de quartos (relacionamento 1:N com Hotel)
    // -------------------------------------------------------------------------

    /**
     * Adiciona um quarto ao inventário do hotel.
     *
     * @param quarto o quarto a ser cadastrado
     */
    public void adicionarQuarto(Quarto quarto) {
        quartos.add(quarto);
        System.out.println("Quarto " + quarto.getNumero() + " adicionado ao hotel " + nome);
    }

    /**
     * Cadastra um hóspede no hotel para que ele possa fazer reservas.
     *
     * @param hospede o hóspede a ser cadastrado
     */
    public void adicionarHospede(Hospede hospede) {
        hospedes.add(hospede);
    }

    public ArrayList<Hospede> getHospedes() {
        return hospedes;
    }

    // -------------------------------------------------------------------------
    // Busca de disponibilidade
    // -------------------------------------------------------------------------

    /**
     * Devolve só os quartos que dá pra reservar no período pedido.
     *
     * São dois filtros: primeiro tira os que estão em manutenção; depois, pra
     * cada um que sobrou, olha se já tem reserva ativa batendo nas datas. Se
     * não bater com nenhuma, o quarto entra na lista.
     *
     * @param entrada data de entrada desejada
     * @param saida   data de saída desejada
     * @return lista dos quartos livres no período
     */
    public List<Quarto> buscarQuartosDisponiveis(LocalDate entrada, LocalDate saida) {
        List<Quarto> disponiveis = new ArrayList<>();

        for (Quarto quarto : quartos) {

            // quartos em manutenção ficam de fora
            if (quarto.getStatus() == StatusQuarto.MANUTENCAO) {
                continue;
            }

            boolean temConflito = false;

            for (Reserva reserva : reservas) {
                // só verifica reservas que ainda estão ativas
                boolean reservaAtiva = reserva.getStatus() == StatusReserva.PENDENTE
                        || reserva.getStatus() == StatusReserva.EM_ANDAMENTO;

                if (reserva.getQuarto().getId() == quarto.getId()
                        && reservaAtiva
                        && reserva.conflitaCom(entrada, saida)) {
                    temConflito = true;
                    break;
                }
            }

            if (!temConflito) {
                disponiveis.add(quarto);
            }
        }

        return disponiveis;
    }

    // -------------------------------------------------------------------------
    // Criação de reserva
    // -------------------------------------------------------------------------

    /**
     * Tenta reservar o quarto pro hóspede no período pedido.
     *
     * Se o quarto tá livre: cria a reserva e guarda na lista. Se não tá: o
     * hóspede entra na fila de espera e devolvo null. Quem chama (a tela) usa
     * esse null pra saber que caiu na fila e não virou reserva.
     *
     * @param hospede  quem quer reservar
     * @param quarto   o quarto escolhido
     * @param entrada  data de entrada
     * @param saida    data de saída
     * @return a reserva criada, ou null se foi pra fila
     */
    public Reserva criarReserva(Hospede hospede, Quarto quarto, LocalDate entrada, LocalDate saida) {
        List<Quarto> disponiveis = buscarQuartosDisponiveis(entrada, saida);

        if (disponiveis.contains(quarto)) {
            Reserva novaReserva = new Reserva(contadorReservas++, hospede, quarto, entrada, saida);
            adicionarReserva(novaReserva);
            System.out.println("Reserva criada com sucesso: " + novaReserva);
            return novaReserva;
        } else {
            filaEspera.add(hospede);
            System.out.println(hospede.getNome() + " adicionado à fila de espera do hotel " + nome);
            return null;
        }
    }

    // deixei separado de propósito caso precisem inserir uma reserva já pronta
    // de fora (importação, teste, etc.), sem passar pelo criarReserva.
    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    // -------------------------------------------------------------------------
    // Check-in e Check-out
    // -------------------------------------------------------------------------

    // o hotel só repassa pra reserva quem faz o trabalho de verdade é ela.
    // mantive o método aqui pra tela conversar sempre com o hotel, não com a reserva.
    public void realizarCheckIn(Reserva reserva) {
        reserva.realizarCheckIn();
    }

    /**
     * Check-out: encerra a estadia, libera o quarto e, se tiver gente na fila
     * de espera, chama o próximo (tira o primeiro com poll, que é o dequeue).
     *
     * @param reserva a reserva que está encerrando
     */
    public void realizarCheckOut(Reserva reserva) {
        reserva.finalizar();

        // liberou um quarto, então vê se tem alguém na fila esperando vaga
        if (!filaEspera.isEmpty()) {
            Hospede proximoDaFila = filaEspera.poll();
            System.out.println("Notificação: " + proximoDaFila.getNome()
                    + " da fila de espera — quarto " + reserva.getQuarto().getNumero() + " está disponível!");
        }
    }

    // -------------------------------------------------------------------------
    // Gestão de funcionários
    // -------------------------------------------------------------------------

    /**
     * Tenta cancelar a reserva chamando a regra de negócio da classe Reserva.
     * Se o quarto for liberado, notifica o próximo da fila de espera.
     *
     * @param reserva a reserva que o hóspede deseja cancelar
     */
    public void cancelarReserva(Reserva reserva) {
        boolean canceladoComSucesso = reserva.cancelar();

        if (canceladoComSucesso) {
            // O quarto vagou! Vamos olhar a fila de espera.
            if (!filaEspera.isEmpty()) {
                Hospede proximoDaFila = filaEspera.poll();
                System.out.println("Notificação para fila: " + proximoDaFila.getNome()
                        + ", o quarto " + reserva.getQuarto().getNumero() + " está disponível devido a um cancelamento!");
            }
        }
    }

    public void setGerente(Administrador gerente) {
        this.gerente = gerente;
        System.out.println(gerente.getNome() + " definido como gerente do hotel " + nome);
    }

    public void adicionarRecepcionista(Administrador recepcionista) {
        recepcionistas.add(recepcionista);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public ArrayList<Quarto> getQuartos() {
        return quartos;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public Queue<Hospede> getFilaEspera() {
        return filaEspera;
    }

    public Administrador getGerente() {
        return gerente;
    }

    public ArrayList<Administrador> getRecepcionistas() {
        return recepcionistas;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " - " + localizacao
                + " | Quartos: " + quartos.size()
                + " | Reservas ativas: " + reservas.size()
                + " | Fila de espera: " + filaEspera.size();
    }
}
