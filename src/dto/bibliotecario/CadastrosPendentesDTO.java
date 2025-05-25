package dto.bibliotecario;

import java.sql.Date;

public record CadastrosPendentesDTO(int id, String nome, String email, String vinculo, String necessidade, String identificador, Date solicitado) {
}
