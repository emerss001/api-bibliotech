package type;

public enum PessoaVinculo {
    ALUNO("Aluno"),
    PROFESSOR("Professor"),
    BIBLIOTECARIO("Bibliotecario");

    private final String nomeFormatado;

    PessoaVinculo(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    public String getDisplayName() {
        return nomeFormatado;
    }
}
