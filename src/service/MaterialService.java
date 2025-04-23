package service;

import dao.MaterialDAO;
import model.Material;

import java.util.List;

public class MaterialService {
    private final MaterialDAO materialDAO = new MaterialDAO();

    public List<Material> getAll() {
        return materialDAO.getAll();
    }

    public Material addMaterial(Material material) {
        if (material.getTitulo() == null || material.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O título do material é obrigatório");
        }
        return materialDAO.addMaterial(material);
    }
}
