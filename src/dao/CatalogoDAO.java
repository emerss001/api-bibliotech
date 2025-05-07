package dao;

import db.ConnectionDB2;
import dto.CatalogoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogoDAO {
    public List<CatalogoDTO> getAreaConhecimento() {
        String sqlCommand = "SELECT * FROM Area_conhecimento";
        List<CatalogoDTO> areas = new ArrayList<>();

        try (Connection connection = ConnectionDB2.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                areas.add(CatalogoDTO.create(id, nome));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return areas;
    }

    public List<CatalogoDTO> getNecessidades() {
        String sqlCommand = "SELECT * FROM Necessidade";
        List<CatalogoDTO> areas = new ArrayList<>();

        try (Connection connection = ConnectionDB2.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                areas.add(CatalogoDTO.create(id, nome));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return areas;
    }

    public List<CatalogoDTO> getFormatosMateriais() {
        String sqlCommand = "SELECT * FROM Formato_material";
        List<CatalogoDTO> areas = new ArrayList<>();

        try (Connection connection = ConnectionDB2.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo");
                areas.add(new CatalogoDTO(id, nome, tipo));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return areas;
    }

}
