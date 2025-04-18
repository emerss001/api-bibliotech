package Controller;

import com.google.gson.Gson;
import service.MaterialService;
import static spark.Spark.*;

public class MaterialController {
    private final MaterialService materialService = new MaterialService();

   public MaterialController() {
        setupRoutes();
   }

   private void setupRoutes() {
       // GET /materials --> Lista todos os materiais
       get("/materials", ((request, response) -> {
         try {
             response.type("application/json");
             return new Gson().toJson(materialService.getAll());
         } catch (Exception e) {
             System.err.println(e.getMessage());
             return null;
         }
       }));
   }

}
