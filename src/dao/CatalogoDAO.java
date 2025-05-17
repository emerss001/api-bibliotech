package dao;

import db.ConnectionDB;
import dto.CatalogoDTO;
import model.catalogo.Catalogo;

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

        try (Connection connection = ConnectionDB.getConnection()) {
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

        try (Connection connection = ConnectionDB.getConnection()) {
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

        try (Connection connection = ConnectionDB.getConnection()) {
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

    public Catalogo catalogoExiste(Integer id, String classe) {
        boolean isFormato = classe.equals("formato");
        String sqlCommand = "";

        if (isFormato) {
            sqlCommand = "SELECT * FROM Formato_material WHERE id = ?";
        } else {
            sqlCommand = "SELECT * FROM Area_conhecimento WHERE id = ?";
        }

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int idCatalogo = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                if (isFormato) {
                    String tipo = resultSet.getString("tipo");
                    return new Catalogo(idCatalogo, nome, tipo);
                }

                return new Catalogo(idCatalogo, nome);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao catálogo: " + e.getMessage());
        }

        return null;
    }

}
