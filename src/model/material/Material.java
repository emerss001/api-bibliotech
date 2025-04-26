package model.material;

import dao.MaterialDAO;

import java.sql.Timestamp;

public class Material {
    private Integer id;
    private String titulo;
    private String formato;
    private String area;
    private String nivel;
    private double nota;
    private Timestamp adicionado;
    private int quantidadeAvaliacoes;
    private String descricao;

    public Material(String titulo, String formato, String area, String nivel, String descricao) {
        this.id = null;
        this.titulo = titulo;
        this.formato = formato;
        this.area = area;
        this.nivel = nivel;
        this.descricao = descricao;
    }

    public Integer salvar(MaterialDAO dao) {
        return dao.addMaterial(this);
    }

    public String getFormato() {
        return formato;
    }

    public String getArea() {
        return area;
    }

    public String getNivel() {
        return nivel;
    }

    public String getDescricao() {
        return descricao;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
