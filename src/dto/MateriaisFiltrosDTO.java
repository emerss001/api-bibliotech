package dto;

import java.util.List;

public record MateriaisFiltrosDTO(
        List<String> tipo,
        List<String> nivel_conhecimento,
        List<Integer> formato_material,
        List<Integer> area_conhecimento
) {

    public boolean hasTipo() {
        return tipo != null && !tipo.isEmpty();
    }

    public boolean hasNivel() {
        return nivel_conhecimento != null && !nivel_conhecimento.isEmpty();
    }

    public boolean hasFormato() {
        return formato_material != null && !formato_material.isEmpty();
    }

    public boolean hasArea() {
        return area_conhecimento != null && !area_conhecimento.isEmpty();
    }

    // Retorna o trecho de WHERE para um campo específico
    public static String buildInClause(String fieldName, int size) {
        return size > 0
                ? " AND " + fieldName + " IN (" + "?,".repeat(size).replaceAll(",$", "") + ")"
                : "";
    }
}

