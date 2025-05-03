package dao;

import db.ConnectionDB;
import exception.NullConnectionException;
import model.pessoa.Aluno;
import model.pessoa.Pessoa;
import model.pessoa.Professor;
import type.PessoaVinculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PessoaDAO {
    public Professor addProfessor(Professor professor,  int pessoaId) {
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

    public Aluno addAluno(Aluno aluno, int pessoaId) {
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

   public Pessoa buscarPorIdentificador(PessoaVinculo userTipo, String identificador) {
        String sqlCommand;

        if (userTipo == PessoaVinculo.ALUNO) {
            sqlCommand = "SELECT pessoa_id, email, senha FROM Aluno join Pessoa on Aluno.pessoa_id = Pessoa.id WHERE matricula = ?";
        } else {
            sqlCommand = "SELECT pessoa_id, email, senha FROM Professor join Pessoa on Professor.pessoa_id = Pessoa.id WHERE siap = ?";
        }

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sqlCommand);
            stmt.setString(1, identificador);
            ResultSet rsPessoa = stmt.executeQuery();

            if (rsPessoa.next()) {
                int id = rsPessoa.getInt("pessoa_id");
                String email = rsPessoa.getString("email");
                String senha = rsPessoa.getString("senha");

                return new Pessoa(id, email, senha);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Erro ao buscar por email", e);
        }

       return null;
   }

    public Pessoa buscarPorEmail(String email) {
        String sqlPessoa = "SELECT * FROM Pessoa WHERE email = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sqlPessoa);
            stmt.setString(1, email);
            ResultSet rsPessoa = stmt.executeQuery();

            if (rsPessoa.next()) {
                int id = rsPessoa.getInt("id");
                String nome = rsPessoa.getString("nome");
                String senha = rsPessoa.getString("senha");

                return new Pessoa(senha, nome, email, true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por email", e);
        }
        return null;
    }

    public static boolean pessoaValido(Integer pessoaId){
        String sqlCommand = "SELECT 1 FROM Pessoa WHERE id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, pessoaId);
            ResultSet rs = statement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existencia do ID da pessoa ", e);
        }
    }
 }

