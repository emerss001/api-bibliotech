package model.material;

import dao.MaterialDAO;
import dto.NovoMaterialFisicoDTO;

public class MaterialFisico extends Material{
    private Integer quantidade;

    public MaterialFisico(String adm, NovoMaterialFisicoDTO dto, String tipo) {
        super(dto.autor(), tipo, dto.titulo(), dto.formato(), dto.area(), String.valueOf(dto.nivel()), dto.descricao(),adm);
        this.quantidade = dto.quantidade();
    }

    public MaterialFisico cadastrarMaterialFisico(MaterialDAO dao) {
        Integer id = salvar(dao);
        return dao.cadastrarMaterialFisico(this, id);
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}