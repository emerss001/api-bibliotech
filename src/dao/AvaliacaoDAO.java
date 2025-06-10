package dao;

import db.ConnectionDB;
import dto.AvaliacaoResponseDTO;
import entity.material.Avaliacao;

import java.sql.*;
import java.util.ArrayList;

public class AvaliacaoDAO {
    public Integer addAvaliacao(Avaliacao avaliacao) {
        String sqlCommand = "INSERT INTO Avaliacao (material_id, aluno_id, nota, avaliacao, data) VALUES (?, ?, ?, ?, NOW())";
        String updateMatrial = """
    UPDATE Material
    SET
        nota = COALESCE((SELECT AVG(nota) FROM Avaliacao WHERE material_id = ?), 0),
        quantidade_avaliacao = COALESCE(quantidade_avaliacao, 0) + 1
    WHERE id = ?;
    """;

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, avaliacao.getMaterial().getId());
            statement.setInt(2, avaliacao.getAluno().getId());
            statement.setInt(3, avaliacao.getNota());
            statement.setString(4, avaliacao.getAvaliacao());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        PreparedStatement materialUpdate = connection.prepareStatement(updateMatrial);
                        materialUpdate.setInt(1, avaliacao.getMaterial().getId());
                        materialUpdate.setInt(2, avaliacao.getMaterial().getId());
                        materialUpdate.executeUpdate();

                        return rs.getInt(1);    // retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar avaliação: " + e.getMessage());
        }
        return null;
    }

    public void removeAvaliacao(Integer id) {
        String sqlCommand = "DELETE FROM Avaliacao WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) throw new RuntimeException("Nenhuma avaliação foi deletada - ID não encontrado");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar avaliação: " + e.getMessage());
        }
    }

    public ArrayList<AvaliacaoResponseDTO> readAvaliacao(Integer id) {
        ArrayList<AvaliacaoResponseDTO> lista = new ArrayList<>();
        String sqlCommand = "SELECT a.id, p.nome AS aluno, a.nota, a.avaliacao, a.data FROM Avaliacao AS a JOIN Pessoa AS p ON p.id = a.aluno_id WHERE a.material_id = ? order by a.data desc";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                lista.add(new AvaliacaoResponseDTO(
                        rs.getInt("id"),
                        rs.getString("aluno"),
                        rs.getInt("nota"),
                        rs.getString("avaliacao"),
                        rs.getString("data")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar avaliações do material: " + e.getMessage());
        }
    }
}
