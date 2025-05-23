package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.EmprestimoFiltroDTO;
import model.material.Emprestimo;
import service.EmprestimoService;
import dto.NovoEmprestimoDTO;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.lang.reflect.Array;
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
        post("/protegida/emprestimos/:materialId", this::criarEmprestimo);
        patch("/protegida/emprestimos/aprovar", this::aprovarEmprestimo);
        get("/protegida/emprestimos", this::listarEmprestimo);
        patch("/protegida/emprestimos", this::atualizarEmprestimo);
        delete("/protegida/emprestimos", this::excluirEmprestimo);
    }

    private Object criarEmprestimo(Request request, Response response) {
        try {
            String token = request.headers("Authorization");
            Integer materialId = Integer.parseInt(request.params("materialId"));

            Integer alunoId = emprestimoService.tokenTOId(token);
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

    private Object aprovarEmprestimo(Request request, Response response) {
        try {
            Integer emprestimoId = Integer.valueOf(request.queryParams("emprestimoId"));

            emprestimoService.aprovarEmprestimo(emprestimoId);

            response.status(200);
            return gson.toJson("Empréstimo aprovado");
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id do empréstimo inválido"));
        }
    }

    private Object atualizarEmprestimo(Request request, Response response) {
        try {
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
            Integer quantidadeArr = request.queryParams("quantidade") != null ? Integer.parseInt(request.queryParams("quantidade")) : null;
            String[] statusArr = request.queryParamsValues("status");
            String[] alunoIdArr = request.queryParamsValues("alunoId");

            ArrayList<Emprestimo> lista = emprestimoService.listEmprestimo(
                    new EmprestimoFiltroDTO(
                        quantidadeArr,
                        statusArr != null ? Arrays.asList(statusArr) : List.of(),
                        alunoIdArr != null ? Arrays.asList(alunoIdArr) : List.of()
                    )
            );

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
