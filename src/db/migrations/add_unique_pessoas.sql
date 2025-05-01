ALTER TABLE Aluno
    MODIFY COLUMN matricula VARCHAR(20) NOT NULL,
    ADD CONSTRAINT unique_matricula UNIQUE (matricula);

ALTER TABLE Professor
    MODIFY COLUMN siap VARCHAR(10) NOT NULL,
    ADD CONSTRAINT unique_siap UNIQUE (siap);

