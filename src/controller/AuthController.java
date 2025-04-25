package controller;

import com.google.gson.Gson;
import dto.LoginDTO;
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

    private void rotaProtegida(Request request, Response response) {
        String token = request.headers("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            halt(401, "Token não informado");
        }

        try {
            TokenUtil.validarToken(token.replace("Bearer ", ""));
        } catch (Exception e) {
            halt(401, gson.toJson(Map.of("error", e.getMessage())));
        }
    }
}
