package type;

public enum PessoaVinculo {
    ALUNO("Aluno"),
    PROFESSOR("Professor");

    private final String nomeFormatado;

    PessoaVinculo(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    public String getDisplayName() {
        return nomeFormatado;
    }
}
