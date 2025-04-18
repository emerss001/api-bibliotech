package dao;

import db.ConnectionDB;
import model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
