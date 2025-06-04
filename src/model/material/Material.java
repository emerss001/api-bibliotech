package model.material;

import dao.MaterialDAO;
import model.catalogo.Catalogo;
import model.pessoa.Pessoa;
import type.MaterialNivel;

import java.sql.Timestamp;

public class Material {
    private Integer id;
    private String tipo;
    private String autor;
    private String titulo;
    private Catalogo formato;
    private Catalogo area;
    private MaterialNivel nivel;
    private double nota;
    private Timestamp adicionado;
    private int quantidadeAvaliacoes;
    private String descricao;
    private Pessoa cadastradoPor;

    public Material() {

    }

    // construtor usado na criação de um novo material
    public Material(String titulo, String autor, MaterialNivel nivel, String descricao, Pessoa cadastradoPor, Catalogo formato, Catalogo area, String tipo) {
        this.titulo = titulo;
        this.autor = autor;
        this.nivel = nivel;
        this.descricao = descricao;
        this.cadastradoPor = cadastradoPor;
        this.formato = formato;
        this.area = area;
        this.tipo = tipo;
    }

    // Construtor usado na busca por detalhes de uma material
    public Material(Integer id, String autor, String tipo, String titulo, MaterialNivel nivel, String descricao, double nota, int quantidadeAvaliacoes) {
        this.id = id;
        this.autor = autor;
        this.tipo = tipo;
        this.titulo = titulo;
        this.nivel = nivel;
        this.descricao = descricao;
        this.nota = nota;
        this.quantidadeAvaliacoes = quantidadeAvaliacoes;
    }


    public Integer salvar(MaterialDAO dao) {
        return dao.addMaterial(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Catalogo getFormato() {
        return formato;
    }

    public void setFormato(Catalogo formato) {
        this.formato = formato;
    }

    public Catalogo getArea() {
        return area;
    }

    public void setArea(Catalogo area) {
        this.area = area;
    }

    public String getNivel() {
        return nivel.getDisplayName();
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public Timestamp getAdicionado() {
        return adicionado;
    }

    public void setAdicionado(Timestamp adicionado) {
        this.adicionado = adicionado;
    }

    public int getQuantidadeAvaliacoes() {
        return quantidadeAvaliacoes;
    }

    public void setQuantidadeAvaliacoes(int quantidadeAvaliacoes) {
        this.quantidadeAvaliacoes = quantidadeAvaliacoes;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pessoa getCadastradoPor() {
        return cadastradoPor;
    }

    public void setCadastradoPor(Pessoa cadastradoPor) {
        this.cadastradoPor = cadastradoPor;
    }
}
