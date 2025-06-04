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

    public List<ListarMateriaisDTO> listarMateriaisPorCriador(String emailCriador) {
        if (emailCriador == null || emailCriador.trim().isEmpty()) {
            return new ArrayList<>();
        }
        Pessoa criador = pessoaDAO.buscarPorEmail(emailCriador);
        if (criador == null) {
            System.out.println("Nenhum criador encontrado com o email: " + emailCriador);
            return new ArrayList<>();
        }
        int idCriador = criador.getId();
        List<Material> materiaisDoCriador = materialDAO.buscarPorIdCriador(idCriador);
        List<ListarMateriaisDTO> dtos = new ArrayList<>();
        if (materiaisDoCriador != null) {
            for (Material material : materiaisDoCriador) {
                dtos.add(new ListarMateriaisDTO(
                        material.getId(),
                        material.getCadastradoPor().getNome(),
                        material.getTitulo(),
                        material.getFormato().getName(),
                        material.getArea().getTipo(),
                        material.getDescricao(),
                        material.getNivel(),
                        material.getTipo()
                ));
            }
        }
        return dtos;
    }
    public void apagarMaterial(int idMaterial, String emailUsuarioLogado, String tipoUsuarioLogado) {
        Material materialParaApagar = materialDAO.buscarDetalhesMaterial(idMaterial);

        if (materialParaApagar == null) {
            throw new IllegalArgumentException("Material não encontrado com o ID: " + idMaterial);
        }

        int idCriadorDoMaterial = materialParaApagar.getCadastradoPor().getId();
        String identificador = null;

        String emailCriadorDoMaterial = null;
        if (idCriadorDoMaterial > 0) {
            Pessoa criadorDoMaterial = pessoaDAO.buscarPorId(idCriadorDoMaterial);
            if (criadorDoMaterial != null) {
                emailCriadorDoMaterial = criadorDoMaterial.getEmail();
            }
        }

        boolean temPermissao = false;
        if (emailCriadorDoMaterial != null && emailCriadorDoMaterial.equals(emailUsuarioLogado)) {
            temPermissao = true;
        } else if (tipoUsuarioLogado != null && tipoUsuarioLogado.equalsIgnoreCase("BIBLIOTECARIO")) {
            temPermissao = true;
        }

        if (!temPermissao) {
            throw new SecurityException("Usuário não tem permissão para apagar este material.");
        }

        boolean apagadoComSucesso = materialDAO.apagarPorId(idMaterial);

        if (!apagadoComSucesso) {
            throw new RuntimeException("Falha ao apagar o material com ID: " + idMaterial);
        }
    }

}