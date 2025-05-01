package dto;

import type.PessoaVinculo;

public record LoginDTO(PessoaVinculo vinculo, String identificador, String senha) {
    // Método de validação

    public boolean valido() {
        return vinculoValido() && identificadorlValido() && senhaValida();
    }

    private boolean vinculoValido() {
        if (vinculo != PessoaVinculo.ALUNO && vinculo != PessoaVinculo.PROFESSOR) throw new IllegalArgumentException("Vínculo inválido");
        return true;
    }

    private boolean identificadorlValido() {
        if (identificador == null || identificador.isBlank()) throw new IllegalArgumentException("O identificador não pode ser vazio");
        return true;
    }

    private boolean senhaValida() {
        if (senha == null || senha.isBlank()) throw new IllegalArgumentException("Senha não pode ser vazia");
        if (senha.length() < 8) throw new IllegalArgumentException("Senha deve ter pelo menos 8 caracteres");
        return true;
    }
}
