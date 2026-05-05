import model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe principal para testar o fluxo completo do sistema hoteleiro.
 * Cobre todas as operações: cadastro, busca, reserva, check-in e check-out.
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        // pega a instância única do sistema
        SistemaCentral sistema = SistemaCentral.getInstancia();

        // -----------------------------------------------------------------------
        // Cadastro de funcionários
        // -----------------------------------------------------------------------
        Administrador gerente = new Administrador(1, "Ana Souza", "ana.gerente", "senha123", NivelAcesso.GERENTE);
        Administrador recepcao = new Administrador(2, "Bruno Lima", "bruno.rec", "senha456", NivelAcesso.RECEPCIONISTA);
        sistema.adicionarAdministrador(gerente);
        sistema.adicionarAdministrador(recepcao);

        // -----------------------------------------------------------------------
        // Cadastro do hotel
        // -----------------------------------------------------------------------
        Hotel hotel = new Hotel(1, "Grand Palace", "Vitória da Conquista - BA");
        hotel.setGerente(gerente);
        hotel.adicionarRecepcionista(recepcao);
        sistema.adicionarHotel(hotel);

        // -----------------------------------------------------------------------
        // Cadastro de quartos (relacionamento Hotel 1:N Quarto)
        // -----------------------------------------------------------------------
        Quarto q101 = new Quarto(1, 101, TipoQuarto.SOLTEIRO, 1);
        Quarto q102 = new Quarto(2, 102, TipoQuarto.CASAL, 2);
        Quarto q201 = new Quarto(3, 201, TipoQuarto.SUITE, 4);
        hotel.adicionarQuarto(q101);
        hotel.adicionarQuarto(q102);
        hotel.adicionarQuarto(q201);

        System.out.println("\n--- Estado do hotel ---");
        System.out.println(hotel);

        // -----------------------------------------------------------------------
        // Cadastro de hóspedes
        // -----------------------------------------------------------------------
        Hospede hospede1 = new Hospede(1, "Carlos Pereira", "carlos.p", "abc123", "123.456.789-00", "(77) 99999-1111");
        Hospede hospede2 = new Hospede(2, "Fernanda Costa", "ferna.c", "xyz789", "987.654.321-00", "(77) 98888-2222");
        Hospede hospede3 = new Hospede(3, "Rafael Dias", "rafa.d", "pass321", "111.222.333-44", "(77) 97777-3333");

        // -----------------------------------------------------------------------
        // Busca de quartos disponíveis
        // -----------------------------------------------------------------------
        LocalDate entrada1 = LocalDate.of(2026, 6, 10);
        LocalDate saida1   = LocalDate.of(2026, 6, 15);

        System.out.println("\n--- Busca de quartos disponíveis de " + entrada1 + " a " + saida1 + " ---");
        List<Quarto> disponiveis = hotel.buscarQuartosDisponiveis(entrada1, saida1);
        for (Quarto q : disponiveis) {
            System.out.println(q);
        }

        // -----------------------------------------------------------------------
        // Criação de reservas
        // -----------------------------------------------------------------------
        System.out.println("\n--- Criando reservas ---");
        Reserva r1 = hotel.criarReserva(hospede1, q101, entrada1, saida1);
        Reserva r2 = hotel.criarReserva(hospede2, q102, entrada1, saida1);

        // tenta reservar o mesmo quarto no mesmo período — vai para a fila
        System.out.println("\n--- Tentando reservar quarto já ocupado ---");
        hotel.criarReserva(hospede3, q101, entrada1, saida1);

        // -----------------------------------------------------------------------
        // Bloqueio de quarto para manutenção (US16/T2)
        // -----------------------------------------------------------------------
        System.out.println("\n--- Bloqueando quarto 201 para manutenção ---");
        q201.bloquear();

        // busca depois do bloqueio — 201 não deve aparecer
        System.out.println("\n--- Busca de disponíveis após bloqueio ---");
        List<Quarto> aposBloquear = hotel.buscarQuartosDisponiveis(
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));
        for (Quarto q : aposBloquear) {
            System.out.println(q);
        }

        // -----------------------------------------------------------------------
        // Check-in (US04/T1)
        // -----------------------------------------------------------------------
        System.out.println("\n--- Check-in ---");
        if (r1 != null) hotel.realizarCheckIn(r1);
        if (r2 != null) hotel.realizarCheckIn(r2);

        // -----------------------------------------------------------------------
        // Cancelamento de reserva (US15/T3)
        // -----------------------------------------------------------------------
        System.out.println("\n--- Cancelando reserva de Fernanda ---");
        if (r2 != null) r2.cancelar();

        // -----------------------------------------------------------------------
        // Check-out / Finalização (US15/T5)
        // -----------------------------------------------------------------------
        System.out.println("\n--- Check-out de Carlos ---");
        if (r1 != null) hotel.realizarCheckOut(r1);

        // -----------------------------------------------------------------------
        // Histórico do hóspede (Stack - LIFO)
        // -----------------------------------------------------------------------
        System.out.println("\n--- Histórico de Carlos ---");
        System.out.println("Última estadia: " + hospede1.verUltimaEstadia());
        System.out.println("Total de estadias: " + hospede1.getHistorico().size());

        System.out.println("\n--- Estado final do hotel ---");
        System.out.println(hotel);
    }
}
