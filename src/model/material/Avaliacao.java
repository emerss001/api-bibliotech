package model.material;

import dao.AvaliacaoDAO;
import dto.AvaliacaoDTO;

public class Avaliacao {
    private Integer id;
    private Integer alunoId;
    private Integer materialId;
    private Integer nota;
    private String avaliacao;
    private String data;

    public Avaliacao(Integer id, Integer alunoId, Integer materialId, Integer nota, String avaliacao, String data){
        this.id = id;
        this.alunoId = alunoId;
        this.materialId = materialId;
        this.nota = nota;
        this.avaliacao = avaliacao;
        this.data = data;
    }

    public Avaliacao(AvaliacaoDTO dto){
        this.id = dto.id();
        this.alunoId = dto.alunoId();
        this.materialId = dto.materialId();
        this.nota = dto.nota();
        this.avaliacao = dto.avaliacao();
        this.data = dto.data();
    }

    public Integer salvar(AvaliacaoDAO dao) {
        return dao.addAvaliacao(this);
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

    public Integer getNota() {
        return nota;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public String getData() {
        return data;
    }
}
