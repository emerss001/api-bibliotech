package model.material;

import dao.EmprestimoDAO;
import dto.NovoEmprestimoDTO;
import type.EmprestimoStatus;

import java.sql.Timestamp;

public class Emprestimo {
    private Integer id;
    private Integer alunoId;
    private Integer materialId;
    private Timestamp dataEmprestimo;
    private String dataDPrevista;
    private Timestamp dataDReal;
    private EmprestimoStatus status;

    public Emprestimo(Integer alunoId, Integer materialId){
        this.materialId = materialId;
        this.alunoId = alunoId;
    }

    public Emprestimo(NovoEmprestimoDTO dto){
        this.id = dto.id();
        this.dataDPrevista = dto.dataDevolucaoPrevista();
        this.status = dto.status();
    }

    public Integer salvar(EmprestimoDAO dao) {
        return dao.addEmprestimo(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public Timestamp getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getDataDPrevista() {
        return dataDPrevista;
    }

    public Timestamp getDataDReal() {
        return dataDReal;
    }

    public EmprestimoStatus getStatus() {
        return status;
    }
}