package model;

import java.util.ArrayList;

/**
 * O "guarda-tudo" do sistema. Tem uma instância só (Singleton), então em
 * qualquer parte do código a gente pega o mesmo SistemaCentral com
 * getInstancia() e mexe na mesma lista de hotéis e administradores - sem
 * risco de cada tela criar a sua e os dados ficarem bagunçados.
 *
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

    // é por aqui que todo mundo pega o sistema. Na primeira vez cria,
    // depois sempre devolve o mesmo objeto.
    public static SistemaCentral getInstancia() {
        if (instancia == null) {
            instancia = new SistemaCentral();
        }
        return instancia;
    }

    // a Persistencia chama isso na abertura: troca as listas vazias pelas que
    // vieram do arquivo. Não chamem em outro lugar senão zera o que tá em memória.
    public void restaurar(ArrayList<Hotel> hoteis, ArrayList<Administrador> administradores) {
        this.hoteis = hoteis;
        this.administradores = administradores;
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
