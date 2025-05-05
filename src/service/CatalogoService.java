package service;

import dao.CatalogoDAO;
import dto.CatalogoDTO;

import java.util.List;

public class CatalogoService {
    private final CatalogoDAO catalogoDAO;

    public CatalogoService(CatalogoDAO dao) {
        this.catalogoDAO = dao;
    }
    public List<CatalogoDTO> getAreaConhecimento() {
        return catalogoDAO.getAreaConhecimento();
    }

    public List<CatalogoDTO> getNecessidades() {
        return catalogoDAO.getNecessidades();
    }

    public List<CatalogoDTO> getFormatosMateriais() {
        return catalogoDAO.getFormatosMateriais();
    }
}
