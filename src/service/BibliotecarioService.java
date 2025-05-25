package service;

import dao.BibliotecarioDAO;
import dto.bibliotecario.CadastrosPendentesDTO;
import dto.bibliotecario.EmprestimosPendentesDTO;
import dto.bibliotecario.MetricasDTO;

import java.util.List;

public class BibliotecarioService {
    private final BibliotecarioDAO bibliotecarioDAO;

    public BibliotecarioService(BibliotecarioDAO bibliotecarioDAO) {
        this.bibliotecarioDAO = bibliotecarioDAO;
    }

    public List<EmprestimosPendentesDTO> bucarEmprestimosPendentes() {
        return bibliotecarioDAO.buscarEmprestimos();
    }

    public List<CadastrosPendentesDTO> buscarCadastrosPendentes() {
        return bibliotecarioDAO.buscarCadastros();
    }

    public MetricasDTO buscarMetricas() {
        return bibliotecarioDAO.buscarMetricas();
    }

    public boolean aprovarCadastro(Integer idPessoa) {
        if (idPessoa == null || idPessoa <= 0) throw new RuntimeException("Id do cadastro inválido");

        return bibliotecarioDAO.aprovarCadastro(idPessoa);
    }

    public boolean rejeitarCadastro(Integer idPessoa) {
        if (idPessoa == null || idPessoa <= 0) throw new RuntimeException("Id do cadastro inválido");

        return bibliotecarioDAO.rejeitarCadastro(idPessoa);
    }
}
