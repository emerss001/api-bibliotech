
create table Area_conhecimento
(
    id   int auto_increment primary key,
    nome varchar(100) not null
);

create table Formato_material
(
    id   int auto_increment primary key,
    nome varchar(100) not null
);

create table Necessidade
(
    id   int auto_increment
        primary key,
    nome varchar(100) not null
);

create table Material
(
    id  int auto_increment primary key,
    titulo               varchar(100)                                 not null,
    autor                varchar(100)                                 null,
    formato_material     int                                          not null,
    area_conhecimento    int                                          not null,
    nivel_conhecimento   enum ('Básico', 'Intermediário', 'Avançado') not null,
    nota                 double(3, 1)                                 null,
    adicionado_em        datetime default CURRENT_TIMESTAMP           not null,
    quantidade_avaliacao int                                          null,
    descricao            varchar(200)                                 not null,
    cadastrado_por       varchar(100)                                 null,
    tipo                 enum ('Fisico', 'Digital')                   not null,
    foreign key (formato_material) references Formato_material (id),
    foreign key (area_conhecimento) references Area_conhecimento (id)
);


create table Material_digital
(
    material_id int auto_increment primary key,
    link        varchar(200) not null,
    foreign key (material_id) references Material (id)
);

create table Material_fisico
(
    material_id     int auto_increment primary key,
    disponibilidade tinyint(1) not null,
    foreign key (material_id) references Material (id)
);

create table Pessoa
(
    id    int auto_increment primary key,
    nome  varchar(100) not null,
    email varchar(145) not null,
    senha char(60)     not null,
    constraint email unique (email)
);

create table Aluno
(
    pessoa_id      int primary key,
    matricula      varchar(20) not null unique,
    id_necessidade int null,
    foreign key (id_necessidade) references Necessidade (id),
    foreign key (pessoa_id) references Pessoa (id)
);

create table Bibliotecario
(
    pessoa_id int  not null primary key,
    codigo    varchar(10) null,
    foreign key (pessoa_id) references Pessoa (id)
);

create table Emprestimo
(
    id  int auto_increment primary key,
    material_id             int                          not null,
    aluno_id                int                          not null,
    data_emprestimo         datetime                     null,
    data_devolucao_prevista datetime                     null,
    data_devolucao_real     datetime                     null,
    devolvido               tinyint(1)                   not null,
    renovado                tinyint(1)                   not null,
    staus                   enum ('Aprovado', 'Pedente') not null,
    foreign key (material_id) references Material (id),
    foreign key (aluno_id) references Aluno (pessoa_id)
);

create table Professor
(
    pessoa_id int not null primary key,
    siap      varchar(10) not null unique,
    foreign key (pessoa_id) references Pessoa (id)
);

create procedure GetMaterialCompleto(IN mat_id int)
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
            mf.disponibilidade
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


alter table Formato_material add column tipo enum ('Fisico', 'Digital') not null;
