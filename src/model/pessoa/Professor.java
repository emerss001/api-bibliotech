package model.pessoa;

public class Professor extends Pessoa {
    private String siap;
    public Professor(String nome, String email, String siap, String senha) {
        super(senha, nome, email);
        this.siap = siap;
    }

    public void validarSiap(String siap) {
        if (siap == null || siap.trim().isEmpty()) {
            throw new IllegalArgumentException("SIAP não pode ser vazio");
        }
    }

    public String getSiap() {
        return this.siap;
    }
}
