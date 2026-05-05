package model;

import java.util.ArrayList;

/**
 * Gerenciador global do sistema — existe uma única instância (Singleton).
 *
 * O padrão Singleton garante que todos os hotéis e administradores fiquem
 * num lugar só, sem risco de criar listas duplicadas em partes diferentes
 * do código.
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public class SistemaCentral {

    // a única instância do sistema
    private static SistemaCentral instancia;

    private ArrayList<Hotel> hoteis;
    private ArrayList<Administrador> administradores;

    // construtor privado — ninguém pode criar um SistemaCentral com "new"
    private SistemaCentral() {
        hoteis = new ArrayList<>();
        administradores = new ArrayList<>();
    }

    /**
     * Ponto de acesso global. Na primeira chamada cria a instância;
     * nas seguintes devolve a mesma.
     *
     * @return a instância única do SistemaCentral
     */
    public static SistemaCentral getInstancia() {
        if (instancia == null) {
            instancia = new SistemaCentral();
        }
        return instancia;
    }

    /**
     * Cadastra um hotel no sistema.
     *
     * @param hotel o hotel a ser adicionado
     */
    public void adicionarHotel(Hotel hotel) {
        hoteis.add(hotel);
        System.out.println("Hotel '" + hotel.getNome() + "' cadastrado no sistema.");
    }

    /**
     * Cadastra um administrador no sistema.
     *
     * @param admin o administrador a ser adicionado
     */
    public void adicionarAdministrador(Administrador admin) {
        administradores.add(admin);
    }

    /**
     * Busca um hotel pelo ID.
     *
     * @param id o ID do hotel
     * @return o hotel encontrado, ou null se não existir
     */
    public Hotel buscarHotelPorId(int id) {
        for (Hotel hotel : hoteis) {
            if (hotel.getId() == id) {
                return hotel;
            }
        }
        return null;
    }

    /**
     * Busca um administrador pelo login.
     *
     * @param login o login do administrador
     * @return o administrador, ou null se não encontrar
     */
    public Administrador buscarAdminPorLogin(String login) {
        for (Administrador admin : administradores) {
            if (admin.getLogin().equals(login)) {
                return admin;
            }
        }
        return null;
    }

    public ArrayList<Hotel> getHoteis() {
        return hoteis;
    }

    public ArrayList<Administrador> getAdministradores() {
        return administradores;
    }
}
