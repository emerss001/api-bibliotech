package model.material;

import dao.EmprestimoDAO;
import dto.NovoEmprestimoDTO;
import type.EmprestimoStatus;

public class Emprestimo {
    private Integer id;
    private Integer alunoId;
    private Integer materialId;
    private String dataEmprestimo;
    private String dataDPrevista;
    private String dataDReal;
    private EmprestimoStatus status;

    public Emprestimo(Integer alunoId, Integer materialId, Integer id, String dataEmprestimo, String dataDPrevista, String dataDReal, String status){
        this.materialId = materialId;
        this.alunoId = alunoId;
        this.id = id;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDPrevista = dataDPrevista;
        this.dataDReal = dataDReal;
        this.status = EmprestimoStatus.fromString(status);
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

    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getDataDPrevista() {
        return dataDPrevista;
    }

    public String getDataDReal() {
        return dataDReal;
    }

    public EmprestimoStatus getStatus() {
        return status;
    }
}