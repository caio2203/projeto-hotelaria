package model;

/**
 * Implementação manual de Pilha (Stack) usando Lista Encadeada.
 *
 * Segue rigorosamente o princípio LIFO (Last In, First Out):
 * o último elemento inserido é o primeiro a ser removido.
 *
 * Contexto no Sistema de Hotel:
 * Utilizada para armazenar o histórico de estadias de um hóspede.
 * A estadia mais recente fica sempre no topo, permitindo acesso
 * imediato à última visita do hóspede ao hotel via peek() em O(1).
 *
 * Implementação via lista encadeada:
 * - Sem limite fixo de tamanho (cresce dinamicamente)
 * - Inserção e remoção no topo em O(1)
 * - Não utiliza estruturas prontas do Java (java.util.Stack)
 *
 * @param <T> Tipo genérico do elemento armazenado
 */
public class Stack<T> {

    /** Referência para o nó no topo da pilha */
    private Node<T> top;

    /** Número de elementos atualmente na pilha */
    private int size;

    /**
     * Inicializa a pilha vazia.
     */
    public Stack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Empilha (insere) um elemento no topo da pilha.
     * O novo nó aponta para o antigo topo, preservando a ordem LIFO.
     *
     * Complexidade: O(1)
     *
     * @param element Elemento a ser inserido
     * @throws IllegalArgumentException se o elemento for null
     */
    public void push(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Não é permitido inserir elemento nulo na pilha.");
        }
        Node<T> newNode = new Node<>(element);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    /**
     * Desempilha (remove e retorna) o elemento do topo da pilha.
     *
     * Complexidade: O(1)
     *
     * @return Elemento removido do topo
     * @throws EmptyStackException se a pilha estiver vazia
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException("Não é possível remover de uma pilha vazia.");
        }
        T data = top.getData();
        top = top.getNext();
        size--;
        return data;
    }

    /**
     * Consulta (sem remover) o elemento no topo da pilha.
     * No contexto do hotel, retorna a estadia mais recente do hóspede.
     *
     * Complexidade: O(1)
     *
     * @return Elemento no topo da pilha
     * @throws EmptyStackException se a pilha estiver vazia
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException("Não é possível consultar o topo de uma pilha vazia.");
        }
        return top.getData();
    }

    /**
     * Verifica se a pilha está vazia.
     *
     * Complexidade: O(1)
     *
     * @return true se não houver elementos; false caso contrário
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Retorna o número de elementos presentes na pilha.
     *
     * Complexidade: O(1)
     *
     * @return Quantidade de elementos na pilha
     */
    public int size() {
        return size;
    }

    /**
     * Exibe todos os elementos da pilha do topo à base.
     *
     * Complexidade: O(n)
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("  [Pilha vazia]");
            return;
        }
        System.out.println("  Topo (mais recente)");
        Node<T> current = top;
        int pos = 1;
        while (current != null) {
            System.out.printf("  [%2d] %s%n", pos, current.getData().toString());
            current = current.getNext();
            pos++;
        }
        System.out.println("  Base (mais antigo)");
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Stack[]";
        StringBuilder sb = new StringBuilder("Stack[topo -> ");
        Node<T> current = top;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) sb.append(" -> ");
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}