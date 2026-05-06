package Classes;

// classe que representa um quarto dentro do hotel
public class Quarto {

    // identificador único do quarto
    private int id;

    // número do quarto (ex: 101, 202...)
    private int numero;

    // tipo do quarto (ex: solteiro, casal, suíte)
    private String tipo;

    // capacidade máxima de hóspedes
    private int capacidadeMaxima;

    // status do quarto:
    // "disponivel", "ocupado", "manutencao"
    private String status;

    // construtor: inicializa os dados do quarto
    public Quarto(int id, int numero, String tipo, int capacidadeMaxima) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.capacidadeMaxima = capacidadeMaxima;

        // Todo quarto começa como disponível
        this.status = "disponivel";
    }
    // GETTERS (acesso aos dados)

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public String getStatus() {
        return status;
    }
    // SETTERS (alteração de dados)

    // permite alterar o status manualmente
    public void setStatus(String status) {
        this.status = status;
    }
    // MÉTODOS DE NEGÓCIO

    // coloca o quarto em manutenção (indisponível para reservas)
    public void bloquear() {
        this.status = "manutencao";
    }

    // libera o quarto para uso (disponível novamente)
    public void liberar() {
        this.status = "disponivel";
    }

    // verifica se o quarto está disponível para reserva
    public boolean estaDisponivel() {

        // retorna true se o status for "disponivel"
        return this.status.equals("disponivel");
    }
}