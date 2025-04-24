package service;

import dao.PessoaDAO;
import dto.PessoaDTO;
import model.pessoa.Aluno;
import model.pessoa.Pessoa;
import model.pessoa.Professor;

import java.util.Objects;

public class PessoaService {
    private final PessoaDAO pessoaDAO;

    // Injeção de dependência
    public PessoaService(PessoaDAO pessoaDAO) {
        this.pessoaDAO = Objects.requireNonNull(pessoaDAO, "DAO não pode ser nulo");
    }

    public Pessoa cadastrarPessoa(PessoaDTO dto) {
        return switch (dto.vinculo()) {
            case ALUNO -> cadastrarAluno(dto);
            case PROFESSOR -> cadastrarProfessor(dto);
            default -> throw new IllegalArgumentException("Tipo de vínculo inválido" + dto.vinculo());
        };
    }

    private Professor cadastrarProfessor(PessoaDTO dto) {
        dto.dadosEspecificosValidos();
        Professor professor = new Professor(dto.nome(), dto.email(), dto.siap(), dto.senha());
        return pessoaDAO.addProfessor(professor);
    }

    private Aluno cadastrarAluno(PessoaDTO dto) {
        dto.dadosEspecificosValidos();
        Aluno aluno = new Aluno(dto.nome(), dto.email(), dto.matricula(), dto.senha(), dto.idNecessidade());
        return pessoaDAO.addAluno(aluno);
    }
}
