package model.material;

import dao.MaterialDAO;
import dto.NovoMaterialDTO;
import util.FirebaseUpload;

public class MaterialDigital extends Material {
    private String url;

    public MaterialDigital(String adm, NovoMaterialDTO dto, String tipo) {
        super(dto.autor(), tipo, dto.titulo(), dto.formato(), dto.area(), dto.nivel().getDisplayName(), dto.descricao(),adm);
        this.url = FirebaseUpload.upload(dto.arquivo());
    }

    public MaterialDigital(Integer idMaterial, String autor, String tipo, String titulo, Integer formato, Integer area, String nivel, String descricao, String cadastradoPor, double nota, int quantidadeAvaliacoes,String url) {
        super(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes);
        this.url = url;
    }

    public MaterialDigital cadastrarMaterialDigital(MaterialDAO dao) {
        Integer id = salvar(dao);
        return dao.cadastrarMaterialDigital(this, id);
    }

    public String getUrl() {
        return url;
    }
}
