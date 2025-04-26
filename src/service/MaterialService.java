package service;

import dao.MaterialDAO;
import dto.NovoMaterialDTO;
import model.material.Material;
import model.material.MaterialDigital;

import java.util.Objects;

public class MaterialService {
    private final MaterialDAO materialDAO;

    public MaterialService(MaterialDAO materialDAO) {
        this.materialDAO = Objects.requireNonNull(materialDAO, "DAO não pode ser nulo");
    }

    public Material addMaterialDigital(NovoMaterialDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Dados do material inválidos");

        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        MaterialDigital materialDigital = new MaterialDigital(dto);
        materialDigital.cadastrarMaterialDigital(materialDAO);
        return materialDigital;
    }
}
