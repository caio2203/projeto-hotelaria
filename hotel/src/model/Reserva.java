package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Vincula um hóspede a um quarto em um período específico.
 * Aqui ficam as regras mais importantes do sistema: conflito de datas,
 * check-in, cancelamento e finalização.
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public class Reserva {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
     * Verifica se esse intervalo de datas bate com outra reserva no mesmo quarto.
     *
     * A lógica é simples: dois intervalos [A, B] e [C, D] se sobrepõem quando
     * C não está depois de B E A não está depois de D.
     *
     * @param entrada data de entrada da nova reserva
     * @param saida   data de saída da nova reserva
     * @return true se houver conflito
     */
    public boolean conflitaCom(LocalDate entrada, LocalDate saida) {
        return !saida.isBefore(this.dataEntrada) && !entrada.isAfter(this.dataSaida);
    }

    /**
     * Realiza o check-in: marca o quarto como ocupado e a reserva como ativa.
     * Só faz sentido chamar isso quando a data de entrada chegou.
     */
    public void realizarCheckIn() {
        this.status = StatusReserva.EM_ANDAMENTO;
        this.quarto.setStatus(StatusQuarto.OCUPADO);
        System.out.println("Check-in realizado: " + hospede.getNome() + " no quarto " + quarto.getNumero());
    }

    /**
     * Cancela a reserva e libera o quarto imediatamente.
     * Funciona tanto para reservas pendentes quanto ativas.
     */
    public void cancelar() {
        this.status = StatusReserva.CANCELADA;
        this.quarto.setStatus(StatusQuarto.DISPONIVEL);
        System.out.println("Reserva #" + id + " cancelada. Quarto " + quarto.getNumero() + " liberado.");
    }

    /**
     * Finaliza a hospedagem no check-out: libera o quarto e
     * empilha essa reserva no histórico do hóspede (Stack - LIFO).
     */
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
