package service;

import dao.PessoaDAO;
import dto.PessoaDTO;
import exception.InvalidDataException;
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
        if (dto == null) {
            throw new IllegalArgumentException("Dados da pessoa inválidos");
        }

        if (!dto.valido()) {
            throw new InvalidDataException("Dados obrigatórios não informados");
        }
        if (!dto.dadosEspecificosValidos()) {
            throw new InvalidDataException("Dados específicos inválidos para o vínculo");
        }

         switch (dto.vinculo()) {
            case ALUNO:
                Aluno aluno = new Aluno(dto, dto.matricula(), dto.idNecessidade(), false);
                aluno.cadastrarAluno(pessoaDAO);
                return aluno;

            case PROFESSOR:
                Professor professor = new Professor(dto, dto.siap(), false);
                professor.cadastrarProfessor(pessoaDAO);
                return professor;

            default:
                throw new IllegalArgumentException("Tipo de vínculo inválido" + dto.vinculo());
        }
    }
}
