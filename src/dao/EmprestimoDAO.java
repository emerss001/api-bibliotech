package dao;

import db.ConnectionDB;
import entity.pessoa.Aluno;
import exception.NullConnectionException;
import entity.catalogo.Catalogo;
import entity.material.Emprestimo;
import entity.material.Material;
import entity.pessoa.Pessoa;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public Integer addEmprestimo(Emprestimo emprestimo){
        String sqlCommand = "INSERT INTO Emprestimo (material_id, aluno_id, mensagem) VALUES (?, ?, ?)";
        String updateMaterial = "UPDATE Material_fisico mf JOIN Emprestimo e ON mf.id = e.material_id SET mf.disponibilidade = FALSE WHERE e.id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) return null;
            System.out.println(emprestimo.getMaterial().getId());
            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, emprestimo.getMaterial().getId());
            statement.setInt(2, emprestimo.getAluno().getId());
            statement.setString(3, emprestimo.getMensagem());
            int affectedRows = statement.executeUpdate();

            if (affectedRows < 0) throw new RuntimeException("Erro ao criar empréstimo");
            ResultSet emprestimoCriado = statement.getGeneratedKeys();
            emprestimoCriado.next();

            try (PreparedStatement stmtMaterial = connection.prepareStatement(updateMaterial)) {
                stmtMaterial.setInt(1, emprestimoCriado.getInt(1));
                stmtMaterial.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao atualizar a disponibilidade do material: " + e.getMessage());
            }

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);    // retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar emprestimo " + e.getMessage());
        }
        return null;
    }

    public void aprooveEmprestimo(Integer emprestimo, LocalDate dataDevolucao){
        String sqlCommand = "UPDATE Emprestimo SET data_emprestimo = NOW(), data_devolucao_prevista = ?, status = 'Aprovado' WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setDate(1, java.sql.Date.valueOf(dataDevolucao));
            statement.setInt(2, emprestimo);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nenhum empréstimo foi atualizado - ID não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao aprovar emprestimo" + e.getMessage());
        }
    }

    public void refuseEmprestimo(Integer idEmprestimo, String mensagem){
        String sqlCommand = "UPDATE Emprestimo SET status = 'Rejeitado', rejicao_motivo = ? WHERE id = ?";
        String sqlUpdateCommand = "UPDATE Material_fisico mf JOIN Emprestimo e ON mf.id = e.material_id SET mf.disponibilidade = TRUE WHERE e.id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new RuntimeException("Falha ao conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, mensagem);
            statement.setInt(2, idEmprestimo);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Nenhum empréstimo foi atualizado - ID não encontrado");
            }

            PreparedStatement statement2 = connection.prepareStatement(sqlUpdateCommand);
            statement2.setInt(1, idEmprestimo);
            int affectedRows2 = statement2.executeUpdate();

            if (affectedRows2 == 0) {
                throw new RuntimeException("A disponibilidade do material não foi atualizada - ID não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao rejeitar emprestimo: " + e.getMessage());
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

    public List<Emprestimo> getEmprestimosByAluno(Pessoa aluno) {
        String sqlCommand = """
    SELECT
        e.id,
        fm.nome AS formato,
        m.id as material_id,
        m.titulo AS titulo,
        m.autor,
        p.nome AS aluno,
        e.data_criacao,
        e.data_emprestimo,
        e.data_devolucao_prevista,
        e.data_devolucao_real,
        e.status,
        e.rejicao_motivo
    FROM
        Emprestimo AS e
    JOIN
        Material_fisico AS mf ON mf.id = e.material_id
    JOIN
        Material AS m ON m.id = mf.material_id
    JOIN
        Formato_material AS fm ON fm.id = m.formato_material
    JOIN
        Pessoa AS p ON p.id = m.cadastrado_por
    WHERE
        e.aluno_id = ?;
    """;
        List<Emprestimo> emprestimos = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, aluno.getId());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");

                Aluno criador = new Aluno(rs.getString("aluno"));

                Material material = new Material();
                material.setFormato(new Catalogo(null, rs.getString("formato")));
                material.setTitulo(rs.getString("titulo"));
                material.setAutor(rs.getString("autor"));
                material.setId(rs.getInt("material_id"));

                Date dataCriacao = rs.getDate("data_criacao");
                Date dataEmprestimo = rs.getDate("data_emprestimo");
                Date dataDevolucaoPrevista = rs.getDate("data_devolucao_prevista");
                Date dataDevolucaoReal = rs.getDate("data_devolucao_real");

                String status = rs.getString("status");
                String mensagemRejeicao = rs.getString("rejicao_motivo");

                emprestimos.add(new Emprestimo(id, criador, material, dataCriacao, dataEmprestimo, dataDevolucaoPrevista, dataDevolucaoReal, status, mensagemRejeicao));
            }

            return emprestimos;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
