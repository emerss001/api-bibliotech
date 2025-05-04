CREATE TABLE Bibliotecario
(
    pessoa_id INT PRIMARY KEY,
    codigo      VARCHAR(10),
    FOREIGN KEY (pessoa_id) REFERENCES Pessoa (id)
);