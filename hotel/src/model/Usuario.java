package model;

import java.io.Serializable;

/**
 * Classe mãe de quem usa o sistema. Tanto o Hospede quanto o Administrador
 * herdam daqui porque os dois precisam de id, nome, login e senha - então
 * não fazia sentido repetir tudo isso nas duas.
 *
 * @version 1.0
 */
public abstract class Usuario implements Serializable {

    private int id;
    private String nome;
    private String login;
    private String senha;

    // as subclasses chamam esse super() pra preencher os dados de acesso comuns
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
