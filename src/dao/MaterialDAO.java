package dao;

import db.ConnectionDB;
import model.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    public List<Material> getAll() {
        String sqlCommand = "SELECT * FROM Material";
        List<Material> materials = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                materials.add(mapMaterial(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return materials;
    }

    private Material mapMaterial(ResultSet rs) throws SQLException {
        Material material = new Material();

        material.setId(rs.getInt("id"));
        material.setTitulo(rs.getString("titulo"));

        return material;
    }

    public Material addMaterial(Material material) {
        String sqlCommand = "INSERT INTO Material (titulo) VALUES (?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, material.getTitulo());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        material.setId(generatedKeys.getInt(1));
                        return material;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar material", e);
        }
        return null;
    }
}
