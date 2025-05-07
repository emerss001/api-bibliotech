package controller;

import com.google.gson.Gson;
import dto.CatalogoDTO;
import service.CatalogoService;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import java.util.List;

public class CatalogoController {
    private final CatalogoService catalogoService;
    private final Gson gson;

    public CatalogoController(CatalogoService catalogoService, Gson gson) {
        this.catalogoService = catalogoService;
        this.gson = gson;
        setupRoutes();
    }
    private void setupRoutes() {
        get("/catalogo/areas-conhecimento", this::getAreaConhecimento);
        get("/catalogo/formatos-materiais", this::getFormatoMateriais);
        get("/catalogo/necessidades", this::getNecessidades);
    }

    private Object getAreaConhecimento(Request request, Response response) {
        try {
            List<CatalogoDTO> areas = catalogoService.getAreaConhecimento();

            response.status(200);
            return gson.toJson(areas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getNecessidades(Request request, Response response) {
        try {
            List<CatalogoDTO> necessidades = catalogoService.getNecessidades();

            response.status(200);
            return gson.toJson(necessidades);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFormatoMateriais(Request request, Response response) {
        try {
            List<CatalogoDTO> formatos = catalogoService.getFormatosMateriais();

            response.status(200);
            return gson.toJson(formatos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
