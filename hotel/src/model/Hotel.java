package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * O coração do sistema. Cada hotel gerencia seu próprio inventário de quartos,
 * lista de reservas ativas e fila de espera.
 *
 * Estruturas usadas:
 * - ArrayList para quartos e reservas (precisa iterar bastante)
 * - Queue (LinkedList) para a fila de espera (FIFO — quem chegou primeiro sai primeiro)
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public class Hotel {

    private int id;
    private String nome;
    private String localizacao;
    private ArrayList<Quarto> quartos;
    private ArrayList<Reserva> reservas;
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

    // -------------------------------------------------------------------------
    // Busca de disponibilidade (US02/T2)
    // -------------------------------------------------------------------------

    /**
     * Percorre todos os quartos do hotel e devolve só os que estão livres
     * para o período pedido.
     *
     * A lógica tem dois filtros:
     * 1. Ignora quartos em manutenção.
     * 2. Para cada quarto restante, verifica se existe alguma reserva ativa
     *    que conflite com as datas — se não tiver, o quarto entra na lista.
     *
     * @param entrada data de check-in desejada
     * @param saida   data de check-out desejada
     * @return lista de quartos disponíveis no período
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
    // Criação e armazenamento de reserva (US02/T3 e US07/T2 e US14/T5)
    // -------------------------------------------------------------------------

    /**
     * Tenta criar uma reserva para o hóspede no quarto e período informados.
     *
     * Se o quarto estiver disponível: cria a reserva e a guarda na lista do hotel.
     * Se não estiver: coloca o hóspede na fila de espera (enqueue).
     *
     * @param hospede  quem quer reservar
     * @param quarto   qual quarto foi escolhido
     * @param entrada  data de check-in
     * @param saida    data de check-out
     * @return a reserva criada, ou null se o hóspede foi para a fila
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

    /**
     * Guarda a reserva na lista do hotel.
     * Separado para poder ser chamado de fora quando necessário.
     *
     * @param reserva a reserva a ser armazenada
     */
    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    // -------------------------------------------------------------------------
    // Check-in e Check-out
    // -------------------------------------------------------------------------

    /**
     * Realiza o check-in da reserva: atualiza o status do quarto e da reserva.
     *
     * @param reserva a reserva que está entrando
     */
    public void realizarCheckIn(Reserva reserva) {
        reserva.realizarCheckIn();
    }

    /**
     * Realiza o check-out: finaliza a hospedagem, libera o quarto
     * e verifica se tem alguém na fila de espera.
     *
     * Se tiver, remove o primeiro da fila (dequeue) e notifica.
     *
     * @param reserva a reserva que está sendo encerrada
     */
    public void realizarCheckOut(Reserva reserva) {
        reserva.finalizar();

        // vê se tem alguém esperando por um quarto nesse hotel
        if (!filaEspera.isEmpty()) {
            Hospede proximoDaFila = filaEspera.poll();
            System.out.println("Notificação: " + proximoDaFila.getNome()
                    + " da fila de espera — quarto " + reserva.getQuarto().getNumero() + " está disponível!");
        }
    }

    // -------------------------------------------------------------------------
    // Gestão de funcionários
    // -------------------------------------------------------------------------

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
