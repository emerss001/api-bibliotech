package service;

import com.google.gson.JsonObject;
import dao.EmprestimoDAO;
import dao.PessoaDAO;
import dto.NovoEmprestimoDTO;
import model.material.Emprestimo;
import model.pessoa.Pessoa;
import util.TokenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmprestimoService {
    private final EmprestimoDAO emprestimoDAO;
    private final PessoaDAO pessoaDAO;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, PessoaDAO pessoaDAO) {
        this.emprestimoDAO = Objects.requireNonNull(emprestimoDAO, "DAO dos empréstimos não pode ser nulo");
        this.pessoaDAO = Objects.requireNonNull(pessoaDAO, "DAO dos usuários não pode ser nulo");
    }

    public Emprestimo addEmprestimo(NovoEmprestimoDTO dto){
        if (dto == null) throw new IllegalArgumentException("Dados do empréstimo inválidos");

        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Emprestimo emprestimo = new Emprestimo(dto);
        emprestimo.setId(emprestimo.salvar(emprestimoDAO));
        return emprestimo;
    }

    public void updateEmprestimo(NovoEmprestimoDTO dto){
        if (dto == null) throw new IllegalArgumentException("Dados do empréstimo inválidos");

        //if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Emprestimo emprestimo = new Emprestimo(dto);

        switch (dto.status()){
            case APROVADO -> emprestimoDAO.aprooveEmprestimo(emprestimo);
            case REJEITADO -> emprestimoDAO.refuseEmprestimo(emprestimo);
            case RENOVADO -> emprestimoDAO.renovateEmprestimo(emprestimo);
            case DEVOLVIDO -> emprestimoDAO.returnEmprestimo(emprestimo);
        }
    }

    public void deleteEmprestimo(NovoEmprestimoDTO dto){
        if (dto == null) throw new IllegalArgumentException("Dados do empréstimo inválidos");

        //if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Emprestimo emprestimo = new Emprestimo(dto);
        emprestimoDAO.deleteEmprestimo(emprestimo);
    }


    public ArrayList<Emprestimo> listEmprestimo(JsonObject json){
        String quantidade = json.get("quantidade").getAsString() != null ? json.get("quantidade").getAsString() : "*";
        String status = json.get("status").getAsString();
        String alunoId = json.get("alunoId").getAsString();

        System.out.println(quantidade+"\n"+status+"\n"+alunoId);

        return emprestimoDAO.readEmprestimo(quantidade,status,alunoId);
    }

    public Integer tokenTOId(String token){
        Pessoa aluno = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));

        return aluno.getId();
    }
}
