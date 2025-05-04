package model.material;

import dao.MaterialDAO;
import dto.NovoMaterialFisicoDTO;

public class MaterialFisico extends Material{
    private Boolean disponibilidade;

    public MaterialFisico(String adm, NovoMaterialFisicoDTO dto, String tipo) {
        super(dto.autor(), tipo, dto.titulo(), dto.formato(), dto.area(), dto.descricao(), String.valueOf(dto.nivel()), adm);
        this.disponibilidade = true;
    }

    public MaterialFisico(Integer idMaterial, String autor, String tipo, String titulo, String formato, String area, String nivel, String descricao, String cadastradoPor, double nota, int quantidadeAvaliacoes, Boolean disponibilidade) {
        super(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes);
        this.disponibilidade = disponibilidade;
    }

    public MaterialFisico cadastrarMaterialFisico(MaterialDAO dao) {
        Integer id = salvar(dao);
        return dao.cadastrarMaterialFisico(this, id);
    }

    public Boolean getQuantidade() {
        return disponibilidade;
    }
}