package Classes;

import java.util.Stack;

// classe que representa um hóspede do hotel
public class Hospede {

    // identificador unico do hóspede
    private int id;

    // nome do hóspede
    private String nome;

    // CPF (identificação única no mundo real)
    private String cpf;

    // contato (telefone, email, etc.)
    private String contato;

    // estrutura de dados do tipo pilha (stack)
    // armazena o histórico de reservas do hóspede
    private Stack<Reserva> historico;

    // construtor: inicializa os dados do hóspede
    public Hospede(int id, String nome, String cpf, String contato) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.contato = contato;

        // inicializa a pilha de histórico (vazia)
        this.historico = new Stack<>();
    }
    // GETTERS (acesso aos dados)
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getContato() {
        return contato;
    }

    // retorna todo o histórico de reservas
    public Stack<Reserva> getHistorico() {
        return historico;
    }
    // MÉTODOS DE NEGÓCIO

    // adiciona uma reserva ao histórico do hóspede
    public void adicionarHistorico(Reserva r) {

        // verifica se a reserva não é nula
        if (r != null) {

            // adiciona no topo da pilha
            // (última reserva sempre fica por cima)
            historico.push(r);
        }
    }

    // retorna a última estadia do hóspede
    public Reserva obterUltimaEstadia() {

        // verifica se a pilha não está vazia
        if (!historico.isEmpty()) {

            // retorna o elemento do topo (última reserva)
            return historico.peek();
        }

        // se não houver histórico
        return null;
    }
}