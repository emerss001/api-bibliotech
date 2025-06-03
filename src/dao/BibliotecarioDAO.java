package dao;

import db.ConnectionDB;
import dto.bibliotecario.*;
import exception.NullConnectionException;
import model.catalogo.Catalogo;
import model.material.Material;

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
            WHEN m.pessoa_id IS NOT NULL THEN 'Bibliotecário'
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

    public MetricasMaterias buscarMetricasMateriais() {
        String sqlCommand = """
    SELECT
        -- Dados para o primeiro card (Total de Materiais, Físicos e Digitais)
        (SELECT COUNT(DISTINCT m.id) FROM Material m) AS total_materiais,
        (SELECT COUNT(DISTINCT m.id) FROM Material m WHERE m.tipo = 'Fisico') AS materiais_fisicos,
        (SELECT COUNT(DISTINCT m.id) FROM Material m WHERE m.tipo = 'Digital') AS materiais_digitais,

        -- Dados para o segundo card (Materiais Físicos Disponíveis/Indisponíveis)
        (
            SELECT COUNT(DISTINCT m.id)
            FROM Material m
            WHERE m.tipo = 'Fisico'
              AND EXISTS (
                SELECT 1 FROM Material_fisico mf
                WHERE mf.material_id = m.id AND mf.disponibilidade = 1
            )
        ) AS disponibilidade,
        (
            SELECT COUNT(DISTINCT m.id)
            FROM Material m
            WHERE m.tipo = 'Fisico'
              AND NOT EXISTS (
                SELECT 1 FROM Material_fisico mf
                WHERE mf.material_id = m.id AND mf.disponibilidade = 1
            )
        ) AS indisponibilidade,

        -- Dados para o terceiro card (Downloads e Empréstimos)
        (SELECT SUM(uso) FROM Material where tipo = 'Digital') AS downloads_total,
        (SELECT sum(uso) FROM Material WHERE tipo  = 'Fisico') AS emprestimos_total,

        -- Dados para o quarto card (Avaliações)
        (SELECT AVG(nota) FROM Material WHERE nota IS NOT NULL) AS media_avaliacao,
        (SELECT SUM(quantidade_avaliacao) FROM Material WHERE nota IS NOT NULL) AS total_avaliacoes;
    """;

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int totalMateriais = rs.getInt("total_materiais");
                int materiaisFisicos = rs.getInt("materiais_fisicos");
                int materiaisDigitais = rs.getInt("materiais_digitais");
                int disponibilidade = rs.getInt("disponibilidade");
                int indisponibilidade = rs.getInt("indisponibilidade");
                int downloadsTotal = rs.getInt("downloads_total");
                int emprestimosTotal = rs.getInt("emprestimos_total");
                int mediaAvaliacao = rs.getInt("media_avaliacao");
                int totalAvaliacoes = rs.getInt("total_avaliacoes");

                return new MetricasMaterias(totalMateriais, materiaisFisicos, materiaisDigitais, disponibilidade, indisponibilidade, downloadsTotal, emprestimosTotal, mediaAvaliacao,totalAvaliacoes);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Material> buscarMateriais() {
        String sqlCommand = """
    SELECT
        m.id,
        m.titulo,
        m.tipo,
        m.adicionado_em,
        fm.nome AS formato,
        ac.nome AS area,
        m.autor,
        m.nota,
        m.quantidade_avaliacao,
        m.uso,
        m.listado
    FROM
        Material m
    JOIN
        Formato_material fm ON fm.id = m.formato_material
    JOIN
        Area_conhecimento ac ON ac.id = m.area_conhecimento;
    """;
        List<Material> materials = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String tipo = rs.getString("tipo");
                Date adicionado = rs.getDate("adicionado_em");
                String formato = rs.getString("formato");
                String area = rs.getString("area");
                String autor = rs.getString("autor");
                double nota = rs.getDouble("nota");
                int avaliacoes = rs.getInt("quantidade_avaliacao");
                int uso = rs.getInt("uso");
                boolean listado = rs.getBoolean("listado");

                Material material = new Material();
                material.setId(id);
                material.setTitulo(titulo);
                material.setTipo(tipo);
                material.setAdicionado(adicionado);
                material.setListado(listado);

                Catalogo formatoMaterial = new Catalogo(null, formato);
                Catalogo areaConhecimento = new Catalogo(null, area);

                material.setFormato(formatoMaterial);
                material.setArea(areaConhecimento);
                material.setAutor(autor);
                material.setNota(nota);
                material.setQuantidadeAvaliacoes(avaliacoes);
                material.setUso(uso);

                materials.add(material);
            }

            return materials;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletarMaterial(Integer id) {
        // Primeiro verifica o tipo do material
        String sqlCheck = "SELECT tipo FROM Material WHERE id = ?";
        String sqlDelete = "DELETE FROM Material WHERE id = ? AND tipo = 'Digital'";

        try (Connection connection = ConnectionDB.getConnection()) {
            // Verifica o tipo do material
            PreparedStatement checkStatement = connection.prepareStatement(sqlCheck);
            checkStatement.setInt(1, id);
            ResultSet rs = checkStatement.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                if ("Fisico".equals(tipo)) {
                    throw new RuntimeException("Não é permitido excluir materiais físicos diretamente.");
                }
            } else {
                throw new RuntimeException("Material não encontrado com o ID: " + id);
            }

            // Se não for físico, prossegue com a exclusão
            PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete);
            deleteStatement.setInt(1, id);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected < 1) {
                throw new RuntimeException("Nenhum material digital foi deletado (ID inválido ou já removido).");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao acessar o banco de dados: " + e.getMessage(), e);
        }
    }

    public void ocultarMaterial(Integer id) {
        String sqlCommand = "update Material set listado = false where id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected < 1) throw new RuntimeException("Nenhum material foi atualizado");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void listarMaterial(Integer id) {
        String sqlCommand = "update Material set listado = true where id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected < 1) throw new RuntimeException("Nenhum material foi atualizado");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MetricasAlunos buscarMetricasAlunos() {
        String sqlCommand = """
    
                SELECT
        COUNT(*) AS total_alunos,
        SUM(CASE WHEN suspenso = 0 THEN 1 ELSE 0 END) AS alunos_ativos,
        SUM(CASE WHEN suspenso = 1 THEN 1 ELSE 0 END) AS alunos_suspensos
    FROM Aluno
    join Pessoa on Aluno.pessoa_id = Pessoa.id
    where Pessoa.aprovado = true;
    """;

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int totalAlunos = rs.getInt("total_alunos");
                int ativos = rs.getInt("alunos_ativos");
                int suspensos = rs.getInt("alunos_suspensos");

                return new MetricasAlunos(totalAlunos, ativos, suspensos);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<AlunosCadastradosDTO> buscarAlunos() {
        String sqlCommand = """
    SELECT
        p.id,
        p.nome,
        p.email,
        a.matricula,
        n.nome AS necessidade,
        a.suspenso,
        COUNT(e.id) AS total_emprestimos,
        SUM(CASE WHEN e.status = 'Devolvido' THEN 1 ELSE 0 END) AS emprestimos_devolvidos
    FROM Aluno a
    JOIN Pessoa p ON a.pessoa_id = p.id
    JOIN Necessidade n ON n.id = a.id_necessidade
    LEFT JOIN Emprestimo e ON e.aluno_id = a.pessoa_id
    WHERE p.aprovado = true
    GROUP BY p.id, p.nome, p.email, a.matricula, n.nome, a.suspenso;
    """;
        List<AlunosCadastradosDTO> alunos = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String matricula = rs.getString("matricula");
                String necessidade = rs.getString("necessidade");
                boolean suspenso = rs.getBoolean("suspenso");
                int totalEmprestimos = rs.getInt("total_emprestimos");
                int emprestimosFinalizados = rs.getInt("emprestimos_devolvidos");

                alunos.add(new AlunosCadastradosDTO(id, nome, email, matricula, necessidade, suspenso, totalEmprestimos, emprestimosFinalizados));
            }

            return alunos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void suspenderALuno(Integer id) {
        String sqlCommand = "update Aluno set suspenso = true where Aluno.pessoa_id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected < 1) throw new RuntimeException("Nenhum aluno foi atualizado");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ativarALuno(Integer id) {
        String sqlCommand = "update Aluno set suspenso = false where Aluno.pessoa_id = ?";

        try (Connection connection = ConnectionDB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected < 1) throw new RuntimeException("Nenhum aluno foi atualizado");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}

