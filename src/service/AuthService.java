package service;

import dao.PessoaDAO;
import dto.LoginDTO;
import dto.PessoaDTO;
import model.pessoa.Aluno;
import model.pessoa.Bibliotecario;
import model.pessoa.Pessoa;
import model.pessoa.Professor;
import util.TokenUtil;

public class AuthService {
    private final PessoaDAO pessoaDAO;

    public AuthService(PessoaDAO pessoaDAO) {
        this.pessoaDAO = pessoaDAO;
    }

    public String autenticar(LoginDTO dto) {
        dto.valido();
        Pessoa pessoa = pessoaDAO.buscarPorIdentificador(dto.vinculo(), dto.identificador());

        if (pessoa == null || !pessoa.validarSenha(dto.senha())) throw new IllegalArgumentException("Identificador ou senha inválidos");
        return TokenUtil.gerarToken(pessoa.getEmail());
    }

    public Pessoa cadastro(PessoaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados da pessoa inválidos");
        }

        if (!dto.valido()) {
            throw new IllegalArgumentException("Dados obrigatórios não informados");
        }
        if (!dto.dadosEspecificosValidos()) {
            throw new IllegalArgumentException("Dados específicos inválidos para o vínculo");
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

            case BIBLIOTECARIO:
                Bibliotecario bibliotecario = new Bibliotecario(dto, dto.codigo(), false);
                bibliotecario.cadastrarBibliotecario(pessoaDAO);
                return bibliotecario;

            default:
                throw new IllegalArgumentException("Tipo de vínculo inválido" + dto.vinculo());
        }
    }
}
