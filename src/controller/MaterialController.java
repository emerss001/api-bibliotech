package controller;

import com.google.gson.Gson;
import model.Material;
import service.MaterialService;
import static spark.Spark.*;

public class MaterialController {
    private final MaterialService materialService = new MaterialService();
    private final Gson gson = new Gson();

   public MaterialController() {
        setupRoutes();
   }

   private void setupRoutes() {
       // GET /materials --> Lista todos os materiais
       get("/protegida/materials", ((request, response) -> {
         try {
             response.type("application/json");
             response.status(200);

             return gson.toJson(materialService.getAll());
         } catch (Exception e) {
             System.err.println(e.getMessage());
             response.type("application/json");
             response.status(500);

            return gson.toJson("Erro ao listar os materiais.");
         }
       }));

       // POST /materials --> Criar um novo material
       post("/materials", (request, response) -> {
           try {
               response.type("application/json");
               Material material = gson.fromJson(request.body(), Material.class);

               Material createdMaterial = materialService.addMaterial(material);
               response.status(201);
               return gson.toJson(createdMaterial);

           } catch (IllegalArgumentException e) {
               response.status(400);
               return gson.toJson(e.getMessage());
           } catch (Exception e) {
               response.status(500);
               return gson.toJson("Erro ao criar material: " + e.getMessage());
           }
       });
   }

}
