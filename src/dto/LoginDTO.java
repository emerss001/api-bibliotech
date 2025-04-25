package dto;

public record LoginDTO(String email, String senha) {
    // Método de validação

    public boolean valido() {
        return emailValido() && senhaValida();
    }

        private boolean emailValido() {
            if (email == null || email.isBlank()) throw new IllegalArgumentException("O email não pode ser vazio");
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) throw new IllegalArgumentException("Email inválido");
            return true;
        }

    private boolean senhaValida() {
        if (senha == null || senha.isBlank()) throw new IllegalArgumentException("Senha não pode ser vazia");
        if (senha.length() < 8) throw new IllegalArgumentException("Senha deve ter pelo menos 8 caracteres");
        return true;
    }
}
