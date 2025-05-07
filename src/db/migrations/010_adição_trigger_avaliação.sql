-- TRIGGER: Atualizar média ao inserir avaliação
CREATE TRIGGER atualizar_media_avaliacao
AFTER INSERT ON Avaliacao
FOR EACH ROW
BEGIN
    DECLARE media_nota DECIMAL(3,1);
    DECLARE total_avaliacoes INT;

    SELECT AVG(nota), COUNT(*) INTO media_nota, total_avaliacoes
    FROM Avaliacao
    WHERE material_id = NEW.material_id;

    UPDATE Material
    SET nota = media_nota,
        quantidade_avaliacao = total_avaliacoes
    WHERE id = NEW.material_id;
END //

-- TRIGGER: Atualizar média ao deletar avaliação
CREATE TRIGGER atualizar_media_avaliacao_delete
AFTER DELETE ON Avaliacao
FOR EACH ROW
BEGIN
    DECLARE media_nota DECIMAL(3,1);
    DECLARE total_avaliacoes INT;

    SELECT AVG(nota), COUNT(*) INTO media_nota, total_avaliacoes
    FROM Avaliacao
    WHERE material_id = OLD.material_id;

    IF total_avaliacoes = 0 THEN
        UPDATE Material
        SET nota = NULL,
            quantidade_avaliacao = 0
        WHERE id = OLD.material_id;
    ELSE
        UPDATE Material
        SET nota = media_nota,
            quantidade_avaliacao = total_avaliacoes
        WHERE id = OLD.material_id;
    END IF;
END //

DELIMITER ;