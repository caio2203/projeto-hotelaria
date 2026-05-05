package model;

import java.util.Stack;

/**
 * O cliente do hotel. Além dos dados pessoais, guarda um histórico
 * de estadias usando uma Pilha (Stack - LIFO).
 *
 * A pilha faz sentido aqui porque o que mais interessa é a estadia
 * mais recente, e o peek() acessa o topo em O(1) sem precisar percorrer tudo.
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public class Hospede extends Usuario {

    private String cpf;
    private String contato;
    private Stack<Reserva> historico;

    /**
     * Cria o hóspede com dados pessoais e um histórico vazio.
     *
     * @param id      identificador único
     * @param nome    nome completo
     * @param login   login de acesso
     * @param senha   senha de acesso
     * @param cpf     CPF do hóspede
     * @param contato telefone ou e-mail
     */
    public Hospede(int id, String nome, String login, String senha, String cpf, String contato) {
        super(id, nome, login, senha);
        this.cpf = cpf;
        this.contato = contato;
        this.historico = new Stack<>();
    }

    /**
     * Empilha a reserva finalizada no histórico.
     * Chamado automaticamente pelo método finalizar() da Reserva.
     *
     * @param reserva a estadia que acabou de ser concluída
     */
    public void adicionarAoHistorico(Reserva reserva) {
        historico.push(reserva);
    }

    /**
     * Retorna a última estadia sem remover da pilha.
     * Retorna null se o hóspede nunca ficou em nenhum hotel.
     *
     * @return a reserva mais recente, ou null
     */
    public Reserva verUltimaEstadia() {
        if (historico.isEmpty()) {
            return null;
        }
        return historico.peek();
    }

    /**
     * Remove e retorna a última estadia da pilha.
     * Use com cuidado — prefira verUltimaEstadia() se só quiser consultar.
     *
     * @return a reserva do topo da pilha
     */
    public Reserva removerUltimaEstadia() {
        if (historico.isEmpty()) {
            return null;
        }
        return historico.pop();
    }

    public String getCpf() {
        return cpf;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Stack<Reserva> getHistorico() {
        return historico;
    }

    @Override
    public String toString() {
        return super.toString() + " | CPF: " + cpf + " | Contato: " + contato;
    }
}
