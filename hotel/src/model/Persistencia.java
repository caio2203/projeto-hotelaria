package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * É aqui que a gente salva e carrega os dados do arquivo.
 *
 * Resolvi usar serialização de objetos em vez de ficar montando CSV na mão:
 * como tudo (hotel, quartos, reservas, hóspede e até a pilha de histórico) já
 * é Serializable, dá pra gravar o grafo inteiro de uma vez só e ler de volta
 * do mesmo jeito. Bem menos código do que parsear texto linha por linha.
 *
 * O arquivo (hotel-dados.ser) fica na pasta onde o programa roda.
 *
 * @version 1.0
 */
public final class Persistencia {

    private static final Path ARQUIVO = Path.of("hotel-dados.ser");

    // classe só de método estático, ninguém precisa instanciar
    private Persistencia() {
    }

    /**
     * Salva o estado atual no arquivo. O HotelApp chama isso quando a janela fecha.
     *
     * @param sistema a instância única do sistema
     */
    public static void salvar(SistemaCentral sistema) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO.toFile()))) {
            out.writeObject(sistema.getHoteis());
            out.writeObject(sistema.getAdministradores());
            System.out.println("Dados salvos em " + ARQUIVO.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Falha ao salvar dados: " + e.getMessage());
        }
    }

    /**
     * Lê o arquivo (se existir) e coloca os dados de volta no sistema.
     * Na primeira vez que roda não tem arquivo nenhum, então só devolve false
     * e quem chamou (o HotelApp) trata de popular os dados de exemplo.
     *
     * @param sistema a instância única do sistema
     * @return true se achou e carregou o arquivo; false se não tinha nada salvo
     */
    @SuppressWarnings("unchecked")
    public static boolean carregar(SistemaCentral sistema) {
        // primeira execução: arquivo ainda não existe
        if (!Files.exists(ARQUIVO)) {
            return false;
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ARQUIVO.toFile()))) {
            ArrayList<Hotel> hoteis = (ArrayList<Hotel>) in.readObject();
            ArrayList<Administrador> admins = (ArrayList<Administrador>) in.readObject();
            sistema.restaurar(hoteis, admins);
            System.out.println("Dados recuperados de " + ARQUIVO.toAbsolutePath());
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Falha ao carregar dados: " + e.getMessage());
            return false;
        }
    }
}
