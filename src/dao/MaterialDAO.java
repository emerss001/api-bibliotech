package dao;

import db.ConnectionDB;
import dto.material.ListarMateriaisDTO;
import dto.material.MateriaisFiltrosDTO;
import exception.NullConnectionException;
import model.catalogo.Catalogo;
import model.material.Material;
import model.material.MaterialDigital;
import model.material.MaterialFisico;
import model.pessoa.Pessoa;
import type.MaterialNivel;

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
            statement.setInt(3, material.getFormato().getId());
            statement.setInt(4, material.getArea().getId());
            statement.setString(5, material.getNivel());
            statement.setString(6, material.getDescricao());
            statement.setInt(7, material.getCadastradoPor().getId());
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

    public List<ListarMateriaisDTO> buscarTodosMateriais(MateriaisFiltrosDTO filtros) {
        StringBuilder sqlBuilder = new StringBuilder("""
                SELECT
                    m.id,
                    fm.nome as formato,
                    ar.nome as area,
                    m.titulo,
                    p.nome as cadastrado_por,
                    m.descricao,
                    m.tipo,
                    m.nivel_conhecimento
                FROM Material as m
                         JOIN Formato_material as fm ON fm.id = m.formato_material
                         JOIN Area_conhecimento as ar ON ar.id = m.area_conhecimento
                         JOIN Pessoa AS p on p.id = m.cadastrado_por
                WHERE 1 = 1""");
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
//            if (!rs.next()) throw new RuntimeException("Não foi encontrado material com o id " + idMaterial);

            if (rs.next()) {
                String autor = rs.getString("autor");
                String tipo = rs.getString("tipo");
                String titulo = rs.getString("titulo");
                String formato = rs.getString("formato_material");
                String area = rs.getString("area_conhecimento");
                String nivel = rs.getString("nivel_conhecimento");
                String descricao = rs.getString("descricao");
                String cadastradoPor = rs.getString("cadastrado_por");
                double nota = rs.getDouble("nota");
                int quantidadeAvaliacoes = rs.getInt("quantidade_avaliacao");

                if (tipo.equals("Digital")) {
                    String url = rs.getString("link");
                    Material material = new MaterialDigital(idMaterial, autor, tipo, titulo, MaterialNivel.fromString(nivel), descricao, nota, quantidadeAvaliacoes, url);
                    material.setFormato(new Catalogo(null, formato));
                    material.setArea(new Catalogo(null, area));
                    material.setCadastradoPor(new Pessoa(cadastradoPor));

                    return material;
                }

                boolean disponibilidade = rs.getBoolean("disponibilidade");
                Material material = new MaterialFisico(idMaterial, autor, tipo, titulo, MaterialNivel.fromString(nivel), descricao, nota, quantidadeAvaliacoes, disponibilidade);
                material.setFormato(new Catalogo(null, formato));
                material.setArea(new Catalogo(null, area));
                material.setCadastradoPor(new Pessoa(cadastradoPor));

                return material;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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

        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Erro ao verificar disponibilidade do material ID: " + materialId, e);
        }
    }

    public static boolean materialValidoAvaliacao(Integer materialId){
        String sqlCommand = "SELECT 1 FROM Material WHERE id = ? ";

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

    public Integer getDisponibilidade(Integer materialId) {
        String sqlCommand = "SELECT mf.id FROM Material_fisico as mf JOIN Material as m ON m.id = mf.material_id WHERE m.id = ? AND mf.disponibilidade = 1 LIMIT 1";

        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection == null) throw new NullConnectionException("Não foi possível conectar ao banco de dados");

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, materialId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}