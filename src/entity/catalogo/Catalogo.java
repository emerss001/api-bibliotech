package entity.catalogo;

public class Catalogo {
    private Integer id;
    private String name;
    private String tipo;

    public Catalogo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Catalogo(Integer id, String name, String tipo) {
        this.id = id;
        this.name = name;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
