package service;

import dao.BibliotecarioDAO;
import dto.bibliotecario.CadastrosPendentesDTO;
import dto.bibliotecario.EmprestimosPendentesDTO;
import dto.bibliotecario.MetricasDTO;
import dto.bibliotecario.MetricasMaterias;
import model.material.Material;

import java.util.List;
import java.util.Map;

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

    public MetricasMaterias buscarMetricasMateriais() {
        return bibliotecarioDAO.buscarMetricasMateriais();
    }

    public List<Material> buscarMateriais() {
        return bibliotecarioDAO.buscarMateriais();
    }

    public void excluirMaterialAdmin(Integer id) {
        if (id == null) throw new IllegalArgumentException("id obrigatório");
        if (id <= 0) throw new IllegalArgumentException("id inválido");

        bibliotecarioDAO.deletarMaterial(id);
    }

    public void ocultarMaterial(Integer id) {
        if (id == null) throw new IllegalArgumentException("id obrigatório");
        if (id <= 0) throw new IllegalArgumentException("id inválido");

        bibliotecarioDAO.ocultarMaterial(id);
    }

    public void listarMaterial(Integer id) {
        if (id == null) throw new IllegalArgumentException("id obrigatório");
        if (id <= 0) throw new IllegalArgumentException("id inválido");

        bibliotecarioDAO.listarMaterial(id);
    }
}
