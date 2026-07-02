package model;

/**
 * Ciclo de vida de uma reserva: começa pendente, vira ativa no check-in
 * e termina como concluída ou cancelada.
 */
public enum StatusReserva {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA
}
