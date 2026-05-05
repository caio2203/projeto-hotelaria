package model;

/**
 * Representa um quarto físico do hotel.
 * O status controla a disponibilidade para reservas.
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public class Quarto {

    private int id;
    private int numero;
    private TipoQuarto tipo;
    private int capacidadeMaxima;
    private StatusQuarto status;

    /**
     * Todo quarto começa disponível quando é cadastrado.
     *
     * @param id               identificador único
     * @param numero           número do quarto (ex: 101, 202)
     * @param tipo             SOLTEIRO, CASAL ou SUITE
     * @param capacidadeMaxima quantas pessoas cabem
     */
    public Quarto(int id, int numero, TipoQuarto tipo, int capacidadeMaxima) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidadeMaxima;
        this.status = StatusQuarto.DISPONIVEL;
    }

    /**
     * Manda o quarto para manutenção.
     * Enquanto estiver bloqueado, ele não aparece nas buscas.
     */
    public void bloquear() {
        this.status = StatusQuarto.MANUTENCAO;
        System.out.println("Quarto " + numero + " bloqueado para manutenção.");
    }

    /**
     * Libera o quarto após o conserto.
     */
    public void desbloquear() {
        this.status = StatusQuarto.DISPONIVEL;
        System.out.println("Quarto " + numero + " liberado e disponível novamente.");
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public StatusQuarto getStatus() {
        return status;
    }

    public void setStatus(StatusQuarto status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Quarto " + numero + " [" + tipo + "] - Cap: " + capacidadeMaxima + " | Status: " + status;
    }
}
