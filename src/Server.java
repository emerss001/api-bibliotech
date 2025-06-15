import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.*;
import dao.*;
import service.*;
import util.FirebaseInitializer;
import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        // 1. Configuração mínima do servidor
        FirebaseInitializer.initialize();
        int porta = Integer.parseInt(System.getenv().getOrDefault("PORT", "8888"));
        port(porta);

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
        final Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        final PessoaDAO pessoaDAO = new PessoaDAO();
        final MaterialDAO materialDAO = new MaterialDAO();
        final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        final CatalogoDAO catalogoDAO = new CatalogoDAO();
        final AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
        final BibliotecarioDAO bibliotecarioDAO = new BibliotecarioDAO();

        final AuthService authService = new AuthService(pessoaDAO);
        final MaterialService materialService = new MaterialService(materialDAO, pessoaDAO, catalogoDAO);
        final EmprestimoService emprestimoService = new EmprestimoService(emprestimoDAO, pessoaDAO, materialDAO);
        final CatalogoService catalogoService = new CatalogoService(catalogoDAO);
        final AvaliacaoService avaliacaoService = new AvaliacaoService(avaliacaoDAO,pessoaDAO);
        final BibliotecarioService bibliotecarioService = new BibliotecarioService(bibliotecarioDAO);
    }

    private static depedenciasContainer setupDependencies() {
        return new depedenciasContainer(); // Todas as dependências são criadas aqui
    }

    private static void registrarControllers(depedenciasContainer dc) {
        new MaterialController(dc.materialService, dc.gson);
        new AuthController(dc.authService, dc.gson);
        new EmprestimoController(dc.emprestimoService,dc.gson);
        new CatalogoController(dc.catalogoService,dc.gson);
        new AvaliacaoController(dc.avaliacaoService,dc.gson);
        new BibliotecarioController(dc.bibliotecarioService, dc.gson);
        // Adicione novos controllers aqui com 1 linha cada
    }

    private static void enableCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

            // Para requisições OPTIONS, retorne imediatamente
            if (request.requestMethod().equals("OPTIONS")) {
                response.status(200);
                halt();
            }
        });
    }}
