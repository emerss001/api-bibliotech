package dto;

import dao.MaterialDAO;
import dao.PessoaDAO;
import type.EmprestimoStatus;

public record AvaliacaoDTO(
        Integer id,
        Integer alunoId,
        Integer materialId,
        Integer nota,
        String avaliacao,
        String data
){
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

        if (MaterialDAO.materialValidoAvaliacao(materialId))
            return true;
        throw new IllegalArgumentException("O material: " + materialId + " não existe, ou não está disponível");
    }
}
