package model;

import java.util.Stack;

/**
 * O cliente do hotel. Fora os dados pessoais, cada hóspede carrega o histórico
 * das estadias dele numa Pilha (Stack do java.util, LIFO).
 *
 * Usei pilha de propósito: o que a gente mais consulta é a última estadia, e
 * com o peek() pego o topo na hora sem varrer a lista toda. A estadia mais nova
 * sempre fica em cima.
 *
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

    // não precisa chamar isso na mão: a própria Reserva chama quando faz o
    // check-out (lá no finalizar()). Aqui é só empilhar a estadia que terminou.
    public void adicionarAoHistorico(Reserva reserva) {
        historico.push(reserva);
    }

    // só dá uma espiada no topo (não tira da pilha). Devolve null se o hóspede
    // ainda não teve nenhuma estadia. Use esse aqui quando for só pra mostrar.
    public Reserva verUltimaEstadia() {
        if (historico.isEmpty()) {
            return null;
        }
        return historico.peek();
    }

    // esse aqui remove de verdade do topo. Cuidado: se for só pra consultar,
    // usa o verUltimaEstadia() em cima senão você acaba apagando do histórico.
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
