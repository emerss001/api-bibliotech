alter table Material add column uso int default 0;
alter table Material add column listado boolean default true;

-- adicionando on cascade em avaliação
drop table Avaliacao;
create table Avaliacao (
    id          int auto_increment primary key,
    material_id int                                not null,
    aluno_id    int                                not null,
    nota        int                                not null,
    avaliacao   varchar(200)                       not null,
    data        datetime default CURRENT_TIMESTAMP not null,
    foreign key (material_id) references Material (id) on delete cascade,
    foreign key (aluno_id) references Aluno (pessoa_id) on delete cascade
);

-- adicionando on cascade em Material Digital
SELECT CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'Material_digital' AND COLUMN_NAME = 'material_id';

ALTER TABLE Material_digital DROP FOREIGN KEY Material_digital_ibfk_1;
ALTER TABLE Material_digital
    ADD CONSTRAINT Material_digital_ibfk_1
        FOREIGN KEY (material_id) REFERENCES Material(id)
            ON DELETE CASCADE;

-- adicionando on cascade em Material Digital
SELECT CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'Material_fisico' AND COLUMN_NAME = 'material_id';

ALTER TABLE Material_fisico DROP FOREIGN KEY Material_fisico_ibfk_1;
ALTER TABLE Material_fisico
    ADD CONSTRAINT Material_fisico_ibfk_1
        FOREIGN KEY (material_id) REFERENCES Material(id)
            ON DELETE CASCADE;


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
            m.adicionado_em,
            f.nome as formato_material,
            a.nome as area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            p.nome as cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            MAX(mf.disponibilidade) AS disponibilidade
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


