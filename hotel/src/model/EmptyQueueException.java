package model;

/**
 * Exceção personalizada lançada quando se tenta realizar uma operação
 * inválida em uma Fila vazia (dequeue ou peek sem elementos).
 *
 * Herda de RuntimeException (exceção não verificada), seguindo
 * a convenção de exceções de estruturas de dados.
 */
public class EmptyQueueException extends RuntimeException {

    public EmptyQueueException(String message) {
        super(message);
    }
}