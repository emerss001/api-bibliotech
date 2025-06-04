package dto.bibliotecario;

import java.util.Date;

public record EmprestimosPendentesDTO(
        Integer id,
        String materialTitulo,
        String formato,
        Date solicitado,
        String solicitanteNome,
        String solicitanteEmail,
        String solicitanteNecessidade,
        String solicitanteJustificativa,
        String capaUrl) {
}
