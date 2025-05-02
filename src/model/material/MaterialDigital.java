package model.material;

import dao.MaterialDAO;
import dto.NovoMaterialDTO;
import util.FirebaseUpload;

public class MaterialDigital extends Material {
    private String url;

    public MaterialDigital(String adm, NovoMaterialDTO dto) {
        super(dto.autor(), dto.titulo(), dto.formato(), dto.area(), String.valueOf(dto.nivel()), dto.descricao(),adm);
        this.url = FirebaseUpload.upload(dto.arquivo());
    }

    public MaterialDigital cadastrarMaterialDigital(MaterialDAO dao) {
        Integer id = salvar(dao);
        return dao.cadastrarMaterialDigital(this, id);
    }

    public String getUrl() {
        return url;
    }
}
