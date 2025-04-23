import controller.MaterialController;

import static spark.Spark.get;
import static spark.Spark.port;

public class Server {
    public static void main(String[] args) {
        // Configuração básica
        port(8888);
        new MaterialController();

        // Rota de health check
        get("/", (request, response) -> {
            response.type("application/json");
            return "{\"status\":\"running\", \"message\":\"API de materiais está operacional\"}";
        });
    }
}