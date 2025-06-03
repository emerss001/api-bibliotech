alter table Aluno add column suspenso boolean default false;

SELECT
    COUNT(*) AS total_alunos,
    SUM(CASE WHEN suspenso = 0 THEN 1 ELSE 0 END) AS alunos_ativos,
    SUM(CASE WHEN suspenso = 1 THEN 1 ELSE 0 END) AS alunos_suspensos
FROM Aluno;
