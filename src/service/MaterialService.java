package service;

import dao.MaterialDAO;
import dao.PessoaDAO;
import dto.NovoMaterialDTO;
import dto.NovoMaterialFisicoDTO;
import model.material.Material;
import model.material.MaterialDigital;
import model.material.MaterialFisico;
import model.pessoa.Pessoa;
import util.TokenUtil;

import java.util.Objects;

public class MaterialService {
    private final MaterialDAO materialDAO;
    private final PessoaDAO pessoaDAO;

    public MaterialService(MaterialDAO materialDAO, PessoaDAO pessoaDAO) {
        this.materialDAO = Objects.requireNonNull(materialDAO, "DAO dos materiais não pode ser nulo");
        this.pessoaDAO = Objects.requireNonNull(pessoaDAO, "DAO dos usuários não pode ser nulo");
    }

    public Material addMaterialDigital(NovoMaterialDTO dto, String token) {
        if (dto == null) throw new IllegalArgumentException("Dados do material inválidos");

        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Pessoa autor = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        MaterialDigital materialDigital = new MaterialDigital(autor.getNome(), dto);
        materialDigital.cadastrarMaterialDigital(materialDAO);
        return materialDigital;
    }

    public Material addMaterialFisico(NovoMaterialFisicoDTO dto, String token) {
        if (dto == null) throw new IllegalArgumentException("Dados do material inválidos");

        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Pessoa autor = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        MaterialFisico materialFisico = new MaterialFisico(autor.getNome(), dto);
        materialFisico.cadastrarMaterialFisico(materialDAO);
        return materialFisico;
    }
}
