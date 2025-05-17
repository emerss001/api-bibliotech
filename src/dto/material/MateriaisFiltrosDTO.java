package dto.material;

import java.util.List;

public record MateriaisFiltrosDTO(
        List<String> tipo,
        List<String> nivel,
        List<Integer> formato,
        List<Integer> area
) {

    public boolean hasTipo() {
        return tipo != null && !tipo.isEmpty();
    }

    public boolean hasNivel() {
        return nivel != null && !nivel.isEmpty();
    }

    public boolean hasFormato() {
        return formato != null && !formato.isEmpty();
    }

    public boolean hasArea() {
        return area != null && !area.isEmpty();
    }

    // Retorna o trecho de WHERE para um campo específico
    public static String buildInClause(String fieldName, int size) {
        return size > 0
                ? " AND " + fieldName + " IN (" + "?,".repeat(size).replaceAll(",$", "") + ")"
                : "";
    }
}

