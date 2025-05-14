package dto;

import java.util.List;

public record EmprestimoFiltroDTO(
        Integer quantidade,
        List<String> status,
        List<String> alunoId
) {
    public boolean hasStatus(){
        return status != null && !status.isEmpty();
    }

    public boolean hasAlunoId(){
        return alunoId != null && !alunoId.isEmpty();
    }

    public static String buildInClause(String fieldName, int size) {
        return size > 0
                ? " AND " + fieldName + " IN (" + "?,".repeat(size).replaceAll(",$", "") + ")"
                : "";
    }
}
