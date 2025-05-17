package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.material.Emprestimo;
import service.EmprestimoService;
import dto.NovoEmprestimoDTO;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class EmprestimoController {
    private final EmprestimoService emprestimoService;
    private final Gson gson;

    public EmprestimoController(EmprestimoService emprestimoService, Gson gson) {
        this.emprestimoService = emprestimoService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        post("/protegida/emprestimos", this::criarEmprestimo);
        get("/protegida/emprestimos", this::listarEmprestimo);
        patch("/protegida/emprestimos", this::atualizarEmprestimo);
        delete("/protegida/emprestimos", this::excluirEmprestimo);
    }

    private Object criarEmprestimo(Request request, Response response) {
        try {
            String token = request.headers("Authorization");

            Integer alunoId = emprestimoService.tokenTOId(token);
            Integer materialId = Integer.valueOf(request.queryParams("materialId"));

            Emprestimo novoEmprestimo = emprestimoService.addEmprestimo(alunoId, materialId);

            if (novoEmprestimo == null) throw new RuntimeException();

            response.status(201);
            return gson.toJson(Map.of("id", novoEmprestimo.getId()));
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "Id do material inválido"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object atualizarEmprestimo(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
            NovoEmprestimoDTO emprestimoDTO = gson.fromJson(request.body(), NovoEmprestimoDTO.class);

            emprestimoService.updateEmprestimo(emprestimoDTO);

            response.status(204);
            return gson.toJson("No content");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object listarEmprestimo(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
            JsonObject emprestimoJson = gson.fromJson(request.body(), JsonObject.class);

            ArrayList<Emprestimo> lista = emprestimoService.listEmprestimo(emprestimoJson);

            response.status(200);
            return gson.toJson(lista);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object excluirEmprestimo(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
            JsonObject json = gson.fromJson(request.body(), JsonObject.class);

            emprestimoService.deleteEmprestimo(json.get("id").getAsInt());

            response.status(204);
            return gson.toJson("No content");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }
}
