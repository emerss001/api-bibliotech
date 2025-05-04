package controller;

import com.google.gson.Gson;
import model.material.Emprestimo;
import service.EmprestimoService;
import dto.NovoEmprestimoDTO;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.util.Map;

import static spark.Spark.*;

public class EmprestimoController {
    private final EmprestimoService emprestimoService;
    private final Gson gson;

    public EmprestimoController(EmprestimoService emprestimoService, Gson gson) {
        this.emprestimoService = emprestimoService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        post("/protegida/emprestimos", this::criarEmprestimo);
        //get("/protegida/emprestimos", this::listarEmprestimo);
        //put("/protegida/emprestimos", this::atualizarEmprestimo);
        //delete("/protegida/emprestimos", this::excluirEmprestimo);
    }

    private Object criarEmprestimo(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
            String token = request.headers("Authorization");
            String materialIdSTR = request.queryParams("materialId");
            Integer alunoId = emprestimoService.tokenTOId(token);
            Integer materialId = Integer.parseInt(materialIdSTR);

            Emprestimo novoEmprestimo = emprestimoService.addEmprestimo(
                    new NovoEmprestimoDTO(
                            alunoId,
                            materialId
                    )
            );

            if (novoEmprestimo == null) throw new RuntimeException();

            response.status(201);
            return gson.toJson(Map.of("id", novoEmprestimo.getId()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }
}
