package controller;

import com.google.gson.Gson;
import dto.NovoMaterialDTO;
import dto.NovoMaterialFisicoDTO;
import model.material.Material;
import service.MaterialService;
import spark.Request;
import spark.Response;
import type.MaterialNivel;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class MaterialController {
    private final MaterialService materialService;
    private final Gson gson;

    public MaterialController(MaterialService materialService, Gson gson) {
        this.materialService = materialService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        get("/protegida/materials", this::listarMateriais);
        get("/protegida/materials/:id", this::buscarDetalhesMaterial);
        post("/protegida/materials/material-digital", this::criarMaterialDigital);
        post("/protegida/materials/material-fisico", this::criarMaterialFisico);
    }

    private Object criarMaterialDigital(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
            String token = request.headers("Authorization");
            String titulo = request.queryParams("titulo");
            String autor = request.queryParams("autor");
            Integer formato = Integer.parseInt(request.queryParams("formato"));
            Integer area = Integer.parseInt(request.queryParams("area"));
            String nivel = request.queryParams("nivel");
            String descricao = request.queryParams("descricao");
            Part arquivo = request.raw().getPart("arquivo");

            Material novoMaterial = materialService.addMaterialDigital(
                    new NovoMaterialDTO(
                            titulo,
                            autor,
                            formato,
                            area,
                            MaterialNivel.fromString(nivel),
                            descricao,
                            arquivo
                    ), token
            );

            if (novoMaterial == null) throw new RuntimeException();

            response.status(201);
            return gson.toJson(Map.of("id", novoMaterial.getId()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object criarMaterialFisico(Request request, Response response) {
        try {
            // Pegando os dados da requisição
            String token = request.headers("Authorization");
            String titulo = request.queryParams("titulo");
            String autor = request.queryParams("autor");
            Integer formato = Integer.parseInt(request.queryParams("formato"));
            Integer areaConhecimento = Integer.parseInt(request.queryParams("area"));
            String nivel = request.queryParams("nivel");
            String descricao = request.queryParams("descricao");
            Integer quantidade = Integer.parseInt(request.queryParams("quantidade"));

            ArrayList<Integer> novoMaterialList = materialService.addMaterialFisico(
                    new NovoMaterialFisicoDTO(
                            titulo,
                            autor,
                            formato,
                            areaConhecimento,
                            MaterialNivel.fromString(nivel),
                            descricao,
                            quantidade
                    ), token
            );

            if (novoMaterialList == null) throw new RuntimeException();

            response.status(201);
            return gson.toJson(Map.of("id", novoMaterialList));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);
            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object listarMateriais(Request request, Response response) {
        try {
            String inferiorParam = request.queryParams("limiteInferior");
            String superiorParam = request.queryParams("limiteSuperior");

            if (inferiorParam == null || superiorParam == null) return gson.toJson(Map.of("error", "Parâmetros 'limiteInferior' e 'limiteSuperior' são obrigatórios"));

            List<Material> materiais = materialService.buscarTodosMateriais(Integer.parseInt(inferiorParam), Integer.parseInt(superiorParam));

            response.status(200);
            return gson.toJson(materiais);
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "Parâmetros devem ser números inteiros"));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            response.status(500);
            System.out.println(e.getMessage());
            return gson.toJson(Map.of("error", "Erro interno ao buscar materiais"));
        }
    }

    private Object buscarDetalhesMaterial(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("id"));

            if (idMaterial <= 0) return gson.toJson(Map.of("error: ", "id inválido"));
            Material material = materialService.buscarDetalhesMaterial(idMaterial);

            response.status(200);
            return gson.toJson(material);
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "Parâmetros devem ser números inteiros"));
        }
    }
}