package service;

import dao.MaterialDAO;
import model.Material;

import java.util.List;

public class MaterialService {
    private final MaterialDAO materialDAO = new MaterialDAO();

    public List<Material> getAll() {
        return materialDAO.getAll();
    }
}
