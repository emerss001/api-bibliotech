truncate table Material_digital;
delete from Avaliacao where '1' = '1';
delete from Emprestimo where '1' = '1';
truncate table Material_fisico;
delete from Material where '1' = '1';


ALTER TABLE Material
    MODIFY COLUMN cadastrado_por INT NULL,
    ADD CONSTRAINT fk_material_pessoa
        FOREIGN KEY (cadastrado_por) REFERENCES Pessoa(id);


