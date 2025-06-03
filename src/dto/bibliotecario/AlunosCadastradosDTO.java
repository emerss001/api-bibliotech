package dto.bibliotecario;

public record AlunosCadastradosDTO(Integer id, String nome, String email, String matricula, String necessidade, boolean suspenso, Integer emprestimosTotatis, Integer emprestimosFinalizados) {
}
