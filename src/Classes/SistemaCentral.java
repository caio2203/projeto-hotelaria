package Classes;

import java.util.ArrayList;
import java.util.List;

// classe responsável por gerenciar todos os hotéis do sistema
public class SistemaCentral {

    // instância única da classe (padrão Singleton)
    private static SistemaCentral instance;

    // lista que armazena todos os hotéis cadastrados
    private List<Hotel> hoteis;

    // construtor privado:
    // impede que outras classes criem objetos usando "new SistemaCentral()"
    // isso garante que só exista UMA instância da classe
    private SistemaCentral() {
        this.hoteis = new ArrayList<>(); // inicializa a lista de hotéis
    }

    // método público para acessar a instância única da classe
    public static SistemaCentral getInstance() {

        // se ainda não foi criada, cria a instância
        if (instance == null) {
            instance = new SistemaCentral();
        }

        // retorna sempre a mesma instância
        return instance;
    }

    // método para cadastrar (adicionar) um novo hotel na lista
    public void cadastrarHotel(Hotel hotel) {

        // verifica se o hotel não é nulo (evita erro)
        if (hotel != null) {
            hoteis.add(hotel); // adiciona o hotel na lista
        }
    }

    // método para buscar um hotel pelo ID
    public Hotel buscarHotel(int id) {

        // percorre todos os hotéis da lista
        for (Hotel h : hoteis) {

            // se encontrar um hotel com o mesmo ID
            if (h.getId() == id) {
                return h; // retorna o hotel encontrado
            }
        }

        // se não encontrar nenhum hotel com o ID informado
        return null;
    }

    // método para listar todos os hotéis cadastrados
    public List<Hotel> listarHoteis() {

        // retorna a lista completa de hotéis
        return hoteis;
    }
}
