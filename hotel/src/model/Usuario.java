package model;

/**
 * Classe mãe de todos os usuários do sistema.
 * Hóspede e Administrador herdam daqui porque ambos têm login e senha.
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public abstract class Usuario {

    private int id;
    private String nome;
    private String login;
    private String senha;

    /**
     * Cria um usuário com os dados de acesso básicos.
     *
     * @param id    identificador único
     * @param nome  nome completo
     * @param login nome de usuário para entrar no sistema
     * @param senha senha de acesso
     */
    public Usuario(int id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (login: " + login + ")";
    }
}
