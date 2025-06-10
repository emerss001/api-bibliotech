package entity.material;

import dao.MaterialDAO;
import dto.material.NovoMaterialFisicoDTO;
import entity.catalogo.Catalogo;
import entity.pessoa.Pessoa;
import type.MaterialNivel;

import java.util.ArrayList;

public class MaterialFisico extends Material{
    private Boolean disponibilidade;
    private Integer quantidade;

    // Construtor usando na rota de cração de um novo material
    public MaterialFisico(Pessoa adicionadoPor, NovoMaterialFisicoDTO dto, Catalogo formato, Catalogo area, String capaUrl, String tipo) {
        super(dto.titulo(), dto.autor(), dto.nivel(), dto.descricao(), adicionadoPor, formato, area, tipo, capaUrl);
        this.disponibilidade = true;
        this.quantidade = dto.quantidade();
    }

    // Construtor usado na rota de buscar detalhes de um material
    public MaterialFisico(Integer id, String autor, String tipo, String titulo, MaterialNivel nivel, String descricao, double nota, int quantidadeAvaliacoes, boolean disponibilidade, String capaUrl) {
        super(id, autor, tipo, titulo, nivel, descricao, nota, quantidadeAvaliacoes, capaUrl);
        this.disponibilidade = disponibilidade;
    }

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