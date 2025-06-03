package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.AvaliacaoDTO;
import dto.AvaliacaoResponseDTO;
import entity.material.Avaliacao;
import service.AvaliacaoService;

import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.Map;

import static spark.Spark.*;

public class AvaliacaoController {
    private final AvaliacaoService avaliacaoService;
    private final Gson gson;

    public AvaliacaoController(AvaliacaoService avaliacaoService, Gson gson) {
        this.avaliacaoService = avaliacaoService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        post("/protegida/avaliacoes", this::criarAvaliacao);
        get("/protegida/avaliacoes/:materialId", this::listarAvaliacao);
        delete("/protegida/avaliacoes", this::excluirAvaliacao);
    }

    private Object criarAvaliacao(Request request, Response response) {
        try {
            String token = request.headers("Authorization");
            Integer alunoId = avaliacaoService.tokenTOId(token);
            JsonObject json = gson.fromJson(request.body(), JsonObject.class);

            Avaliacao novaAvaliacao = avaliacaoService.addAvaliacao(
                    new AvaliacaoDTO(
                            null,
                            alunoId,
                            json.get("materialId").getAsInt(),
                            json.get("nota").getAsInt(),
                            json.get("avaliacao").getAsString(),
                            null
                    )
            );

            if (novaAvaliacao == null) throw new RuntimeException();

            response.status(201);
            return gson.toJson(Map.of("id", novaAvaliacao.getId()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }

    }

    private Object listarAvaliacao(Request request, Response response) {
        try {
            int materialId = Integer.parseInt(request.params("materialId"));
            if (materialId <= 0) return gson.toJson(Map.of("error: ", "id inválido"));

            ArrayList<AvaliacaoResponseDTO> lista = avaliacaoService.readAvaliacao(materialId);
            response.status(200);
            return gson.toJson(lista);
        } catch (NumberFormatException e) {
            response.status(400);

            return gson.toJson(Map.of("error", "ID inválido. O identificador deve ser um número."));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }

    }

    private Object excluirAvaliacao(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
            JsonObject json = gson.fromJson(request.body(), JsonObject.class);
            avaliacaoService.removeAvaliacao(json);

            response.status(204);
            return gson.toJson(Map.of("id", ""));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

}