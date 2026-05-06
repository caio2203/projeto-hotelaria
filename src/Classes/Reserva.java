package Classes;

import java.util.Date;

// classe que representa uma reserva de hospedagem no hotel
public class Reserva {

    // identificador único da reserva
    private int id;

    // data de entrada (check-in)
    private Date dataEntrada;

    // data de saída (check-out)
    private Date dataSaida;

    // status da reserva:
    // "pendente" : criada mas ainda não confirmada
    // "ativa": confirmada / em andamento
    // "cancelada": cancelada pelo usuário
    // "concluida": hospedagem finalizada
    private String status;

    // relação com as outras entidades do sistema

    // hóspede que fez a reserva
    private Hospede hospede;

    // quarto reservado
    private Quarto quarto;

    // hotel onde a reserva foi feita
    private Hotel hotel;

    // construtor: cria uma nova reserva com os dados iniciais
    public Reserva(int id, Date dataEntrada, Date dataSaida,
                   Hospede hospede, Quarto quarto, Hotel hotel) {

        this.id = id;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.hospede = hospede;
        this.quarto = quarto;
        this.hotel = hotel;

        // toda reserva começa como pendente
        // (ainda não confirmada pelo sistema)
        this.status = "pendente";
    }
    // GETTERS (acesso aos dados)

    public int getId() {
        return id;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public String getStatus() {
        return status;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public Hotel getHotel() {
        return hotel;
    }
    // MÉTODOS DE NEGÓCIO

    // CONFIRMAR RESERVA
    public void confirmar() {

        // altera o status para ativa
        // isso indica que a reserva foi aceita pelo sistema
        this.status = "ativa";
    }

    // CANCELAR RESERVA
    public void cancelar() {

        // altera o status para cancelada
        this.status = "cancelada";

        // libera o quarto (fica disponível novamente)
        if (quarto != null) {
            quarto.liberar();
        }
    }

    // CHECK-IN (início da hospedagem)
    public void iniciar() {

        // define a reserva como ativa
        this.status = "ativa";

        // marca o quarto como ocupado
        if (quarto != null) {
            quarto.setStatus("ocupado");
        }
    }

    // CHECK-OUT (fim da hospedagem)
    public void finalizar() {

        // marca a reserva como concluída
        this.status = "concluida";

        // libera o quarto para novas reservas
        if (quarto != null) {
            quarto.liberar();
        }

        // salva essa reserva no histórico do hóspede
        // (usando a pilha - Stack)
        if (hospede != null) {
            hospede.adicionarHistorico(this);
        }
    }
}