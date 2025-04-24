import com.google.gson.Gson;
import controller.MaterialController;
import controller.PessoaController;
import dao.MaterialDAO;
import dao.PessoaDAO;
import service.MaterialService;
import service.PessoaService;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        // 1. Configuração mínima do servidor
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
        final MaterialService materialService = new MaterialService();
    }

    private static depedenciasContainer setupDependencies() {
        return new depedenciasContainer(); // Todas as dependências são criadas aqui
    }

    private static void registrarControllers(depedenciasContainer dc) {
        new PessoaController(dc.pessoaService, dc.gson);
        new MaterialController();
        // Adicione novos controllers aqui com 1 linha cada
    }
}