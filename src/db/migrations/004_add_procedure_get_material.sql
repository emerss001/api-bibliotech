CREATE PROCEDURE GetMaterialCompleto(IN mat_id INT)
BEGIN
    DECLARE mat_tipo ENUM('Fisico', 'Digital');

    -- Buscar o tipo do material
    SELECT tipo INTO mat_tipo
    FROM Material
    WHERE id = mat_id;

    -- Verifica e executa o SELECT apropriado
    IF mat_tipo = 'Fisico' THEN
        SELECT
            m.autor,
            m.tipo,
            m.titulo,
            m.formato_material,
            m.area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            m.cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            mf.quantidade
        FROM Material m
                 JOIN Material_fisico mf ON m.id = mf.material_id
        WHERE m.id = mat_id;

    ELSEIF mat_tipo = 'Digital' THEN
        SELECT
            m.autor,
            m.tipo,
            m.titulo,
            m.formato_material,
            m.area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            m.cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            md.link
        FROM Material m
                 JOIN Material_digital md ON m.id = md.material_id
        WHERE m.id = mat_id;

    ELSE
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Tipo de material desconhecido ou material não encontrado';
    END IF;
END;