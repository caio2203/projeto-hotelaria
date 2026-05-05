package Classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;

// classe que representa um hotel e gerencia quartos, reservas e fila de espera
public class Hotel {

    // identificação do hotel
    private int id;
    private String nome;
    private String localizacao;

    // lista de quartos do hotel
    private List<Quarto> quartos;

    // lista de reservas feitas no hotel
    private List<Reserva> reservas;

    // fila de espera de hóspedes (FIFO - primeiro entra, primeiro sai)
    private Queue<Hospede> filaEspera;

    // construtor: inicializa o hotel com seus dados e listas vazias
    public Hotel(int id, String nome, String localizacao) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.quartos = new ArrayList<>();     // lista dinâmica de quartos
        this.reservas = new ArrayList<>();    // lista dinâmica de reservas
        this.filaEspera = new LinkedList<>(); // fila para espera
    }
    // GETTERS (acesso aos dados)

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public List<Quarto> getQuartos() {
        return quartos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public Queue<Hospede> getFilaEspera() {
        return filaEspera;
    }
    // MÉTODOS DE NEGÓCIO

    // adiciona um novo quarto ao hotel
    public void adicionarQuarto(Quarto q) {

        // verifica se o quarto não é nulo (evita erro)
        if (q != null) {
            quartos.add(q);
        }
    }

    // busca um quarto pelo número
    public Quarto buscarQuarto(int numero) {

        // percorre todos os quartos
        for (Quarto q : quartos) {

            // se encontrar o número correspondente
            if (q.getNumero() == numero) {
                return q;
            }
        }

        // se não encontrar
        return null;
    }

    // verifica quais quartos estão disponíveis em um período
    public List<Quarto> verificarDisponibilidade(Date dataEntrada, Date dataSaida) {

        // lista de quartos disponíveis
        List<Quarto> disponiveis = new ArrayList<>();

        // percorre todos os quartos do hotel
        for (Quarto q : quartos) {

            // só verifica quartos que estão livres
            if (q.estaDisponivel()) {

                boolean ocupado = false;

                // verifica se já existe reserva nesse quarto
                for (Reserva r : reservas) {

                    // se for o mesmo quarto e a reserva estiver ativa
                    if (r.getQuarto().getId() == q.getId() &&
                            r.getStatus().equals("ativa")) {

                        // verifica conflito de datas
                        // (se as datas se sobrepõem)
                        if (!(dataSaida.before(r.getDataEntrada()) ||
                                dataEntrada.after(r.getDataSaida()))) {

                            ocupado = true;
                            break;
                        }
                    }
                }

                // se não estiver ocupado, adiciona à lista
                if (!ocupado) {
                    disponiveis.add(q);
                }
            }
        }

        return disponiveis;
    }

    // calcula a taxa de ocupação do hotel em uma data específica
    public double calcularTaxaOcupacao(Date data) {

        int ocupados = 0;

        // percorre todas as reservas
        for (Reserva r : reservas) {

            // considera apenas reservas ativas
            if (r.getStatus().equals("ativa")) {

                // verifica se a data está dentro da reserva
                if (!data.before(r.getDataEntrada()) &&
                        !data.after(r.getDataSaida())) {

                    ocupados++;
                }
            }
        }

        // evita divisão por zero
        if (quartos.size() == 0) return 0;

        // calcula percentual de ocupação
        return (double) ocupados / quartos.size() * 100;
    }

    // adiciona uma nova reserva
    public void adicionarReserva(Reserva r) {

        if (r != null) {

            // verifica quartos disponíveis no período
            List<Quarto> disponiveis = verificarDisponibilidade(
                    r.getDataEntrada(), r.getDataSaida()
            );

            // se o quarto da reserva está disponível
            if (disponiveis.contains(r.getQuarto())) {

                reservas.add(r);   // adiciona reserva
                r.confirmar();     // muda status para ativa

                System.out.println("Reserva confirmada!");

            } else {

                // se não tiver vaga, adiciona o hóspede na fila de espera
                filaEspera.add(r.getHospede());

                System.out.println("Sem vaga. Hóspede adicionado à fila de espera.");
            }
        }
    }

    // cancela uma reserva pelo ID
    public void cancelarReserva(int idReserva) {

        // usa Iterator para evitar erro ao remover
        Iterator<Reserva> it = reservas.iterator();

        while (it.hasNext()) {

            Reserva r = it.next();

            // se encontrar a reserva
            if (r.getId() == idReserva) {

                r.cancelar(); // altera status
                it.remove();  // remove da lista

                System.out.println("Reserva cancelada!");
                return;
            }
        }
    }
}
