package model.material;

import dao.MaterialDAO;
import dto.NovoMaterialFisicoDTO;

public class MaterialFisico extends Material{
    private Integer quantidade;

    public MaterialFisico(String adm, NovoMaterialFisicoDTO dto, String tipo) {
        super(dto.autor(), tipo, dto.titulo(), dto.formato(), dto.area(), dto.descricao(), String.valueOf(dto.nivel()), adm);
        this.quantidade = dto.quantidade();
    }

    public MaterialFisico(Integer idMaterial, String autor, String tipo, String titulo, String formato, String area, String nivel, String descricao, String cadastradoPor, double nota, int quantidadeAvaliacoes, Integer quantidade) {
        super(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes);
        this.quantidade = quantidade;
    }

    public MaterialFisico cadastrarMaterialFisico(MaterialDAO dao) {
        Integer id = salvar(dao);
        return dao.cadastrarMaterialFisico(this, id);
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}