package model;

/**
 * Classe genérica Node (Nó) utilizada tanto pela Pilha quanto pela Fila.
 * Representa um elemento da lista encadeada simples.
 *
 * @param <T> Tipo genérico do dado armazenado no nó
 */
public class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}