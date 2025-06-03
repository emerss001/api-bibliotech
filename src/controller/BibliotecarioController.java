package controller;

import com.google.gson.Gson;
import dto.bibliotecario.CadastrosPendentesDTO;
import dto.bibliotecario.EmprestimosPendentesDTO;
import dto.bibliotecario.MetricasDTO;
import dto.bibliotecario.MetricasMaterias;
import model.material.Material;
import service.BibliotecarioService;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class BibliotecarioController {
    private final BibliotecarioService bibliotecarioService;
    private final Gson gson;

    public BibliotecarioController(BibliotecarioService bibliotecarioService, Gson gson) {
        this.bibliotecarioService = bibliotecarioService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        get("/admin/emprestimos-pendentes", this::getEmprestimosPendentes);
        get("/admin/cadastros-pendentes", this::getCadastrosPendentes);
        get("/admin/metricas", this::getMetricas);
        patch("/admin/aprovar-cadastro/:pessoaId", this::aprovarCadastro);
        delete("/admin/rejeitar-cadastro/:pessoaId", this::rejeitarCadastro);

        get("/admin/metricas-materiais", this::getMetricasMaterias);
        get("/admin/buscar-materiais", this::getMateriais);
        delete("/admin/excluir-material/:idMaterial", this::excluirMaterialAdmin);
        patch("/admin/ocultar-material/:idMaterial", this::ocultarMaterial);
        patch("/admin/listar-material/:idMaterial", this::listarMaterial);
    }

    private Object getEmprestimosPendentes(Request request, Response response) {
        try {
            List<EmprestimosPendentesDTO> emprestimos = bibliotecarioService.bucarEmprestimosPendentes();

            response.status(200);
            return gson.toJson(emprestimos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getCadastrosPendentes(Request request, Response response) {
        try {
            List<CadastrosPendentesDTO> cadastros = bibliotecarioService.buscarCadastrosPendentes();

            response.status(200);
            return gson.toJson(cadastros);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getMetricas(Request request, Response response) {
        try {
            MetricasDTO metricas = bibliotecarioService.buscarMetricas();

            response.status(200);
            return gson.toJson(metricas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object aprovarCadastro(Request request, Response response) {
        try {
            Integer pessoaId = Integer.parseInt(request.params("pessoaId"));

            if (bibliotecarioService.aprovarCadastro(pessoaId)) {
                response.status(200);
                return gson.toJson("cadastro aprovado");
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id inválido"));
        } catch (RuntimeException e) {
            response.status(404);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Object rejeitarCadastro(Request request, Response response) {
        try {
            Integer pessoaId = Integer.parseInt(request.params("pessoaId"));

            if (bibliotecarioService.rejeitarCadastro(pessoaId)) {
                response.status(200);
                return gson.toJson("cadastro rejeitado");
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id inválido"));
        } catch (RuntimeException e) {
            response.status(404);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // materiais
    private Object getMetricasMaterias(Request request, Response response) {
        try {
            MetricasMaterias metricasMaterias = bibliotecarioService.buscarMetricasMateriais();

            response.status(200);
            return gson.toJson(metricasMaterias);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getMateriais(Request request, Response response) {
        try {
            List<Material> materials = bibliotecarioService.buscarMateriais();

            response.status(200);
            return gson.toJson(materials);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object excluirMaterialAdmin(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("idMaterial"));

            bibliotecarioService.excluirMaterialAdmin(idMaterial);

            response.status(200);
            return gson.toJson("Material excluido com sucesso");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(e.getMessage());
        }
    }

    private Object ocultarMaterial(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("idMaterial"));
            bibliotecarioService.ocultarMaterial(idMaterial);

            response.status(200);
            return gson.toJson("Material ocultado com sucesso");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object listarMaterial(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("idMaterial"));
            bibliotecarioService.listarMaterial(idMaterial);

            response.status(200);
            return gson.toJson("Material listado com sucesso");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
