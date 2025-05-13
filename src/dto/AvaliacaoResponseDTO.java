package dto;

import java.util.Date;

public record AvaliacaoResponseDTO(Integer id, String aluno, double nota, String avaliacao, String data) {
}
