package model;

/**
 * Representa um funcionário do hotel — pode ser gerente ou recepcionista.
 * O nível de acesso define o que cada um pode fazer no sistema.
 *
 * @author Caio Goncalves Vieira
 * @version 1.0
 */
public class Administrador extends Usuario {

    private NivelAcesso nivelAcesso;

    /**
     * Cria um administrador com seu respectivo nível.
     *
     * @param id          identificador único
     * @param nome        nome completo
     * @param login       login de acesso
     * @param senha       senha de acesso
     * @param nivelAcesso GERENTE ou RECEPCIONISTA
     */
    public Administrador(int id, String nome, String login, String senha, NivelAcesso nivelAcesso) {
        super(id, nome, login, senha);
        this.nivelAcesso = nivelAcesso;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    @Override
    public String toString() {
        return super.toString() + " | Nível: " + nivelAcesso;
    }
}
