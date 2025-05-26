package service;

import dao.CatalogoDAO;
import dao.MaterialDAO;
import dao.PessoaDAO;
import dto.material.ListarMateriaisDTO;
import dto.material.MateriaisFiltrosDTO;
import dto.material.NovoMaterialDTO;
import dto.material.NovoMaterialFisicoDTO;
import model.catalogo.Catalogo;
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
    private final CatalogoDAO catalogoDAO;
    private final String TIPO_DIGITAL = "Digital";
    private final String TIPO_FISICO = "Fisico";

    public MaterialService(MaterialDAO materialDAO, PessoaDAO pessoaDAO, CatalogoDAO catalogoDAO) {
        this.materialDAO = Objects.requireNonNull(materialDAO, "DAO dos materiais não pode ser nulo");
        this.pessoaDAO = Objects.requireNonNull(pessoaDAO, "DAO dos usuários não pode ser nulo");
        this.catalogoDAO = Objects.requireNonNull(catalogoDAO, "DAO dos catálogos não pode ser nulo");
    }

    public Material addMaterialDigital(NovoMaterialDTO dto, String token) {
        if (dto == null) throw new IllegalArgumentException("Dados do material inválidos");
        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Pessoa adicionadoPor = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        Catalogo formato = catalogoDAO.catalogoExiste(dto.formato(), "formato");
        Catalogo area = catalogoDAO.catalogoExiste(dto.area(), "area");

        MaterialDigital materialDigital = new MaterialDigital(adicionadoPor, dto, formato, area, TIPO_DIGITAL);

        return materialDigital.cadastrarMaterialDigital(materialDAO);
    }

    public ArrayList<Integer> addMaterialFisico(NovoMaterialFisicoDTO dto, String token) {
        if (dto == null) throw new IllegalArgumentException("Dados do material inválidos");
        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Pessoa adicionadoPor = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));
        Catalogo formato = catalogoDAO.catalogoExiste(dto.formato(), "formato");
        Catalogo area = catalogoDAO.catalogoExiste(dto.area(), "area");

        MaterialFisico materialFisico = new MaterialFisico(adicionadoPor, dto, formato, area, TIPO_FISICO);

        return materialFisico.cadastrarMaterialFisico(materialDAO);
    }

    public List<ListarMateriaisDTO> buscarTodosMateriais(MateriaisFiltrosDTO filtros) {

        if (filtros == null) filtros = new MateriaisFiltrosDTO(List.of(), List.of(), List.of(), List.of());

        return materialDAO.buscarTodosMateriais(filtros);
    }

    public Material buscarDetalhesMaterial(int idMaterial) {
            return materialDAO.buscarDetalhesMaterial(idMaterial);
    }
}
