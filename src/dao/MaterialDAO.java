package dao;

import db.ConnectionDB;
import dto.ListarMateriaisDTO;
import dto.MateriaisFiltrosDTO;
import exception.NullConnectionException;
import model.material.Material;
import model.material.MaterialDigital;
import model.material.MaterialFisico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public Integer addMaterial(Material material) {
        String sqlCommand = "INSERT INTO Material (autor, titulo, formato_material, area_conhecimento, nivel_conhecimento, descricao, cadastrado_por, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            // Configura para retornar o ID gerado
            PreparedStatement statement = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, material.getAutor());
            statement.setString(2, material.getTitulo());
            statement.setInt(3, material.getFormato());
            statement.setInt(4, material.getArea());
            statement.setString(5, material.getNivel());
            statement.setString(6, material.getDescricao());
            statement.setString(7, material.getCadastradoPor());
            statement.setString(8, material.getTipo());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);    // retorna o ID gerado
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar material: " + e.getMessage());
        }
        return null;
    }

    public MaterialDigital cadastrarMaterialDigital(MaterialDigital materialDigital, Integer idMaterial) {
        String sqlCommand = "INSERT INTO Material_digital (material_id, link) VALUES (?, ?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, String.valueOf(idMaterial));
            statement.setString(2, materialDigital.getUrl());

            statement.executeUpdate();

            materialDigital.setId(idMaterial);
            return materialDigital;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cadastrarMaterialFisico(MaterialFisico materialFisico, Integer idMaterial){
        String sqlCommand = "INSERT INTO Material_fisico (material_id, disponibilidade) VALUES (?, true)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, String.valueOf(idMaterial));

            statement.executeUpdate();

            materialFisico.setId(idMaterial);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ListarMateriaisDTO> buscarTodosMateriais(int limiteInferior, int limiteSuperior, MateriaisFiltrosDTO filtros) {
        StringBuilder sqlBuilder = new StringBuilder("""
                SELECT
                    m.id,
                        Formato_material.nome as formato,
                        Area_conhecimento.nome as area,
                        m.titulo,
                        m.cadastrado_por,
                        m.descricao,
                        m.tipo,
                        m.nivel_conhecimento
                FROM Material as m
                JOIN Formato_material ON Formato_material.id = m.formato_material
                JOIN Area_conhecimento ON Area_conhecimento.id = m.area_conhecimento WHERE 1 = 1\s""");
        List<Object> parametros = new ArrayList<>();

        if (filtros.hasTipo()) {
            sqlBuilder.append(MateriaisFiltrosDTO.buildInClause("m.tipo", filtros.tipo().size()));
            parametros.addAll(filtros.tipo());
        }

        if (filtros.hasNivel()) {
            sqlBuilder.append(MateriaisFiltrosDTO.buildInClause("nivel_conhecimento", filtros.nivel().size()));
            parametros.addAll(filtros.nivel());
        }

        if (filtros.hasFormato()) {
            sqlBuilder.append(MateriaisFiltrosDTO.buildInClause("formato_material", filtros.formato().size()));
            parametros.addAll(filtros.formato());
        }

        if (filtros.hasArea()) {
            sqlBuilder.append(MateriaisFiltrosDTO.buildInClause("area_conhecimento", filtros.area().size()));
            parametros.addAll(filtros.area());
        }

        sqlBuilder.append(" LIMIT ?, ?");
        parametros.add(limiteInferior);
        parametros.add(limiteSuperior);

        System.out.println(sqlBuilder);

        List<ListarMateriaisDTO> materiais = new ArrayList<>();

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < parametros.size(); i++) {
                statement.setObject(i + 1, parametros.get(i));
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");
                String cadastrado_por = rs.getString("cadastrado_por");
                String titulo = rs.getString("titulo");
                String formato = rs.getString("formato");
                String area = rs.getString("area");
                String descricao = rs.getString("descricao");
                String nivel = rs.getString("nivel_conhecimento");
                String tipo = rs.getString("tipo");

                materiais.add(new ListarMateriaisDTO(id, cadastrado_por, titulo, formato, area, descricao, nivel, tipo));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return materiais;
    }

    public Material buscarDetalhesMaterial(int idMaterial) {
        String sqlCommand = "CALL GetMaterialCompleto(?)";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, idMaterial);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String autor = rs.getString("autor");
                String tipo = rs.getString("tipo");
                String titulo = rs.getString("titulo");
                Integer formato = rs.getInt("formato_material");
                Integer area = rs.getInt("area_conhecimento");
                String nivel = rs.getString("nivel_conhecimento");
                String descricao = rs.getString("descricao");
                String cadastradoPor = rs.getString("cadastrado_por");
                Double nota = rs.getDouble("nota");
                int quantidadeAvaliacoes = rs.getInt("quantidade_avaliacao");

                if (tipo.equals("Digital")) {
                    String url = rs.getString("link");
                    return new MaterialDigital(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes, url);
                }

                boolean disponibilidade = rs.getBoolean("disponibilidade");
                return new MaterialFisico(idMaterial, autor, tipo, titulo, formato, area, nivel, descricao, cadastradoPor, nota, quantidadeAvaliacoes, disponibilidade);
            } else {
                System.out.println("Nenhum resultado retornado pela procedure.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean materialValido(Integer materialId){
        String sqlCommand = "SELECT 1 FROM Material_fisico WHERE material_id = ? ";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, materialId);
            ResultSet rs = statement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar disponibilidade do material ID: " + materialId, e);
        }
    }


}