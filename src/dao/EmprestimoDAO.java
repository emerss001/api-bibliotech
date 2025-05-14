package dao;

import db.ConnectionDB;
import dto.EmprestimoFiltroDTO;
import model.material.Emprestimo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public Integer addEmprestimo(Emprestimo emprestimo){
        String sqlCommand = "INSERT INTO Emprestimo (material_id, aluno_id) VALUES (?, ?)";
        String updateMaterial = "UPDATE Material_fisico mf JOIN Emprestimo e ON mf.material_id = e.material_id SET mf.disponibilidade = FALSE WHERE e.id = ?";

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

    public void aprooveEmprestimo(Emprestimo emprestimo){
        String sqlCommand = "UPDATE Emprestimo SET data_emprestimo = NOW(), data_devolucao_prevista = ?, status = 'Aprovado' WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setTimestamp(1, Timestamp.valueOf(emprestimo.getDataDPrevista()));
            statement.setInt(2, emprestimo.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nenhum empréstimo foi atualizado - ID não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao aprovar emprestimo", e);
        }
    }

    public void refuseEmprestimo(Emprestimo emprestimo){
        String sqlCommand = "UPDATE Emprestimo SET status = 'Rejeitado' WHERE id = ?";
        String sqlUpdateCommand = "UPDATE Material_fisico mf JOIN Emprestimo e ON mf.material_id = e.material_id SET mf.disponibilidade = TRUE WHERE e.id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, emprestimo.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nenhum empréstimo foi atualizado - ID não encontrado");
            }

            PreparedStatement statement2 = connection.prepareStatement(sqlUpdateCommand);
            statement2.setInt(1, emprestimo.getId());
            int affectedRows2 = statement2.executeUpdate();

            if (affectedRows2 == 0) {
                throw new RuntimeException("A disponibilidade do material não foi atualizada - ID não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao rejeitar emprestimo", e);
        }
    }

    public void returnEmprestimo(Emprestimo emprestimo){
        String sqlCommand = "UPDATE Emprestimo SET data_devolucao_real = NOW(), status = 'Devolvido' WHERE id = ?";
        String sqlUpdateCommand = "UPDATE Material_fisico mf JOIN Emprestimo e ON mf.material_id = e.material_id SET mf.disponibilidade = TRUE WHERE e.id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, emprestimo.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nenhum empréstimo foi atualizado - ID não encontrado");
            }

            PreparedStatement statement2 = connection.prepareStatement(sqlUpdateCommand);
            statement2.setInt(1, emprestimo.getId());
            int affectedRows2 = statement2.executeUpdate();

            if (affectedRows2 == 0) {
                throw new RuntimeException("A disponibilidade do material não foi atualizada - ID não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao devolver emprestimo", e);
        }
    }

    public void renovateEmprestimo(Emprestimo emprestimo){
        String sqlCommand = "UPDATE Emprestimo SET data_devolucao_prevista = ?, status = 'Renovado' WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setTimestamp(1, Timestamp.valueOf(emprestimo.getDataDPrevista()));
            statement.setInt(2, emprestimo.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nenhum empréstimo foi atualizado - ID não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao renovar emprestimo", e);
        }
    }

    public void deleteEmprestimo(Integer id){
        String sqlCommand = "DELETE FROM Emprestimo WHERE id = ?";
        String sqlUpdateCommand = "UPDATE Material_fisico mf JOIN Emprestimo e ON mf.material_id = e.material_id SET mf.disponibilidade = TRUE WHERE e.id = ? AND e.status IN ('Aprovado','Pendente','Renovado')";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement2 = connection.prepareStatement(sqlUpdateCommand);
            statement2.setInt(1, id);
            int affectedRows2 = statement2.executeUpdate();

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nenhum empréstimo foi deletado - ID não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar emprestimo", e);
        }
    }

    public ArrayList<Emprestimo> readEmprestimo(EmprestimoFiltroDTO dto){
        ArrayList<Emprestimo> lista = new ArrayList<>();
        //StringBuilder sqlCommand = new StringBuilder("SELECT * FROM Emprestimo WHERE (aluno_id = ? OR ? IS NULL) AND (status = ? OR ? IS NULL) ");
        StringBuilder sqlCommand = new StringBuilder("SELECT * FROM Emprestimo WHERE 1 = 1\s ");

        List<Object> parametros = new ArrayList<>();

        if (dto.hasAlunoId()){
            sqlCommand.append(EmprestimoFiltroDTO.buildInClause("aluno_id", dto.alunoId().size()));
            parametros.addAll(dto.alunoId());
        }

        if (dto.hasStatus()){
            sqlCommand.append(EmprestimoFiltroDTO.buildInClause("status", dto.status().size()));
            parametros.addAll(dto.status());
        }

        if (dto.quantidade() != null){
            sqlCommand.append("LIMIT ").append(dto.quantidade());
        }

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand.toString());

            for (int i = 0; i < parametros.size(); i++) {
                statement.setObject(i + 1, parametros.get(i));
            }

            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                lista.add(new Emprestimo(
                        rs.getInt("aluno_id"),
                        rs.getInt("material_id"),
                        rs.getInt("id"),
                        rs.getString("data_emprestimo"),
                        rs.getString("data_devolucao_prevista"),
                        rs.getString("data_devolucao_real"),
                        rs.getString("status")
                ));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar emprestimos, verifique se a chave quantidade não excede o número de empréstimos", e);
        }
    }
}
