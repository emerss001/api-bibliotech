package service;

import dao.BibliotecarioDAO;
import dto.bibliotecario.*;
import entity.material.Material;

import java.time.LocalDate;
import java.util.List;

public class BibliotecarioService {
    private final BibliotecarioDAO bibliotecarioDAO;

    public BibliotecarioService(BibliotecarioDAO bibliotecarioDAO) {
        this.bibliotecarioDAO = bibliotecarioDAO;
    }

    public List<EmprestimosPendentesDTO> bucarEmprestimosPendentes() {
        return bibliotecarioDAO.buscarEmprestimos();
    }

    public List<EmprestimosPendentesDTO> buscarEmprestimosAndamento() {
        return bibliotecarioDAO.buscarEmprestimosAndamento();
    }

    public void devolverEmprestimo(Integer emprestimoId) {
        if (emprestimoId == null || emprestimoId <= 0) throw new IllegalArgumentException("O id não pode ser vazio");

        bibliotecarioDAO.returnEmprestimo(emprestimoId);
    }

    public void renovarEmprestimo(Integer emprestimoId, LocalDate dataDevolucao) {
        if (emprestimoId == null || emprestimoId <= 0) throw new IllegalArgumentException("O id não pode ser vazio");
        if (LocalDate.now().isAfter(dataDevolucao)) throw new IllegalArgumentException("A data não pode estar no passado");

        bibliotecarioDAO.renovarEmprestimo(emprestimoId, dataDevolucao);
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

    public MetricasAlunos buscarMetricasAlunos() {
        return bibliotecarioDAO.buscarMetricasAlunos();
    }

    public List<AlunosCadastradosDTO> buscarALunos() {
        return bibliotecarioDAO.buscarAlunos();
    }

    public void suspenderALuno(Integer id) {
        if (id == null) throw new IllegalArgumentException("id obrigatório");
        if (id <= 0) throw new IllegalArgumentException("id inválido");

        bibliotecarioDAO.suspenderALuno(id);
    }

    public void ativarALuno(Integer id) {
        if (id == null) throw new IllegalArgumentException("id obrigatório");
        if (id <= 0) throw new IllegalArgumentException("id inválido");

        bibliotecarioDAO.ativarALuno(id);
    }
}
