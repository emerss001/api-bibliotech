package controller;

import com.google.gson.Gson;
import dto.bibliotecario.CadastrosPendentesDTO;
import dto.bibliotecario.EmprestimosPendentesDTO;
import dto.bibliotecario.MetricasDTO;
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
        get("/protegida/admin/emprestimos-pendentes", this::getEmprestimosPendentes);
        get("/protegida/admin/cadastros-pendentes", this::getCadastrosPendentes);
        get("/protegida/admin/metricas", this::getMetricas);
        patch("/protegida/admin/aprovar-cadastro/:pessoaId", this::aprovarCadastro);
        delete("/protegida/admin/rejeitar-cadastro/:pessoaId", this::rejeitarCadastro);
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
}
