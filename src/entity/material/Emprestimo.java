package entity.material;

import dao.EmprestimoDAO;
import entity.pessoa.Aluno;
import entity.pessoa.Pessoa;
import type.EmprestimoStatus;

import java.sql.Date;

public class Emprestimo {
    private Integer id;
    private Aluno aluno;
    private Material material;
    private Date dataCriacao;
    private Date dataAprovacao;
    private Date dataDevolucaoPrevista;
    private Date dataDevolucaoReal;
    private EmprestimoStatus status;
    private String mensagem;
    private String rejicaoMotivo;

    public String getMensagem() {
        return mensagem;
    }

    public Emprestimo() {}

    public Emprestimo(Material material, Aluno aluno, String mensagem) {
        this.aluno = aluno;
        this.material = material;
        this.mensagem = mensagem;
    }

    public Emprestimo(Integer id, Aluno aluno, Material material, Date dataCriacao, Date dataAprovacao, Date dataDevolucaoPrevista, Date dataDevolucaoReal, String status, String rejicaoMotivo){
        this.id = id;
        this.aluno = aluno;
        this.material = material;
        this.dataCriacao = dataCriacao;
        this.dataAprovacao = dataAprovacao;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.status = EmprestimoStatus.fromString(status);
        this.rejicaoMotivo = rejicaoMotivo;
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

    public Pessoa getAluno() {
        return aluno;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getRejicaoMotivo() {
        return rejicaoMotivo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(Date dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public Date getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Date getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(Date dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public EmprestimoStatus getStatus() {
        return status;
    }

    public void setStatus(EmprestimoStatus status) {
        this.status = status;
    }
}