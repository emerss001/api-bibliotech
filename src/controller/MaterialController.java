package controller;

import com.google.gson.Gson;
import dto.NovoMaterialDTO;
import model.material.Material;
import service.MaterialService;
import spark.Request;
import spark.Response;
import type.MaterialNivel;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

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
       post("/protegida/materials/material-digital", this::criarMaterialDigital);
   }

    private Object criarMaterialDigital(Request request, Response response) {
         try {
             request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

             // Pegando os dados da requisição
             String titulo = request.queryParams("titulo");
             String formato = request.queryParams("formato");
             String area = request.queryParams("area");
             String nivel = request.queryParams("nivel");
             String descricao = request.queryParams("descricao");
             Part arquivo = request.raw().getPart("arquivo");

             Material novoMaterial = materialService.addMaterialDigital (
                     new NovoMaterialDTO(
                             titulo,
                             formato,
                             area,
                             MaterialNivel.fromString(nivel),
                             descricao,
                             arquivo
                     )
             );

             if (novoMaterial == null) throw new RuntimeException();

             response.status(201);
             return gson.toJson(Map.of("id", novoMaterial.getId()));
         } catch (Exception e) {
             System.err.println(e.getMessage());
             response.type("application/json");
             response.status(500);

            return gson.toJson("Erro ao listar os materiais.");
         }
   }

}
