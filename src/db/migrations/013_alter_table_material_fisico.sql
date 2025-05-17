drop table Material_fisico;

CREATE TABLE Material_fisico (
     id INT AUTO_INCREMENT PRIMARY KEY,
     material_id INT NOT NULL,
     disponibilidade TINYINT(1) NOT NULL,
     FOREIGN KEY (material_id) REFERENCES Material (id)
);