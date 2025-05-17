drop procedure GetMaterialCompleto;

create procedure GetMaterialCompleto(IN mat_id int)
BEGIN
    DECLARE mat_tipo ENUM('Fisico', 'Digital');

    -- Buscar o tipo do material
    SELECT tipo INTO mat_tipo
    FROM Material
    WHERE id = mat_id;

    -- Verifica e executa o SELECT apropriado
    IF mat_tipo = 'Fisico' THEN
        SELECT DISTINCT
            m.autor,
            m.tipo,
            m.titulo,
            f.nome as formato_material,
            a.nome as area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            p.nome as cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            mf.disponibilidade
        FROM Material m
                 JOIN Material_fisico mf ON m.id = mf.material_id
                join Formato_material AS f on f.id = m.formato_material
                join Area_conhecimento AS a on a.id = m.area_conhecimento
                join Pessoa as p on p.id = m.cadastrado_por
        WHERE m.id = mat_id;

    ELSEIF mat_tipo = 'Digital' THEN
        SELECT
            m.autor,
            m.tipo,
            m.titulo,
            f.nome as formato_material,
            a.nome as area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            p.nome as cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            md.link
        FROM Material m
                 JOIN Material_digital md ON m.id = md.material_id
                 join Formato_material AS f on f.id = m.formato_material
                 join Area_conhecimento AS a on a.id = m.area_conhecimento
                 join Pessoa as p on p.id = m.cadastrado_por
        WHERE m.id = mat_id;

    ELSE
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Tipo de material desconhecido ou material não encontrado';
    END IF;
END;

