package service;

import dao.PessoaDAO;
import dto.LoginDTO;
import exception.InvalidDataException;
import model.pessoa.Pessoa;
import util.TokenUtil;

public class AuthService {
    private final PessoaDAO pessoaDAO;

    public AuthService(PessoaDAO pessoaDAO) {
        this.pessoaDAO = pessoaDAO;
    }

    public String autenticar(LoginDTO dto) {
        Pessoa pessoa = pessoaDAO.buscarPorEmail(dto.email());

        if (pessoa == null || !pessoa.validarSenha(dto.senha())) throw new InvalidDataException("Email ou senha inválidos");
        return TokenUtil.gerarToken(pessoa.getEmail());
    }
}
