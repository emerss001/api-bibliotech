package dao;

import db.ConnectionDB;
import model.material.Emprestimo;

import java.sql.*;

public class EmprestimoDAO {

    public Integer addEmprestimo(Emprestimo emprestimo){
        String sqlCommand = "INSERT INTO Emprestimo (material_id, aluno_id, devolvido, renovado, staus) VALUES (?, ?, false, false, 'Pedente')";
        String updateMaterial = "UPDATE Material_fisico SET disponibilidade = false WHERE material_id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, emprestimo.getMaterialId());
            statement.setInt(2, emprestimo.getAlunoId());
            int affectedRows = statement.executeUpdate();

            try (PreparedStatement stmtMaterial = connection.prepareStatement(updateMaterial)) {
                stmtMaterial.setInt(1, emprestimo.getMaterialId());
                stmtMaterial.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao atualizar a disponibilidade do material", e);
            }

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);    // retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar emprestimo", e);
        }
        return null;
    }
}
