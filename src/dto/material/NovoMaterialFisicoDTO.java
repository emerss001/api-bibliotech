package dto.material;

import type.MaterialNivel;

import javax.servlet.http.Part;

public record NovoMaterialFisicoDTO (
        String titulo,
        String autor,
        Integer formato,
        Integer area,
        MaterialNivel nivel,
        String descricao,
        Integer quantidade,
        Part capa
) {
    public boolean valido() {
        return tituloValido() && autorValido() && formatoValido() && areaValido() && nivelValido() && descricaoValida() && quantidadeValido();
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
        nullIsBlank(formato.toString(), "Formato");
        return true;
    }

    private boolean areaValido() {
        nullIsBlank(area.toString(), "Area");
        return true;
    }

    private boolean nivelValido() {
        nullIsBlank(nivel.getDisplayName(), "Nível");
        if (nivel != MaterialNivel.BASICO && nivel != MaterialNivel.INTERMEDIARIO && nivel != MaterialNivel.AVANCADO) {
            throw new IllegalArgumentException("Nível inválido. Use: BASICO, INTERMEDIARIO ou AVANCADO");
        }
        return true;
    }

    public boolean descricaoValida() {
        nullIsBlank(descricao, "Descrição");
        return true;
    }

    private boolean quantidadeValido(){
        nullIsBlank(quantidade.toString(), "Quantidade");
        return quantidade > 0;
    }
}
