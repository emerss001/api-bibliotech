package model.pessoa;

import dao.PessoaDAO;
import dto.PessoaDTO;

public class Professor extends Pessoa {
    private String siap;
    public Professor(PessoaDTO dto, String siap, boolean senhaJaHasheada) {
        super(dto.senha(), dto.nome(), dto.email(), senhaJaHasheada);
        this.siap = siap;
    }

    public void validarSiap(String siap) {
        if (siap == null || siap.trim().isEmpty()) {
            throw new IllegalArgumentException("SIAP não pode ser vazio");
        }
    }

    public Professor cadastrarProfessor(PessoaDAO dao) {
        int idNovaPessoa = salvar(dao);
        return dao.addProfessor(this, idNovaPessoa);
    }

    public String getSiap() {
        return this.siap;
    }
}
