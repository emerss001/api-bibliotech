package dao;

import db.ConnectionDB;
import exception.NullConnectionException;
import model.material.Material;
import model.material.MaterialDigital;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MaterialDAO {

    public Integer addMaterial(Material material) {
        String sqlCommand = "INSERT INTO Material (titulo, formato_material, area_conhecimento, nivel_conhecimento, descricao) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, material.getTitulo());
            statement.setString(2, material.getFormato());
            statement.setString(3, material.getArea());
            statement.setString(4, material.getNivel());
            statement.setString(5, material.getDescricao());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);    // retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar material", e);
        }
        return null;
    }

    public MaterialDigital cadastrarMaterialDigital(MaterialDigital materialDigital, Integer idMaterial) {
        String sqlCommand = "INSERT INTO Material_digital (material_id, link) VALUES (?, ?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, String.valueOf(idMaterial));
            statement.setString(2, materialDigital.getUrl());

            statement.executeUpdate();

            materialDigital.setId(idMaterial);
            return materialDigital;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
