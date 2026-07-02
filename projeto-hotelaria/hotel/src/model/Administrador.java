package model;

/**
 * O funcionário do hotel: gerente ou recepcionista. Quem diz o que ele pode
 * fazer é o nível de acesso (por enquanto a gente só guarda; a regra de
 * permissão em si fica pra parte do Luis/Andrei).
 *
 * @version 1.0
 */
public class Administrador extends Usuario {

    private NivelAcesso nivelAcesso;

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
