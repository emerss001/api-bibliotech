alter table Pessoa add column aprovado boolean default false;
ALTER TABLE Pessoa ADD COLUMN solicitado DATETIME DEFAULT CURRENT_TIMESTAMP;

-- adicionando on cascade em aluno
SELECT CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'Aluno' AND COLUMN_NAME = 'pessoa_id';

ALTER TABLE Aluno DROP FOREIGN KEY Aluno_ibfk_2;

ALTER TABLE Aluno
    ADD CONSTRAINT Aluno_ibfk_2
        FOREIGN KEY (pessoa_id) REFERENCES Pessoa(id)
            ON DELETE CASCADE;

-- adicionando on cascade em professor
SELECT CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'Professor' AND COLUMN_NAME = 'pessoa_id';

ALTER TABLE Professor DROP FOREIGN KEY Professor_ibfk_1;

ALTER TABLE Professor
    ADD CONSTRAINT Professor_ibfk_1
        FOREIGN KEY (pessoa_id) REFERENCES Pessoa(id)
            ON DELETE CASCADE;

-- adicionando on cascade em bibliotecário
SELECT CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'Bibliotecario' AND COLUMN_NAME = 'pessoa_id';

ALTER TABLE Bibliotecario DROP FOREIGN KEY Bibliotecario_ibfk_1;

ALTER TABLE Bibliotecario
    ADD CONSTRAINT Bibliotecario_ibfk_1
        FOREIGN KEY (pessoa_id) REFERENCES Pessoa(id)
            ON DELETE CASCADE;

