package dao;

import db.ConnectionDB;
import exception.NullConnectionException;
import model.material.Material;
import model.material.MaterialDigital;
import model.material.MaterialFisico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public Integer addMaterial(Material material) {
        String sqlCommand = "INSERT INTO Material (autor, titulo, formato_material_id, area_conhecimento_id, nivel_conhecimento, descricao, cadastrado_por, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, material.getAutor());
            statement.setString(2, material.getTitulo());
            statement.setInt(3, material.getFormato());
            statement.setInt(4, material.getArea());
            statement.setString(5, material.getNivel());
            statement.setString(6, material.getDescricao());
            statement.setString(7, material.getCadastradoPor());
            statement.setString(8, material.getTipo());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);    // retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar material: " + e.getMessage());
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

    public void cadastrarMaterialFisico(MaterialFisico materialFisico, Integer idMaterial){
        String sqlCommand = "INSERT INTO Material_fisico (material_id, disponibilidade) VALUES (?, true)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, String.valueOf(idMaterial));

            statement.executeUpdate();

            materialFisico.setId(idMaterial);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Material> buscarTodosMateriais(int limiteInferior, int limiteSuperior) {
        String sqlCommand = "SELECT * FROM Material LIMIT ?, ?";
        List<Material> materiais = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, limiteInferior);
            statement.setInt(2, limiteSuperior);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                Integer formato = rs.getInt("formato_material");
                Integer area = rs.getInt("area_conhecimento");
                String descricao = rs.getString("descricao");
                String nivel = rs.getString("nivel_conhecimento");

                materiais.add(new Material(id, titulo, formato, area, descricao, nivel));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return materiais;
    }

    public Material buscarDetalhesMaterial(int idMaterial) {
        String sqlCommand = "CALL GetMaterialCompleto(?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, idMaterial);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String autor = rs.getString("autor");
                String tipo = rs.getString("tipo");
                String titulo = rs.getString("titulo");
                Integer formato = rs.getInt("formato_material");
                Integer area = rs.getInt("area_conhecimento");
                String nivel = rs.getString("nivel_conhecimento");
                String descricao = rs.getString("descricao");
                String cadastradoPor = rs.getString("cadastrado_por");
                Double nota = rs.getDouble("nota");
                int quantidadeAvaliacoes = rs.getInt("quantidade_avaliacao");

                if (tipo.equals("Digital")) {
                    String url = rs.getString("link");
                    return new MaterialDigital(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes, url);
                }

                boolean disponibilidade = rs.getBoolean("disponibilidade");
                return new MaterialFisico(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes, disponibilidade);
            } else {
                System.out.println("Nenhum resultado retornado pela procedure.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean materialValido(Integer materialId){
        String sqlCommand = "SELECT 1 FROM Material M JOIN Material_fisico MF ON M.id = MF.material_id WHERE M.id = ? AND MF.disponibilidade = true";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, materialId);
            ResultSet rs = statement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar disponibilidade do material ID: " + materialId, e);
        }
    }


}