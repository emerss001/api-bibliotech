package model.material;

import dao.MaterialDAO;
import dto.material.NovoMaterialDTO;
import dto.material.NovoMaterialFisicoDTO;
import model.catalogo.Catalogo;
import model.pessoa.Pessoa;
import type.MaterialNivel;
import util.FirebaseUpload;

import java.util.ArrayList;

public class MaterialFisico extends Material{
    private Boolean disponibilidade;
    private Integer quantidade;

    // Construtor usando na rota de cração de um novo material
    public MaterialFisico(Pessoa adicionadoPor, NovoMaterialFisicoDTO dto, Catalogo formato, Catalogo area, String tipo) {
        super(dto.titulo(), dto.autor(), dto.nivel(), dto.descricao(), adicionadoPor, formato, area, tipo);
        this.disponibilidade = true;
        this.quantidade = dto.quantidade();
    }

    // Construtor usado na rota de buscar detalhes de um material
    public MaterialFisico(Integer id, String autor, String tipo, String titulo, MaterialNivel nivel, String descricao, double nota, int quantidadeAvaliacoes, boolean disponibilidade) {
        super(id, autor, tipo, titulo, nivel, descricao, nota, quantidadeAvaliacoes);
        this.disponibilidade = disponibilidade;
    }


//    public MaterialFisico(Integer idMaterial, String autor, String tipo, String titulo, Integer formato, Integer area, String nivel, String descricao, String cadastradoPor, double nota, int quantidadeAvaliacoes, Boolean disponibilidade) {
//        super(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes);
//        this.disponibilidade = disponibilidade;
//    }

    public ArrayList<Integer> cadastrarMaterialFisico(MaterialDAO dao) {
        ArrayList<Integer> lista = new ArrayList<>();
        Integer id = this.salvar(dao);

        for (int i = 0; i < this.getQuantidade(); i++) {
            this.setId(id);
            dao.cadastrarMaterialFisico(this, id);
            lista.add(id);
        }

        return lista;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}