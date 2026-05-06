package model;

/**
 * Implementação manual de Fila (Queue) usando Lista Encadeada.
 *
 * Segue rigorosamente o princípio FIFO (First In, First Out):
 * o primeiro elemento inserido é o primeiro a ser removido.
 *
 * Contexto no Sistema de Hotel:
 * Utilizada para a lista de espera de reservas quando o hotel
 * está com todos os quartos ocupados. O hóspede que solicitou
 * primeiro tem prioridade quando um quarto for liberado,
 * garantindo ordem justa de atendimento.
 *
 * Implementação via lista encadeada com ponteiros front e rear:
 * - Inserção no final (rear) em O(1)
 * - Remoção no início (front) em O(1)
 * - Não utiliza estruturas prontas do Java (java.util.Queue/LinkedList)
 *
 * @param <T> Tipo genérico do elemento armazenado
 */
public class Queue<T> {

    /** Referência para o primeiro nó (próximo a ser removido) */
    private Node<T> front;

    /** Referência para o último nó (último inserido) */
    private Node<T> rear;

    /** Número de elementos atualmente na fila */
    private int size;

    /**
     * Inicializa a fila vazia.
     */
    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Enfileira (insere) um elemento no final da fila.
     * Se a fila estiver vazia, front e rear apontam para o mesmo nó.
     *
     * Complexidade: O(1)
     *
     * @param element Elemento a ser inserido
     * @throws IllegalArgumentException se o elemento for null
     */
    public void enqueue(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Não é permitido inserir elemento nulo na fila.");
        }
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
        size++;
    }

    /**
     * Desenfileira (remove e retorna) o elemento do início da fila.
     * Se a fila ficar vazia após a remoção, rear é ajustado para null.
     *
     * Complexidade: O(1)
     *
     * @return Elemento removido do início da fila
     * @throws EmptyQueueException se a fila estiver vazia
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyQueueException("Não é possível remover de uma fila vazia.");
        }
        T data = front.getData();
        front = front.getNext();
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /**
     * Consulta (sem remover) o elemento no início da fila.
     * No contexto do hotel, mostra qual hóspede será atendido primeiro.
     *
     * Complexidade: O(1)
     *
     * @return Elemento no início da fila
     * @throws EmptyQueueException se a fila estiver vazia
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyQueueException("Não é possível consultar o início de uma fila vazia.");
        }
        return front.getData();
    }

    /**
     * Verifica se a fila está vazia.
     *
     * Complexidade: O(1)
     *
     * @return true se não houver elementos; false caso contrário
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Retorna o número de elementos presentes na fila.
     *
     * Complexidade: O(1)
     *
     * @return Quantidade de elementos na fila
     */
    public int size() {
        return size;
    }

    /**
     * Exibe todos os elementos da fila do início ao fim.
     *
     * Complexidade: O(n)
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("  [Fila vazia]");
            return;
        }
        System.out.println("  FRENTE (próximo a ser atendido)");
        Node<T> current = front;
        int pos = 1;
        while (current != null) {
            System.out.printf("  [%2d] %s%n", pos, current.getData().toString());
            current = current.getNext();
            pos++;
        }
        System.out.printf("  Total na fila: %d%n", size);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Queue[]";
        StringBuilder sb = new StringBuilder("Queue[front -> ");
        Node<T> current = front;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) sb.append(" -> ");
            current = current.getNext();
        }
        sb.append(" <- rear]");
        return sb.toString();
    }
}