package model.material;

import dao.MaterialDAO;
import dto.NovoMaterialFisicoDTO;

public class MaterialFisico extends Material{
    private Integer quantidade;

    public MaterialFisico(String autor, NovoMaterialFisicoDTO dto) {
        super(autor, dto.titulo(), dto.formato(), dto.area(), String.valueOf(dto.nivel()), dto.descricao());
        this.quantidade = 0;
    }

    public MaterialFisico cadastrarMaterialFisico(MaterialDAO dao) {
        Integer id = salvar(dao);
        return dao.cadastrarMaterialFisico(this, id);
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}