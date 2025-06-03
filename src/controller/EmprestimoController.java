package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.material.Emprestimo;
import service.EmprestimoService;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
        get("protegida/emprestimos/aluno", this::listarEmprestimosAluno);
        post("/protegida/emprestimos/:materialId", this::criarEmprestimo);
        patch("/protegida/emprestimos/aprovar/:emprestimoId", this::aprovarEmprestimo);
        patch("/protegida/emprestimos/rejeitar/:emprestimoId", this::rejeitarEmprestimo);
        delete("/protegida/emprestimos/:emprestimoId", this::excluirEmprestimo);

    }

    private Object listarEmprestimosAluno(Request request, Response response) {
        try {
            String token = request.headers("Authorization");

            List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosAluno(token);
            response.status(200);
            return gson.toJson(emprestimos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Object criarEmprestimo(Request request, Response response) {
        try {
            Integer materialId = Integer.parseInt(request.params("materialId"));
            String token = request.headers("Authorization");
            JsonObject json = JsonParser.parseString(request.body()).getAsJsonObject();

            String mensagem = null;
            if (json.has("mensagem") && !json.get("mensagem").isJsonNull()) {
                mensagem = json.get("mensagem").getAsString();
            }


            Emprestimo novoEmprestimo = emprestimoService.addEmprestimo(token, materialId, mensagem);
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
            Integer emprestimoId = Integer.parseInt(request.params("emprestimoId"));
            JsonObject json = JsonParser.parseString(request.body()).getAsJsonObject();
            OffsetDateTime dateTime = OffsetDateTime.parse(json.get("dataDevolucao").getAsString());
            LocalDate dataDevolucao = dateTime.toLocalDate();

            emprestimoService.aprovarEmprestimo(emprestimoId, dataDevolucao);

            response.status(200);
            return gson.toJson("Empréstimo aprovado");
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id inválido"));
        } catch (DateTimeException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "data inválida"));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object rejeitarEmprestimo(Request request, Response response) {
        try {
            Integer emprestimoId = Integer.parseInt(request.params("emprestimoId"));
            JsonObject json = JsonParser.parseString(request.body()).getAsJsonObject();
            String mensagemRejeicao = json.get("mensagemRejeicao").getAsString();

            emprestimoService.rejeitarEmprestimo(emprestimoId, mensagemRejeicao);
            response.status(200);
            return gson.toJson("Empréstimo rejeitado");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object excluirEmprestimo(Request request, Response response) {
        try {

            Integer emprestimoId = Integer.parseInt(request.params("emprestimoId"));

            if (emprestimoId <= 0) {
                response.status(400);
                return gson.toJson(Map.of("error", "ID do empréstimo inválido."));
            }

            emprestimoService.deleteEmprestimo(emprestimoId);

            response.status(204);
            return "";
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "ID do empréstimo deve ser um número."));
        } catch (RuntimeException e) {
            response.status(404);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erro ao excluir empréstimo: " + e.getMessage());
            response.status(500);
            return gson.toJson(Map.of("error", "Erro interno ao tentar excluir o empréstimo."));
        }
    }
}
