package model;

import java.io.Serializable;

/**
 * Um quarto de verdade do hotel. O que manda na hora de reservar é o status:
 * se tá DISPONIVEL pode reservar, se tá OCUPADO ou em MANUTENCAO não aparece
 * na busca.
 *
 * @version 1.0
 */
public class Quarto implements Serializable {

    private int id;
    private int numero;
    private TipoQuarto tipo;
    private int capacidadeMaxima;
    private StatusQuarto status;

    // quando cadastra, o quarto já nasce DISPONIVEL - ninguém precisa setar isso na mão
    public Quarto(int id, int numero, TipoQuarto tipo, int capacidadeMaxima) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidadeMaxima;
        this.status = StatusQuarto.DISPONIVEL;
    }

    // joga o quarto pra manutenção. Enquanto tiver assim, ele some das buscas.
    public void bloquear() {
        this.status = StatusQuarto.MANUTENCAO;
        System.out.println("Quarto " + numero + " bloqueado para manutenção.");
    }

    // terminou o conserto, volta a ficar disponível
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
