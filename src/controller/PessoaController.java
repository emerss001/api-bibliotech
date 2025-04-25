package controller;

import com.google.gson.Gson;
import dto.PessoaDTO;
import exception.InvalidDataException;
import model.pessoa.Pessoa;
import service.PessoaService;
import spark.Request;
import spark.Response;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import static spark.Spark.*;

public class PessoaController {
    private final PessoaService pessoaService;
    private final Gson gson;

    public PessoaController(PessoaService pessoaService, Gson gson) {
        this.pessoaService = pessoaService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        post("/pessoas", this::criarPessoa);
    }

    private Object criarPessoa(Request request, Response response) {
        try {
            PessoaDTO dto = gson.fromJson(request.body(), PessoaDTO.class);
            Pessoa pessoaCriada = pessoaService.cadastrarPessoa(dto);

            if (pessoaCriada == null) {
                throw new RuntimeException();
            }

            response.status(201);
            return gson.toJson(Map.of("Mensagem", "Usuário criado com sucesso")); // Resposta de sucesso

        } catch (IllegalArgumentException | InvalidDataException e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage())); // Formato padronizado
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.status(500);
            return gson.toJson(Map.of("error", "Erro interno no servidor")); // Não expõe detalhes internos
        }
    }}
