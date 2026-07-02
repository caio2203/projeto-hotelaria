package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Liga um hóspede a um quarto num período. É aqui que mora a parte mais
 * importante das regras: conflito de datas, check-in, cancelamento e check-out.
 *
 * Luis/Andrei: se forem apertar as validações de negócio, é nesta classe.
 *
 * @version 1.0
 */
public class Reserva implements Serializable {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int PRAZO_CANCELAMENTO_DIAS = 2;

    private int id;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private StatusReserva status;
    private Hospede hospede;
    private Quarto quarto;

    /**
     * Cria a reserva no estado PENDENTE. O check-in ainda não aconteceu.
     *
     * @param id          identificador único
     * @param hospede     quem está reservando
     * @param quarto      qual quarto foi escolhido
     * @param dataEntrada data de chegada
     * @param dataSaida   data de saída
     */
    public Reserva(int id, Hospede hospede, Quarto quarto, LocalDate dataEntrada, LocalDate dataSaida) {
        this.id = id;
        this.hospede = hospede;
        this.quarto = quarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.status = StatusReserva.PENDENTE;
    }

    /**
     * Diz se o período passado encosta no período desta reserva (mesmo quarto).
     * <p>
     * O truque dos dois intervalos: [A,B] e [C,D] se cruzam quando C não passa
     * de B E A não passa de D. Testei com datas coladas e funciona, mas se
     * alguém achar um caso de borda esquisito me avisa.
     *
     * @param entrada entrada da reserva nova
     * @param saida   saída da reserva nova
     * @return true se bater (tem conflito)
     */
    public boolean conflitaCom(LocalDate entrada, LocalDate saida) {
        return !saida.isBefore(this.dataEntrada) && !entrada.isAfter(this.dataSaida);
    }

    // check-in: bota o quarto como ocupado e a reserva como em andamento.
    // a ideia é chamar quando o hóspede chega de fato (data de entrada).
    public void realizarCheckIn() {
        this.status = StatusReserva.EM_ANDAMENTO;
        this.quarto.setStatus(StatusQuarto.OCUPADO);
        System.out.println("Check-in realizado: " + hospede.getNome() + " no quarto " + quarto.getNumero());
    }

    // cancela e já solta o quarto na hora. Tanto faz se a reserva tava
    // pendente ou em andamento, funciona pros dois casos.
    public void cancelar() {
        this.status = StatusReserva.CANCELADA;
        this.quarto.setStatus(StatusQuarto.DISPONIVEL);

        if (isCancelamentoGratuito()) {
            System.out.println("Reserva #" + id + " cancelada gratuitamente dentro do prazo. Quarto " + quarto.getNumero() + " liberado."); //[cite: 7]
        } else {
            System.out.println("Reserva #" + id + " cancelada FORA do prazo. Quarto " + quarto.getNumero() + " liberado. Taxa de no-show aplicavel."); //[cite: 7]
        }
    }

    public boolean isCancelamentoGratuito() {
        // Se a reserva já começou ou foi concluída, não há cancelamento gratuito
        if (this.status == StatusReserva.EM_ANDAMENTO || this.status == StatusReserva.CONCLUIDA) { //[cite: 7, 10]
            return false;
        }

        LocalDate hoje = LocalDate.now();
        // Verifica se a data de hoje está pelo menos 2 dias atrás da data de entrada
        return hoje.isBefore(this.dataEntrada.minusDays(PRAZO_CANCELAMENTO_DIAS)) ||
                hoje.isEqual(this.dataEntrada.minusDays(PRAZO_CANCELAMENTO_DIAS));
    }



    // check-out: encerra a estadia, libera o quarto e joga a reserva no
    // histórico do hóspede (a pilha). É o finalizar que empurra pra Stack.
    public void finalizar() {
        this.status = StatusReserva.CONCLUIDA;
        this.quarto.setStatus(StatusQuarto.DISPONIVEL);
        this.hospede.adicionarAoHistorico(this);
        System.out.println("Check-out realizado: " + hospede.getNome() + " deixou o quarto " + quarto.getNumero());
    }

    public int getId() {
        return id;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    @Override
    public String toString() {
        return "Reserva #" + id
                + " | " + hospede.getNome()
                + " | Quarto " + quarto.getNumero()
                + " | " + dataEntrada.format(FORMATO_DATA) + " → " + dataSaida.format(FORMATO_DATA)
                + " | " + status;
    }
}
