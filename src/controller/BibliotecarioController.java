package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.bibliotecario.*;
import entity.material.Material;
import service.BibliotecarioService;
import spark.Request;
import spark.Response;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class BibliotecarioController {
    private final BibliotecarioService bibliotecarioService;
    private final Gson gson;

    public BibliotecarioController(BibliotecarioService bibliotecarioService, Gson gson) {
        this.bibliotecarioService = bibliotecarioService;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        get("/admin/emprestimos-pendentes", this::getEmprestimosPendentes);
        get("/admin/emprestimos-andamento", this::getEmprestimosAndamento);
        patch("/admin/emprestimos/devolver/:emprestimoId", this::devolverEmprestimo);
        patch("/admin/emprestimos/renovar/:emprestimoId", this::renovarEmprestimo);

        get("/admin/cadastros-pendentes", this::getCadastrosPendentes);
        get("/admin/metricas", this::getMetricas);
        patch("/admin/aprovar-cadastro/:pessoaId", this::aprovarCadastro);
        delete("/admin/rejeitar-cadastro/:pessoaId", this::rejeitarCadastro);

        get("/admin/metricas-materiais", this::getMetricasMaterias);
        get("/admin/buscar-materiais", this::getMateriais);
        delete("/admin/excluir-material/:idMaterial", this::excluirMaterialAdmin);
        patch("/admin/ocultar-material/:idMaterial", this::ocultarMaterial);
        patch("/admin/listar-material/:idMaterial", this::listarMaterial);

        get("/admin/metricas-alunos", this::getMetricasAlunos);
        get("/admin/buscar-alunos", this::listarAlunos);
        patch("/admin/suspender-aluno/:idAluno", this::suspenderAluno);
        patch("/admin/ativar-aluno/:idAluno", this::ativarAluno);
    }

    private Object getEmprestimosPendentes(Request request, Response response) {
        try {
            List<EmprestimosPendentesDTO> emprestimos = bibliotecarioService.bucarEmprestimosPendentes();

            response.status(200);
            return gson.toJson(emprestimos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getEmprestimosAndamento(Request request, Response response) {
        try {
            List<EmprestimosPendentesDTO> emprestimos = bibliotecarioService.buscarEmprestimosAndamento();

            response.status(200);
            return gson.toJson(emprestimos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object devolverEmprestimo(Request request, Response response) {
        try {
            Integer emprestimoId = Integer.parseInt(request.params("emprestimoId"));

            bibliotecarioService.devolverEmprestimo(emprestimoId);
            response.status(200);
            return gson.toJson("Empréstimo devolvido");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object renovarEmprestimo(Request request, Response response) {
        try {
            Integer emprestimoId = Integer.parseInt(request.params("emprestimoId"));
            JsonObject json = JsonParser.parseString(request.body()).getAsJsonObject();
            OffsetDateTime dateTime = OffsetDateTime.parse(json.get("dataDevolucao").getAsString());
            LocalDate dataDevolucao = dateTime.toLocalDate();

            bibliotecarioService.renovarEmprestimo(emprestimoId, dataDevolucao);

            response.status(200);
            return gson.toJson("Empréstimo aprovado");
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id inválido"));
        } catch (DateTimeException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "data inválida"));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object getCadastrosPendentes(Request request, Response response) {
        try {
            List<CadastrosPendentesDTO> cadastros = bibliotecarioService.buscarCadastrosPendentes();

            response.status(200);
            return gson.toJson(cadastros);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getMetricas(Request request, Response response) {
        try {
            MetricasDTO metricas = bibliotecarioService.buscarMetricas();

            response.status(200);
            return gson.toJson(metricas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object aprovarCadastro(Request request, Response response) {
        try {
            Integer pessoaId = Integer.parseInt(request.params("pessoaId"));

            if (bibliotecarioService.aprovarCadastro(pessoaId)) {
                response.status(200);
                return gson.toJson("cadastro aprovado");
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id inválido"));
        } catch (RuntimeException e) {
            response.status(404);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Object rejeitarCadastro(Request request, Response response) {
        try {
            Integer pessoaId = Integer.parseInt(request.params("pessoaId"));

            if (bibliotecarioService.rejeitarCadastro(pessoaId)) {
                response.status(200);
                return gson.toJson("cadastro rejeitado");
            }
        } catch (NumberFormatException e) {
            response.status(400);
            return gson.toJson(Map.of("error", "id inválido"));
        } catch (RuntimeException e) {
            response.status(404);
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // materiais
    private Object getMetricasMaterias(Request request, Response response) {
        try {
            MetricasMaterias metricasMaterias = bibliotecarioService.buscarMetricasMateriais();

            response.status(200);
            return gson.toJson(metricasMaterias);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getMateriais(Request request, Response response) {
        try {
            List<Material> materials = bibliotecarioService.buscarMateriais();

            response.status(200);
            return gson.toJson(materials);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object excluirMaterialAdmin(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("idMaterial"));

            bibliotecarioService.excluirMaterialAdmin(idMaterial);

            response.status(200);
            return gson.toJson("Material excluido com sucesso");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(e.getMessage());
        }
    }

    private Object ocultarMaterial(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("idMaterial"));
            bibliotecarioService.ocultarMaterial(idMaterial);

            response.status(200);
            return gson.toJson("Material ocultado com sucesso");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object listarMaterial(Request request, Response response) {
        try {
            int idMaterial = Integer.parseInt(request.params("idMaterial"));
            bibliotecarioService.listarMaterial(idMaterial);

            response.status(200);
            return gson.toJson("Material listado com sucesso");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Alunos
    private Object getMetricasAlunos(Request request, Response response) {
        try {
            MetricasAlunos usuarios = bibliotecarioService.buscarMetricasAlunos();

            response.status(200);
            return gson.toJson(usuarios);
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(Map.of("error", e.getMessage()));
        }
    }

    private Object listarAlunos(Request request, Response response) {
        try {
            List<AlunosCadastradosDTO> alunos = bibliotecarioService.buscarALunos();

            response.status(200);
            return gson.toJson(alunos);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object suspenderAluno(Request request, Response response) {
        try {
            int idAluno = Integer.parseInt(request.params("idAluno"));
            bibliotecarioService.suspenderALuno(idAluno);

            response.status(200);
            return gson.toJson("Aluno suspenso com sucesso");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object ativarAluno(Request request, Response response) {
        try {
            int idAluno = Integer.parseInt(request.params("idAluno"));
            bibliotecarioService.ativarALuno(idAluno);

            response.status(200);
            return gson.toJson("Aluno ativado com sucesso");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
