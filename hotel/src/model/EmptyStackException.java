package model;

/**
 * Exceção personalizada lançada quando se tenta realizar uma operação
 * inválida em uma Pilha vazia (pop ou peek sem elementos).
 *
 * Herda de RuntimeException (exceção não verificada), seguindo
 * a convenção de exceções de estruturas de dados.
 */
public class EmptyStackException extends RuntimeException {

    public EmptyStackException(String message) {
        super(message);
    }
}