package dao;

import db.ConnectionDB;
import dto.bibliotecario.CadastrosPendentesDTO;
import dto.bibliotecario.EmprestimosPendentesDTO;
import dto.bibliotecario.MetricasDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecarioDAO {
    public List<EmprestimosPendentesDTO> buscarEmprestimos() {
        String sqlCommand = """
    SELECT
        e.id,
        m.titulo,
        fm.nome AS formato_material,
        e.data_criacao,
        p.nome,
        p.email,
        n.nome AS necessidade,
        e.mensagem
    FROM Emprestimo AS e
    JOIN Material_fisico AS mf ON e.material_id = mf.id
    JOIN Material AS m ON m.id = mf.material_id
    JOIN Formato_material AS fm ON fm.id = m.formato_material
    JOIN Pessoa AS p ON p.id = e.aluno_id
    JOIN Aluno AS a ON a.pessoa_id = e.aluno_id
    JOIN Necessidade AS n ON n.id = a.id_necessidade
    WHERE e.status = 'Pendente';
    """;
        List<EmprestimosPendentesDTO> emprestimos = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String formatoMaterial = rs.getString("formato_material");
                Date dataCriacao = rs.getDate("data_criacao");
                String solicitanteNome = rs.getString("nome");
                String solicitanteEmail = rs.getString("email");
                String solicitanteNecessidade = rs.getString("necessidade");
                String solicitanteJustificativa = rs.getString("mensagem");

                emprestimos.add(new EmprestimosPendentesDTO(id, titulo, formatoMaterial, dataCriacao, solicitanteNome, solicitanteEmail, solicitanteNecessidade, solicitanteJustificativa));
            }

            return emprestimos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CadastrosPendentesDTO> buscarCadastros() {
        String sqlCommand = """
    SELECT
        p.id,
        p.nome AS nome,
        p.email,
        p.solicitado,
        -- Vínculo
        CASE
            WHEN a.pessoa_id IS NOT NULL THEN 'Aluno'
            WHEN pr.pessoa_id IS NOT NULL THEN 'Professor'
            WHEN m.pessoa_id IS NOT NULL THEN 'Moderador'
            ELSE 'Desconhecido'
        END AS vinculo,
        n.nome AS necessidade,
        -- Campo único para identificador
        CASE
            WHEN a.matricula IS NOT NULL THEN a.matricula
            WHEN pr.siap IS NOT NULL THEN pr.siap
            WHEN m.codigo IS NOT NULL THEN m.codigo
            ELSE NULL
        END AS identificador
    FROM Pessoa p
    LEFT JOIN Aluno a ON a.pessoa_id = p.id
    LEFT JOIN Professor pr ON pr.pessoa_id = p.id
    LEFT JOIN Bibliotecario m ON m.pessoa_id = p.id
    LEFT JOIN Necessidade n ON n.id = a.id_necessidade
    WHERE p.aprovado = FALSE;
    """;
        List<CadastrosPendentesDTO> cadastros = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String vinculo = rs.getString("vinculo");
                String necessidade = rs.getString("necessidade");
                String identificador = rs.getString("identificador");
                Date solicitado = rs.getDate("solicitado");

                cadastros.add(new CadastrosPendentesDTO(id, nome, email, vinculo, necessidade, identificador, solicitado));
            }

            return cadastros;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MetricasDTO buscarMetricas() {
        String sqlCommand = "SELECT (SELECT COUNT(*) FROM Pessoa WHERE aprovado = TRUE) AS usuarios, (SELECT COUNT(*) FROM Material) AS materiais";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int materiais = rs.getInt("materiais");
                int usuarios = rs.getInt("usuarios");

                return new MetricasDTO(materiais, usuarios);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean aprovarCadastro(int pessoaId) {
        String sqlCommand = "update Pessoa set aprovado = true where id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, pessoaId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Nenhuma pessoa foi aprovada. Verifique o ID.");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean rejeitarCadastro(int pessoaId) {
        String sqlCommand = "delete from Pessoa where aprovado = false and id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, pessoaId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Nenhuma pessoa foi rejeitada. Verifique o ID.");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
