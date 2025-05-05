package dto;

import dao.MaterialDAO;
import dao.PessoaDAO;
import type.EmprestimoStatus;

import java.util.Date;

public record NovoEmprestimoDTO(
        Integer id,
        Integer alunoId,
        Integer materialId,
        String dataDevolucaoPrevista,
        boolean devolvido,
        boolean renovado,
        EmprestimoStatus status
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

        if (MaterialDAO.materialValido(materialId))
            return true;
        throw new IllegalArgumentException("O material: " + materialId + " não existe, ou não está disponível");
    }

}