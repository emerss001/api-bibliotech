package service;

import dao.EmprestimoDAO;
import dao.MaterialDAO;
import dao.PessoaDAO;
import entity.material.Emprestimo;
import entity.material.Material;
import entity.pessoa.Aluno;
import entity.pessoa.Pessoa;
import util.TokenUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class EmprestimoService {
    private final EmprestimoDAO emprestimoDAO;
    private final PessoaDAO pessoaDAO;
    private final MaterialDAO materialDAO;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, PessoaDAO pessoaDAO, MaterialDAO materialDAO) {
        this.emprestimoDAO = Objects.requireNonNull(emprestimoDAO, "DAO dos empréstimos não pode ser nulo");
        this.pessoaDAO = Objects.requireNonNull(pessoaDAO, "DAO dos usuários não pode ser nulo");
        this.materialDAO = Objects.requireNonNull(materialDAO, "DAO dos materiais não pode ser nulo");
    }

    public Emprestimo addEmprestimo(String token, Integer materialId, String mensagem){
        if (materialId == null) throw new IllegalArgumentException("id do material não pode ser nulo");
        if (mensagem != null) {
            if (mensagem.length() > 255) throw new IllegalArgumentException("Mensagem deve ter no máximo 255 caracteres");

        }

        Pessoa pessoa = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        if (pessoaDAO.alunoSuspenso(pessoa.getId())) throw new RuntimeException("Aluo suspenso, proibido de fazer novos empréstimos");
        Aluno aluno = new Aluno(pessoa.getId());

        Integer idMaterialDisponivel = materialDAO.getDisponibilidade(materialId);
        if (idMaterialDisponivel == null || idMaterialDisponivel < 0) throw new IllegalArgumentException("Máterial não tem disponibilidade");

        Material material = new Material();
        material.setId(idMaterialDisponivel);

        Emprestimo emprestimo = new Emprestimo(material, aluno, mensagem);
        emprestimo.setId(emprestimo.salvar(emprestimoDAO));
        return emprestimo;
    }

    public void aprovarEmprestimo(Integer emprestimoId, LocalDate dataDevolucao) {
        if (emprestimoId == null || emprestimoId <= 0) throw new IllegalArgumentException("O id não pode ser vazio");
        if (LocalDate.now().isAfter(dataDevolucao)) throw new IllegalArgumentException("A data não pode estar no passado");

        emprestimoDAO.aprooveEmprestimo(emprestimoId, dataDevolucao);
    }

    public void rejeitarEmprestimo(Integer emprestimoId, String mensagem) {
        if (emprestimoId == null || emprestimoId <= 0) throw new IllegalArgumentException("O id não pode ser vazio");
        if (mensagem.length() > 255) throw new IllegalArgumentException("A mensagem não pode ter mais de 255 caracteres");

        emprestimoDAO.refuseEmprestimo(emprestimoId, mensagem);
    }

    public void deleteEmprestimo(Integer id){
        if (id == null || id < 1) throw new IllegalArgumentException("Id inválido");

        emprestimoDAO.deleteEmprestimo(id);
    }

    public List<Emprestimo> listarEmprestimosAluno(String token) {
        Pessoa aluno = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        return emprestimoDAO.getEmprestimosByAluno(aluno);
    }

    public Integer tokenTOId(String token){
        Pessoa aluno = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));

        return aluno.getId();
    }
}
