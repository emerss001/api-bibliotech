package service;

import dao.MaterialDAO;
import dao.PessoaDAO;
import dto.MateriaisFiltrosDTO;
import dto.NovoMaterialDTO;
import dto.NovoMaterialFisicoDTO;
import model.material.Material;
import model.material.MaterialDigital;
import model.material.MaterialFisico;
import model.pessoa.Pessoa;
import util.TokenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaterialService {
    private final MaterialDAO materialDAO;
    private final PessoaDAO pessoaDAO;
    private final String TIPO_DIGITAL = "Digital";
    private final String TIPO_FISICO = "Fisico";

    public MaterialService(MaterialDAO materialDAO, PessoaDAO pessoaDAO) {
        this.materialDAO = Objects.requireNonNull(materialDAO, "DAO dos materiais não pode ser nulo");
        this.pessoaDAO = Objects.requireNonNull(pessoaDAO, "DAO dos usuários não pode ser nulo");
    }

    public Material addMaterialDigital(NovoMaterialDTO dto, String token) {
        if (dto == null) throw new IllegalArgumentException("Dados do material inválidos");

        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Pessoa adm = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        MaterialDigital materialDigital = new MaterialDigital(adm.getNome(), dto, TIPO_DIGITAL);
        materialDigital.cadastrarMaterialDigital(materialDAO);
        return materialDigital;
    }

    public ArrayList<Integer> addMaterialFisico(NovoMaterialFisicoDTO dto, String token) {
        if (dto == null) throw new IllegalArgumentException("Dados do material inválidos");

        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Pessoa adm = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        MaterialFisico materialFisico = new MaterialFisico(adm.getNome(), dto, TIPO_FISICO);

        return materialFisico.cadastrarMaterialFisico(materialDAO,dto.quantidade());
    }

    public List<Material> buscarTodosMateriais(int limiteInferior, int limiteSuperior, MateriaisFiltrosDTO filtros) {
        int diferenca = limiteSuperior - limiteInferior;
        if (limiteInferior < 0 || limiteSuperior <= 0 || diferenca != 10) throw new IllegalArgumentException("Limites de busca de dados inválidos");

        if (filtros == null) filtros = new MateriaisFiltrosDTO(List.of(), List.of(), List.of(), List.of());

        return materialDAO.buscarTodosMateriais(limiteInferior, limiteSuperior, filtros);
    }

    public Material buscarDetalhesMaterial(int idMaterial) {
            return materialDAO.buscarDetalhesMaterial(idMaterial);
    }
}
