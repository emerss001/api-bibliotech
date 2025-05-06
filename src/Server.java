import com.google.gson.Gson;
import controller.AuthController;
import controller.CatalogoController;
import controller.EmprestimoController;
import controller.MaterialController;
import dao.CatalogoDAO;
import dao.EmprestimoDAO;
import dao.MaterialDAO;
import dao.PessoaDAO;
import service.AuthService;
import service.CatalogoService;
import service.EmprestimoService;
import service.MaterialService;
import util.FirebaseInitializer;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        // 1. Configuração mínima do servidor
        FirebaseInitializer.initialize();
        port(8888);
        enableCORS();
        after((req, res) -> res.type("application/json"));

        // 2. Centraliza o setup das dependências
        depedenciasContainer dependencies = setupDependencies();

        // 3. Registra todos os controllers de forma organizada
        registrarControllers(dependencies);

        get("/", (req, res) -> "API rodando");
    }

    // Classe interna para agrupar dependências
    private static class depedenciasContainer {
        final Gson gson = new Gson();
        final PessoaDAO pessoaDAO = new PessoaDAO();
        final MaterialDAO materialDAO = new MaterialDAO();
        final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        final CatalogoDAO catalogoDAO = new CatalogoDAO();

        final AuthService authService = new AuthService(pessoaDAO);
        final MaterialService materialService = new MaterialService(materialDAO, pessoaDAO);
        final EmprestimoService emprestimoService = new EmprestimoService(emprestimoDAO,pessoaDAO);
        final CatalogoService catalogoService = new CatalogoService(catalogoDAO);
    }

    private static depedenciasContainer setupDependencies() {
        return new depedenciasContainer(); // Todas as dependências são criadas aqui
    }

    private static void registrarControllers(depedenciasContainer dc) {
        new AuthController(dc.authService, dc.gson);
        new MaterialController(dc.materialService, dc.gson);
        new EmprestimoController(dc.emprestimoService,dc.gson);
        new CatalogoController(dc.catalogoService,dc.gson);
        // Adicione novos controllers aqui com 1 linha cada
    }

    private static void enableCORS() {
        options("/*", ((request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) response.header("Access-Control-Allow-Methods", accessControlRequestMethod);

            return "OK";

        }));

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*"); // Ou restrinja, ex: http://localhost:3000
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
        });
    }
}
