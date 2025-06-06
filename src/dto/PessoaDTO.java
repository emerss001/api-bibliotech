package dto;

import dao.PessoaDAO;
import exception.DataConflictException;
import type.PessoaVinculo;

public record PessoaDTO(
        String nome,
        String email,
        PessoaVinculo vinculo,
        int idNecessidade,
        String matricula,
        String siap,
        String codigo,
        String senha
) {
    // Método de validação
    private static final PessoaDAO pessoaDAO = new PessoaDAO();
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
        if (pessoaDAO.emailExiste(email)) throw new DataConflictException("O email já esta cadastrado");
        return true;
    }

    boolean vinculoValido() {
        if (vinculo != PessoaVinculo.ALUNO && vinculo != PessoaVinculo.PROFESSOR && vinculo != PessoaVinculo.BIBLIOTECARIO) throw new IllegalArgumentException("Vínculo inválido");
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
            case BIBLIOTECARIO -> validarBibliotecario();
            default -> throw new IllegalStateException("Tipo de vínculo inválido: " + vinculo);
        };
    }

    private boolean validarAluno() {
        if (matricula == null || matricula.isBlank()) throw new IllegalArgumentException("Matrícula não pode ser vazia");
        if (idNecessidade < 1 || idNecessidade > maxIdNecessidade()) throw new IllegalArgumentException("ID de necessidade inválido");
        if (pessoaDAO.matriculaExiste(matricula)) throw new DataConflictException("A matricula já está cadastrada");
        return true;
    }

    private boolean validarProfessor() {
        if (siap == null || siap.isBlank()) throw new IllegalArgumentException("SIAP não pode ser vazio");
        if (pessoaDAO.siapExiste(siap)) throw new DataConflictException("SIAP já cadastrado");
        return true;
    }

    private boolean validarBibliotecario() {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código não pode ser vazio");
        return true;
    }


    private int maxIdNecessidade() {
        PessoaDAO pessoaDAO = new PessoaDAO();
        return pessoaDAO.getMaxIdNecessidade();
    }
}