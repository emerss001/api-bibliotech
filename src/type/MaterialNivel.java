package type;

import java.util.Arrays;

public enum MaterialNivel {
    BASICO("Básico"),
    INTERMEDIARIO("Intermediário"),
    AVANCADO("Avançado");

    private final String nomeFormatado;

    MaterialNivel(String nomeFormatado) {
        this.nomeFormatado  = nomeFormatado;
    }

    public String getDisplayName() {
        return nomeFormatado;
    }

    public static MaterialNivel fromString(String nivel) {
        try {
            return MaterialNivel.valueOf(nivel);
        } catch (IllegalArgumentException e) {
            // Depois tenta bater com o nome formatado (tipo "Básico")
            for (MaterialNivel materialNivel : MaterialNivel.values()) {
                if (materialNivel.getDisplayName().equalsIgnoreCase(nivel)) {
                    return materialNivel;
                }
            }
            throw new IllegalArgumentException("Nível de material inválido: " + nivel);
        }
    }

}
