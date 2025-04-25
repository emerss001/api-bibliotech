package dao;

import db.ConnectionDB;
import exception.NullConnectionException;
import exception.UserNotFound;
import model.pessoa.Aluno;
import model.pessoa.Pessoa;
import model.pessoa.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PessoaDAO {
    public Professor addProfessor(Professor professor) {
        int pessoaId = addPessoa(professor);
        String sqlCommand = "INSERT INTO Professor (pessoa_id, siap) VALUES (?, ?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, String.valueOf(pessoaId));
            statement.setString(2, professor.getSiap());

            statement.executeUpdate();

            professor.setId(pessoaId);
            return professor;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Aluno addAluno(Aluno aluno) {
        int pessoaId = addPessoa(aluno);
        String sqlCommand = "INSERT INTO Aluno (pessoa_id, matricula, id_necessidade) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, String.valueOf(pessoaId));
            statement.setString(2, aluno.getMatricula());
            statement.setInt(3, aluno.getIdNecessidade());

            statement.executeUpdate();

            aluno.setId(pessoaId);
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

   public int addPessoa(Pessoa novaPessoa) {
       String sqlCommand = "INSERT INTO Pessoa (nome, email, senha) VALUES (?, ?, ?)";

       try (Connection connection = ConnectionDB.getConnection()) {
           if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

           PreparedStatement statement = connection.prepareStatement(sqlCommand, PreparedStatement.RETURN_GENERATED_KEYS);
           statement.setString(1, novaPessoa.getNome());
           statement.setString(2, novaPessoa.getEmail());
           statement.setString(3, novaPessoa.getHashSenha());

           int affectedRows = statement.executeUpdate();

           if (affectedRows > 0) {
               try (ResultSet rs = statement.getGeneratedKeys()) {
                   if (rs.next()) {
                       return rs.getInt(1); // retorna o ID gerado
                   }
               }
           }

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       throw new RuntimeException("Não foi possível adicionar a pessoa.");
   }

   public int getMaxIdNecessidade() {
        String sqlCommand = "SELECT MAX(id) FROM Necessidade";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
   }

   public boolean emailExiste(String email) {
	   String sqlCommand = "SELECT COUNT(*) FROM Pessoa WHERE email = ?";

       try (Connection connection = ConnectionDB.getConnection()){
           if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

           PreparedStatement statement = connection.prepareStatement(sqlCommand);
           statement.setString(1, email);
           ResultSet resultSet = statement.executeQuery();

           if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return false;
   }

    public Pessoa buscarPorEmail(String email) {
        String sqlPessoa = "SELECT * FROM Pessoa WHERE email = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sqlPessoa);
            stmt.setString(1, email);
            ResultSet rsPessoa = stmt.executeQuery();

            if (rsPessoa.next()) {
                System.out.println(rsPessoa.getString("senha"));
                int id = rsPessoa.getInt("id");
                String nome = rsPessoa.getString("nome");
                String senha = rsPessoa.getString("senha");

                // Verifica se é Aluno
                String sqlAluno = "SELECT * FROM Aluno WHERE pessoa_id = ?";
                PreparedStatement stmtAluno = connection.prepareStatement(sqlAluno);
                stmtAluno.setInt(1, id);
                ResultSet rsAluno = stmtAluno.executeQuery();

                if (rsAluno.next()) {
                    String matricula = rsAluno.getString("matricula");
                    int idNecessidade = rsAluno.getInt("id_necessidade");

                    return new Aluno(nome, email, matricula, senha, idNecessidade, true);
                }

                // Verifica se é Professor
                String sqlProf = "SELECT * FROM Professor WHERE pessoa_id = ?";
                PreparedStatement stmtProf = connection.prepareStatement(sqlProf);
                stmtProf.setInt(1, id);
                ResultSet rsProf = stmtProf.executeQuery();

                if (rsProf.next()) {
                    String siap = rsProf.getString("siap");
                    return new Professor(nome, email, siap, senha, true);
                }

                throw new UserNotFound("Usuário não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por email", e);
        }

        return null;
    }

}
