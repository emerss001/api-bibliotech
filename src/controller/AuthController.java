package controller;

import com.google.gson.Gson;
import dto.LoginDTO;
import dto.PessoaDTO;
import exception.DataConflictException;
import exception.TokenInvalidoException;
import model.pessoa.Pessoa;
import service.AuthService;
import spark.Filter;
import spark.Request;
import spark.Response;
import util.TokenUtil;
import static spark.Spark.*;
import java.util.Arrays;
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

        // Prefixo admin para BIBLIOTECARIO
        before("/admin/*", verificarTipos("BIBLIOTECARIO"));

        // Rotas específicas por tipo
        before("/professor/*", verificarTipos("PROFESSOR"));
        before("/bibliotecario/*", verificarTipos("BIBLIOTECARIO"));
        before("/aluno/*", verificarTipos("ALUNO"));

        // Prefixo comum para ALUNO, PROFESSOR e BIBLIOTECARIO
        before("/comum/*", verificarTipos("ALUNO", "PROFESSOR", "BIBLIOTECARIO"));
    }

    private Object login(Request request, Response response) {
        try {
            LoginDTO loginDTO = gson.fromJson(request.body(), LoginDTO.class);
            String token = authService.autenticar(loginDTO);

            response.status(200);
            return gson.toJson(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            response.status(403);
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

    public Filter verificarTipos(String... tiposPermitidos) {
        return (request, response) -> {
            String token = request.headers("Authorization");
            if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
                halt(401, new Gson().toJson(Map.of("error", "Token ausente ou mal formatado")));
            }

            try {
                TokenUtil.validarToken(token.replace("Bearer ", ""));
                String tipoUsuario = TokenUtil.extrairTipo(token);

                boolean autorizado = Arrays.stream(tiposPermitidos)
                        .anyMatch(tipo -> tipo.equalsIgnoreCase(tipoUsuario));

                if (!autorizado) {
                    response.status(403);
                    halt(403, gson.toJson(Map.of("error", "Acesso negado para o tipo de usuário")));
                }
            } catch (TokenInvalidoException e) {
                System.out.println(e.getMessage());
                halt(401, gson.toJson(Map.of("error", "Token inválido ou expirado")));
            }
        };
    }


    private void rotaProtegida(Request request, Response response) {
        String token = request.headers("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            halt(401, gson.toJson(Map.of("Error", "Token não informado")));
        }

        try {
            TokenUtil.validarToken(token.replace("Bearer ", ""));
        } catch (Exception e) {
            halt(401, gson.toJson(Map.of("error", e.getMessage())));
        }
    }

}