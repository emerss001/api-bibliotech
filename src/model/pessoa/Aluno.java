package model.pessoa;

public class Aluno extends Pessoa {
    private String matricula;
    private int IdNecessidade;

    public Aluno(String nome, String email, String matricula, String senha, int necessidade, boolean senhaJaHasheada) {
        super(senha, nome, email, senhaJaHasheada);
        this.matricula = matricula;
        this.IdNecessidade = necessidade;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public int getIdNecessidade() {
        return this.IdNecessidade;
    }
}
