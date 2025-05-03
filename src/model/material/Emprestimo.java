package model.material;

import dao.EmprestimoDAO;

import java.sql.Timestamp;

public class Emprestimo {
    private Integer id;
    private Integer alunoId;
    private Integer materialId;
    private Timestamp dataEmprestimo;
    private Timestamp dataDPrevista;
    private Timestamp dataDReal;
    private Boolean devolvido;
    private Boolean renovado;
    private String status;

    public Emprestimo(Integer alunoId, Integer materialId){
        this.materialId = materialId;
        this.alunoId = alunoId;
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

    public Timestamp getDataDPrevista() {
        return dataDPrevista;
    }

    public Timestamp getDataDReal() {
        return dataDReal;
    }

    public Boolean getDevolvido() {
        return devolvido;
    }

    public Boolean getRenovado() {
        return renovado;
    }

    public String getStatus() {
        return status;
    }
}