package entity.material;

import dao.AvaliacaoDAO;
import entity.pessoa.Aluno;
import entity.pessoa.Pessoa;

public class Avaliacao {
    private Integer id;
    private Pessoa pessoa;
    private Material material;
    private Integer nota;
    private String avaliacao;
    private String data;

    public Avaliacao(Pessoa pessoa, Material material, Integer nota, String avaliacao, String data){
        this.pessoa = pessoa;
        this.material = material;
        this.nota = nota;
        this.avaliacao = avaliacao;
        this.data = data;
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


    public Integer getNota() {
        return nota;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public String getData() {
        return data;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public Material getMaterial() {
        return material;
    }
}
