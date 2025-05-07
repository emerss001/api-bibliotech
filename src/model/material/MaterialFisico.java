package model.material;

import dao.MaterialDAO;
import dto.NovoMaterialFisicoDTO;

import java.util.ArrayList;

public class MaterialFisico extends Material{
    private Boolean disponibilidade;
    private Integer quantidade;

    public MaterialFisico(String adm, NovoMaterialFisicoDTO dto, String tipo) {
        super(dto.autor(), tipo, dto.titulo(), dto.formato(), dto.area(), dto.descricao(), dto.nivel().getDisplayName(), adm);
        this.disponibilidade = true;
        this.quantidade = dto.quantidade();
    }

    public MaterialFisico(Integer idMaterial, String autor, String tipo, String titulo, Integer formato, Integer area, String nivel, String descricao, String cadastradoPor, double nota, int quantidadeAvaliacoes, Boolean disponibilidade) {
        super(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes);
        this.disponibilidade = disponibilidade;
    }

    public ArrayList<Integer> cadastrarMaterialFisico(MaterialDAO dao, Integer quantidade) {
        ArrayList<Integer> lista = new ArrayList<>();

        for (int i = 0; i < this.getQuantidade(); i++) {
            Integer id = this.salvar(dao);

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