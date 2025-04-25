package service;

import dao.PessoaDAO;
import dto.LoginDTO;
import model.pessoa.Pessoa;
import util.TokenUtil;

public class AuthService {
    private final PessoaDAO pessoaDAO;

    public AuthService(PessoaDAO pessoaDAO) {
        this.pessoaDAO = pessoaDAO;
    }

    public String autenticar(LoginDTO dto) {
        dto.valido();
        Pessoa pessoa = pessoaDAO.buscarPorEmail(dto.email());

        if (pessoa == null || !pessoa.validarSenha(dto.senha())) throw new IllegalArgumentException("Email ou senha inválidos");
        return TokenUtil.gerarToken(pessoa.getEmail());
    }
}
