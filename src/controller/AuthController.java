package controller;

import com.google.gson.Gson;
import dto.LoginDTO;
import dto.PessoaDTO;
import exception.DataConflictException;
import model.pessoa.Pessoa;
import service.AuthService;
import spark.Request;
import spark.Response;
import util.TokenUtil;
import static spark.Spark.*;
import java.util.Map;

public class AuthController {
    private final AuthService authService;
    private final Gson gson;

    public AuthController(AuthService authService, Gson gson) {
        this.authService = authService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        post("/login", this::login);
        post("/cadastro", this::cadastro);
        before("/protegida/*", this::rotaProtegida);
    }

    private Object login(Request request, Response response) {
        try {
            LoginDTO loginDTO = gson.fromJson(request.body(), LoginDTO.class);
            String token = authService.autenticar(loginDTO);

            response.status(200);
            return gson.toJson(Map.of("token", token));
        } catch (Exception e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object cadastro(Request request, Response response) {
        try {
            PessoaDTO dto = gson.fromJson(request.body(), PessoaDTO.class);
            Pessoa pessoaCriada = authService.cadastro(dto);

            if (pessoaCriada == null) {
                throw new RuntimeException();
            }

            response.status(201);
            return gson.toJson(Map.of("Mensagem", "Usuário criado com sucesso"));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage())); // Formato padronizado
        } catch (DataConflictException e) {
            response.status(409);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.status(500);
            return gson.toJson(Map.of("error", "Erro interno no servidor"));
        }
    }

    private void rotaProtegida(Request request, Response response) {
        String token = request.headers("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            halt(401, gson.toJson(Map.of("Error", "Token não informado")));
        }

        try {
            String tipo = TokenUtil.extrairTipo(token);
            System.out.println("Acesso autorizado para tipo: " + tipo); // apenas para debug

            if (! "Bibliotecario" .equalsIgnoreCase(tipo)){
                halt(403, gson.toJson(Map.of("error", "Acesso restrito ao bibliotecario")));
            }

        } catch (Exception e) {
            halt(401, gson.toJson(Map.of("error", e.getMessage())));
        }
    }

}
