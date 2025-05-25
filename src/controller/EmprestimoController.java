package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.EmprestimoFiltroDTO;
import model.material.Emprestimo;
import service.EmprestimoService;
import dto.NovoEmprestimoDTO;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.lang.reflect.Array;
import java.time.LocalDate;
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
        get("protegida/emprestimos/aluno", this::listarEmprestimosAluno);
        post("/protegida/emprestimos/:materialId", this::criarEmprestimo);
        patch("/protegida/emprestimos/aprovar/:emprestimoId", this::aprovarEmprestimo);
        patch("/protegida/emprestimos/rejeitar/:emprestimoId", this::rejeitarEmprestimo);
        patch("/protegida/emprestimos/devolver/:emprestimoId", this::devolverEmprestimo);
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
            LocalDate dataDevolucao = LocalDate.parse(json.get("dataDevolucao").getAsString());

            emprestimoService.aprovarEmprestimo(emprestimoId, dataDevolucao);

            response.status(200);
            return gson.toJson("Empréstimo aprovado");
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id do empréstimo inválido"));
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

    private Object devolverEmprestimo(Request request, Response response) {
        try {
            Integer emprestimoId = Integer.parseInt(request.params("emprestimoId"));

            emprestimoService.devolverEmprestimo(emprestimoId);
            response.status(200);
            return gson.toJson("Empréstimo devolvido");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    private Object listarEmprestimo(Request request, Response response) {
//        try {
//            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
//
//            // Pegando os dados da requisição
//            Integer quantidadeArr = request.queryParams("quantidade") != null ? Integer.parseInt(request.queryParams("quantidade")) : null;
//            String[] statusArr = request.queryParamsValues("status");
//            String[] alunoIdArr = request.queryParamsValues("alunoId");
//
//            ArrayList<Emprestimo> lista = emprestimoService.listEmprestimo(
//                    new EmprestimoFiltroDTO(
//                            quantidadeArr,
//                            statusArr != null ? Arrays.asList(statusArr) : List.of(),
//                            alunoIdArr != null ? Arrays.asList(alunoIdArr) : List.of()
//                    )
//            );
//
//            response.status(200);
//            return gson.toJson(lista);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            response.type("application/json");
//            response.status(500);
//
//            return gson.toJson(Map.of("error", e.getMessage()));
//        }
//    }

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
