package dto;

import type.MaterialNivel;

import javax.servlet.http.Part;

public record NovoMaterialDTO (
        String titulo,
        String autor,
        String formato,
        String area,
        MaterialNivel nivel,
        String descricao,
        Part arquivo
) {
    // Método de validação
    public boolean valido() {
        return tituloValido() && autorValido() && formatoValido() && areaValido() && nivelValido() && arquivoValido();
    }

    private void nullIsBlank(String s, String nomeCampo) {
        if (s == null || s.trim().isBlank()) throw new IllegalArgumentException("O " + nomeCampo + " não pode ser vazio");
    }

    private boolean tituloValido() {
        nullIsBlank(titulo, "Titulo");
        if (titulo.length() < 3) throw new IllegalArgumentException("O titulo deve ter pelo menos 3 caracteres");
        return true;
    }

    private boolean autorValido() {
        nullIsBlank(autor, "Autor");
        if (titulo.length() < 2) throw new IllegalArgumentException("O autor deve ter pelo menos 2 caracteres");
        return true;
    }

    private boolean formatoValido() {
        nullIsBlank(formato, "Formato");
        return true;
    }

    private boolean areaValido() {
        nullIsBlank(area, "Area");
        return true;
    }

    private boolean nivelValido() {
        nullIsBlank(nivel.getDisplayName(), "Nível");
        if (nivel != MaterialNivel.BASICO && nivel != MaterialNivel.INTERMEDIARIO && nivel != MaterialNivel.AVANCADO) {
            throw new IllegalArgumentException("Nível inválido. Use: BASICO, INTERMEDIARIO ou AVANCADO");
        }
        return true;
    }

    public void descricaoValida() {
        nullIsBlank(descricao, "Descrição");
    }

    private boolean arquivoValido() {
        if (arquivo == null) throw new IllegalArgumentException("Arquivo é obrigatório");
        if (arquivo.getSize() == 0) throw new IllegalArgumentException("Arquivo está vazio");
        return true;
    }

}
