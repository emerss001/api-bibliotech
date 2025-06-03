package entity.material;

import dao.MaterialDAO;
import dto.material.NovoMaterialDTO;
import entity.catalogo.Catalogo;
import entity.pessoa.Pessoa;
import type.MaterialNivel;
import util.FirebaseUpload;

public class MaterialDigital extends Material {
    private String url;

    // Usado na rota de criação de um novo material
    public MaterialDigital(Pessoa adicionadoPor, NovoMaterialDTO dto, Catalogo formato, Catalogo area, String url, String capaUrl, String tipo) {
        super(dto.titulo(), dto.autor(), dto.nivel(), dto.descricao(), adicionadoPor, formato, area, tipo, capaUrl);
        this.url = url;
    }

    // Usado na rota que busca os detalhes de um material
    public MaterialDigital(Integer id, String autor, String tipo, String titulo, MaterialNivel nivel, String descricao, double nota, int quantidadeAvaliacoes, String url, String capaUrl) {
        super(id, autor, tipo, titulo, nivel, descricao, nota, quantidadeAvaliacoes, capaUrl);
        this.url = url;
    }


    public MaterialDigital cadastrarMaterialDigital(MaterialDAO dao) {
        if (this.url == null) return null;
        Integer id = salvar(dao);
        return dao.cadastrarMaterialDigital(this, id);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
