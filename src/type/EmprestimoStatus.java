package type;

public enum EmprestimoStatus {
    PENDENTE("Pendente"),
    APROVADO("Aprovado"),
    RENOVADO("Renovado"),
    DEVOLVIDO("Devolvido"),
    REJEITADO("Rejeitado");

    private final String nomeFormatado;

    EmprestimoStatus(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    public String getDisplayName() {
        return nomeFormatado;
    }

    public static EmprestimoStatus fromString(String status) {
        try {
            return EmprestimoStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Depois tenta bater com o nome formatado (tipo "Básico")
            for (EmprestimoStatus emprestimoStatus : EmprestimoStatus.values()) {
                if (emprestimoStatus.getDisplayName().equalsIgnoreCase(status)) {
                    return emprestimoStatus;
                }
            }
            throw new IllegalArgumentException("Status de empréstimo inválido: " + status);
        }
    }

}
