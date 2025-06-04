package controller;

import exception.TokenInvalidoException;
import util.TokenUtil;
import com.google.gson.Gson;
import dto.material.ListarMateriaisDTO;
import dto.material.MateriaisFiltrosDTO;
import dto.material.NovoMaterialDTO;
import dto.material.NovoMaterialFisicoDTO;
import model.material.Material;
import service.MaterialService;
import spark.Request;
import spark.Response;
import type.MaterialNivel;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class MaterialController {
    private final MaterialService materialService;
    private final Gson gson;

    public MaterialController(MaterialService materialService, Gson gson) {
        this.materialService = materialService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        options("/protegida/materials", (req, res) -> {
            res.status(200);
            return "";
        });

        options("/protegida/materials/*", (req, res) -> {
            res.status(200);
            return "";
        });

        delete("/protegida/materials/:id", this::apagarMaterial);
        get("/protegida/materials/meus", this::listarMeusMateriais);
        get("/protegida/materials", this::listarMateriais);
        get("/protegida/materials/:id", this::buscarDetalhesMaterial);
        post("/protegida/materials/material-digital", this::criarMaterialDigital);
        post("/protegida/materials/material-fisico", this::criarMaterialFisico);

    }

    private Object criarMaterialDigital(Request request, Response response) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Pegando os dados da requisição
            String token = request.headers("Authorization");

            String titulo = request.queryParams("titulo");
            String autor = request.queryParams("autor");
            Integer formato = Integer.parseInt(request.queryParams("formato"));
            Integer area = Integer.parseInt(request.queryParams("area"));
            String nivel = request.queryParams("nivel");
            String descricao = request.queryParams("descricao");
            Part arquivo = request.raw().getPart("arquivo");

            Material novoMaterial = materialService.addMaterialDigital(
                    new NovoMaterialDTO(
                            titulo,
                            autor,
                            formato,
                            area,
                            MaterialNivel.fromString(nivel),
                            descricao,
                            arquivo
                    ), token
            );

            if (novoMaterial == null) throw new RuntimeException();

            response.status(201);
            return gson.toJson(Map.of("id", novoMaterial.getId()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);

            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object criarMaterialFisico(Request request, Response response) {
        try {
            // Pegando os dados da requisição
            String token = request.headers("Authorization");
            NovoMaterialFisicoDTO material = gson.fromJson(request.body(), NovoMaterialFisicoDTO.class);

            ArrayList<Integer> novoMaterialList = materialService.addMaterialFisico(material, token);
            if (novoMaterialList == null) throw new RuntimeException();

            response.status(201);
            return gson.toJson(Map.of("id", novoMaterialList));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.type("application/json");
            response.status(500);
            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object listarMateriais(Request request, Response response) {
        try {
            String[] tipoArr = request.queryParamsValues("tipo");
            String[] nivelArr = request.queryParamsValues("nivel");
            String[] formatoArr = request.queryParamsValues("formato");
            String[] areaArr = request.queryParamsValues("area");


            MateriaisFiltrosDTO filtros = new MateriaisFiltrosDTO(
                    tipoArr != null ? Arrays.asList(tipoArr) : List.of(),
                    nivelArr != null ? Arrays.asList(nivelArr) : List.of(),
                    formatoArr != null ? Arrays.stream(formatoArr).map(Integer::parseInt).toList() : List.of(),
                    areaArr != null ? Arrays.stream(areaArr).map(Integer::parseInt).toList() : List.of()
            );


            List<ListarMateriaisDTO> materiais = materialService.buscarTodosMateriais(filtros);

            response.status(200);
            return gson.toJson(materiais);
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "Parâmetros devem ser números inteiros"));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            response.status(500);
            System.out.println(e.getMessage());
            return gson.toJson(Map.of("error", "Erro interno ao buscar materiais"));
        }
    }

    private Object buscarDetalhesMaterial(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("id"));

            if (idMaterial <= 0) return gson.toJson(Map.of("error: ", "id inválido"));
            Material material = materialService.buscarDetalhesMaterial(idMaterial);

            if (material == null) {
                response.status(404);
                return gson.toJson(Map.of("Error", "Não foi encontrado material com o id " + idMaterial));
            }

            response.status(200);
            return gson.toJson(material);
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "Parâmetros devem ser números inteiros"));
        } catch (RuntimeException e) {
            response.status(404);
            return gson.toJson(Map.of("Error", e.getMessage()));
        } catch (Exception e) {
            response.status(500);
            System.out.println(e.getMessage());
            return gson.toJson(Map.of("error", "Erro interno ao buscar material"));
        }
    }

    private Object listarMeusMateriais(Request request, Response response) {
        response.type("application/json");
        try {
            String token = request.headers("Authorization");
            String emailUsuarioLogado = TokenUtil.extrairEmail(token);

            List<ListarMateriaisDTO> meusMateriais = materialService.listarMateriaisPorCriador(emailUsuarioLogado);
            response.status(200);
            return gson.toJson(meusMateriais);
        } catch (TokenInvalidoException e) {
            response.status(401);
            return gson.toJson(Map.of("error", "Token inválido ou expirado: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "Argumento inválido: " + e.getMessage()));
        } catch (RuntimeException e) {
            response.status(500);
            System.err.println("Erro de RuntimeException em listarMeusMateriais: " + e.getMessage());
            e.printStackTrace();
            return gson.toJson(Map.of("error", "Erro inesperado ao processar sua requisição: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erro em listarMeusMateriais: " + e.getMessage());
            e.printStackTrace();
            response.status(500);
            return gson.toJson(Map.of("error", "Erro interno ao listar seus materiais: " + e.getMessage()));
        }
    }

    private Object apagarMaterial(Request request, Response response) {
        response.type("application/json");
        try {
            String token = request.headers("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                throw new TokenInvalidoException("Token de autorização ausente ou malformado.");
            }

            int idMaterial = Integer.parseInt(request.params("id"));
            if (idMaterial <= 0) {
                throw new IllegalArgumentException("ID do material inválido.");
            }

            String emailUsuarioLogado = TokenUtil.extrairEmail(token);
            String tipoUsuarioLogado = TokenUtil.extrairTipo(token);
            materialService.apagarMaterial(idMaterial, emailUsuarioLogado, tipoUsuarioLogado);

            response.status(204);
            return "";
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "ID do material deve ser um número inteiro."));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("não encontrado")) {
                response.status(404);
            } else {
                response.status(400);
            }
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
            response.status(403);
            return gson.toJson(Map.of("error", "Acesso negado: " + e.getMessage()));
        } catch (TokenInvalidoException e) {
            response.status(401);
            return gson.toJson(Map.of("error", "Token inválido ou expirado: " + e.getMessage()));
        } catch (RuntimeException e) {
            response.status(401);
            System.err.println("Erro de RuntimeException (possivelmente token): " + e.getMessage());
            e.printStackTrace();
            return gson.toJson(Map.of("error", "Não autorizado ou erro de execução: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erro em apagarMaterial: " + e.getMessage());
            e.printStackTrace();
            response.status(500);
            return gson.toJson(Map.of("error", "Erro interno ao apagar material: " + e.getMessage()));
        }

    }
}