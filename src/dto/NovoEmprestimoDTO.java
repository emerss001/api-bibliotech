package dto;

import dao.MaterialDAO;
import dao.PessoaDAO;

public record NovoEmprestimoDTO(
        Integer alunoId,
        Integer materialId
) {

    public boolean valido() {
        return alunoIdValido() && materialIdValido();
    }

    private void nullIsBlank(String s, String nomeCampo) {
        if (s == null || s.trim().isBlank()) throw new IllegalArgumentException("O " + nomeCampo + " não pode ser vazio");
    }

    private boolean alunoIdValido() {
        nullIsBlank(alunoId.toString(), "Id do aluno");
        return PessoaDAO.pessoaValido(alunoId);
    }

    private boolean materialIdValido() {
        nullIsBlank(materialId.toString(), "Id do material");
        return MaterialDAO.materialValido(materialId);
    }

}