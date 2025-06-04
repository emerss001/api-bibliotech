package dto;

import dao.MaterialDAO;
import dao.PessoaDAO;
import type.EmprestimoStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public record NovoEmprestimoDTO(
        Integer alunoId,
        Integer materialId,
        String dataDevolucaoPrevista,
        EmprestimoStatus status
) {

    public boolean valido() {
        return alunoIdValido() && materialIdValido();
    }

    public boolean validoUpdate() {
        return statusValido() && dataDevolucaoPrevistaValido();
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

    private boolean statusValido(){
        nullIsBlank(status.getDisplayName(), "Status do emprestimo");
        return true;
    }

    private boolean dataDevolucaoPrevistaValido(){
        nullIsBlank(dataDevolucaoPrevista, "Data de devolução");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            LocalDateTime.parse(dataDevolucaoPrevista, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}