package dto;

public record CatalogoDTO(int id, String nome, String tipo) {
    public static CatalogoDTO create(int id, String nome) {
        return new CatalogoDTO(id, nome, ""); // Valor padrão para tipo
    }
}
