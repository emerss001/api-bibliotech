package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.AvaliacaoDTO;
import model.material.Avaliacao;
import service.AvaliacaoService;

import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
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
        //get("/protegida/avaliacoes", this::listarAvaliacao);
        //delete("/protegida/avaliacoes", this::excluirAvaliacao);
    }

    private Object criarAvaliacao(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
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

//    private Object listarAvaliacao(Request request, Response response) {
//
//
//    }
//
//    private Object excluirAvaliacao(Request request, Response response) {
//
//
//    }

}