package dao;

import db.ConnectionDB;
import model.material.Avaliacao;

import java.sql.*;

public class AvaliacaoDAO {
    public Integer addAvaliacao(Avaliacao avaliacao) {
        String sqlCommand = "INSERT INTO Avaliacao (material_id, aluno_id, nota, avaliacao, data) VALUES (?, ?, ?, ?, NOW())";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, avaliacao.getMaterialId());
            statement.setInt(2, avaliacao.getAlunoId());
            statement.setInt(3, avaliacao.getNota());
            statement.setString(4, avaliacao.getAvaliacao());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);    // retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar avaliação: " + e.getMessage());
        }
        return null;
    }
}
