import com.google.gson.Gson;
import controller.AuthController;
import controller.MaterialController;
import controller.PessoaController;
import dao.MaterialDAO;
import dao.PessoaDAO;
import service.AuthService;
import service.MaterialService;
import service.PessoaService;
import util.FirebaseInitializer;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        // 1. Configuração mínima do servidor
        FirebaseInitializer.initialize();
        port(8888);
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

        final PessoaService pessoaService = new PessoaService(pessoaDAO);
        final AuthService authService = new AuthService(pessoaDAO);
        final MaterialService materialService = new MaterialService(materialDAO);
    }

    private static depedenciasContainer setupDependencies() {
        return new depedenciasContainer(); // Todas as dependências são criadas aqui
    }

    private static void registrarControllers(depedenciasContainer dc) {
        new PessoaController(dc.pessoaService, dc.gson);
        new AuthController(dc.authService, dc.gson);
        new MaterialController(dc.materialService, dc.gson);
        // Adicione novos controllers aqui com 1 linha cada
    }
}