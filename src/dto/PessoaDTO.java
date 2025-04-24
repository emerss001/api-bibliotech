package dto;

import dao.PessoaDAO;
import exception.InvalidDataException;
import type.PessoaVinculo;

public record PessoaDTO(
        String nome,
        String email,
        PessoaVinculo vinculo,
        int idNecessidade,
        String matricula,
        String siap,
        String senha
) {
    // Método de validação
    public boolean valido() {
        return nomeValido() && emailValido() && vinculoValido() && senhaValida();
    }

    private boolean nomeValido() {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("O nome não pode ser vazio");
        if (nome.length() < 3) throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres");
        return true;
    }

    private boolean emailValido() {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("O email não pode ser vazio");
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) throw new IllegalArgumentException("Email inválido");
        if (emailExistente()) throw new IllegalArgumentException("O email já esta cadastrado");
        return true;
    }

    private boolean emailExistente() {
        PessoaDAO pessoaDAO = new PessoaDAO();
        return pessoaDAO.emailExiste(email);
    }

    private boolean vinculoValido() {
        if (vinculo != PessoaVinculo.ALUNO && vinculo != PessoaVinculo.PROFESSOR) throw new InvalidDataException("Vínculo inválido");
        return true;
    }

    private boolean senhaValida() {
        if (senha == null || senha.isBlank()) throw new IllegalArgumentException("Senha não pode ser vazia");
        if (senha.length() < 8) throw new IllegalArgumentException("Senha deve ter pelo menos 8 caracteres");
        return true;
    }

    public boolean dadosEspecificosValidos() {
        return switch (vinculo) {
            case ALUNO -> validarAluno();
            case PROFESSOR -> validarProfessor();
            default -> throw new IllegalStateException("Tipo de vínculo inválido: " + vinculo);
        };
    }

    private boolean validarAluno() {
        if (matricula == null || matricula.isBlank()) throw new IllegalArgumentException("Matrícula não pode ser vazia");
        if (idNecessidade < 1 || idNecessidade > maxIdNecessidade()) throw new IllegalArgumentException("ID de necessidade inválido");
        return true;
    }

    private boolean validarProfessor() {
        if (siap == null || siap.isBlank()) throw new IllegalArgumentException("SIAP não pode ser vazio");
        return true;
    }

    private int maxIdNecessidade() {
        PessoaDAO pessoaDAO = new PessoaDAO();
        return pessoaDAO.getMaxIdNecessidade();
    }
}