package model.pessoa;

import dao.PessoaDAO;
import dto.PessoaDTO;

public class Aluno extends Pessoa {
    private String matricula;
    private int IdNecessidade;

    public Aluno(PessoaDTO dto, String matricula, int necessidade, boolean senhaJaHasheada) {
        super(0,dto.senha(), dto.nome(), dto.email(), senhaJaHasheada);
        this.matricula = matricula;
        this.IdNecessidade = necessidade;
    }

    public Aluno cadastrarAluno(PessoaDAO dao) {
        int idNovaPessoa = salvar(dao);
        return dao.addAluno(this, idNovaPessoa);
    }

    public String getMatricula() {
        return this.matricula;
    }

    public int getIdNecessidade() {
        return this.IdNecessidade;
    }
}
