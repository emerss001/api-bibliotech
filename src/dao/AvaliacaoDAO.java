package dao;

import com.google.gson.JsonObject;
import db.ConnectionDB2;
import model.material.Avaliacao;
import model.material.Emprestimo;

import java.sql.*;
import java.util.ArrayList;

public class AvaliacaoDAO {
    public Integer addAvaliacao(Avaliacao avaliacao) {
        String sqlCommand = "INSERT INTO Avaliacao (material_id, aluno_id, nota, avaliacao, data) VALUES (?, ?, ?, ?, NOW())";

        try (Connection connection = ConnectionDB2.getConnection()) {
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

    public void removeAvaliacao(Integer id) {
        String sqlCommand = "DELETE FROM Avaliacao WHERE id = ?";

        try (Connection connection = ConnectionDB2.getConnection()) {
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

    public ArrayList<Avaliacao> readAvaliacao(Integer id) {
        ArrayList<Avaliacao> lista = new ArrayList<>();
        String sqlCommand = "SELECT * FROM Avaliacao WHERE material_id = ?";

        try (Connection connection = ConnectionDB2.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                lista.add(new Avaliacao(
                        rs.getInt("id"),
                        rs.getInt("aluno_id"),
                        rs.getInt("material_id"),
                        rs.getInt("nota"),
                        rs.getString("avaliacao"),
                        rs.getString("data")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar avaliações do empréstimos", e);
        }
    }
}
