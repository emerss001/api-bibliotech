ALTER TABLE Emprestimo
DROP COLUMN staus,
DROP COLUMN devolvido,
DROP COLUMN renovado,
ADD COLUMN status ENUM('Aprovado', 'Pendente', 'Devolvido', 'Renovado', 'Rejeitado') NOT NULL DEFAULT 'Pendente';