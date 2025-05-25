-- alterando procedure GetMaterialCompleto
drop procedure GetMaterialCompleto;

DELIMITER $$
CREATE
    DEFINER = user@`%`
    PROCEDURE GetMaterialCompleto(IN mat_id INT)
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
            f.nome AS formato_material,
            a.nome AS area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            p.nome AS cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            MAX(mf.disponibilidade) AS disponibilidade
        FROM Material m
                 JOIN Material_fisico mf ON m.id = mf.material_id
                 JOIN Formato_material AS f ON f.id = m.formato_material
                 JOIN Area_conhecimento AS a ON a.id = m.area_conhecimento
                 JOIN Pessoa AS p ON p.id = m.cadastrado_por
        WHERE m.id = mat_id
        GROUP BY m.id;

    ELSEIF mat_tipo = 'Digital' THEN
        SELECT
            m.autor,
            m.tipo,
            m.titulo,
            f.nome AS formato_material,
            a.nome AS area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            p.nome AS cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            md.link
        FROM Material m
                 JOIN Material_digital md ON m.id = md.material_id
                 JOIN Formato_material AS f ON f.id = m.formato_material
                 JOIN Area_conhecimento AS a ON a.id = m.area_conhecimento
                 JOIN Pessoa AS p ON p.id = m.cadastrado_por
        WHERE m.id = mat_id;

    ELSE
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Tipo de material desconhecido ou material não encontrado';
    END IF;
END$$

DELIMITER ;

-- Alterando tabela de empréstimos
alter table Emprestimo add column mensagem varchar(255) null;
alter table Emprestimo add column rejicao_motivo varchar(255) null;
