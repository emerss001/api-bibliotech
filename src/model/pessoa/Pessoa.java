package model.pessoa;

import org.mindrot.jbcrypt.BCrypt;

public class Pessoa {
    private static final int BCRYPT_COST = 12;

    private int id;
    private String senhaHash;
    private String nome;
    private String email;

    public Pessoa(String senha, String nome, String email) {
        validarDados(senha, nome, email);
        this.id = -1;
        this.senhaHash = hashSenha(senha);
        this.nome = nome.trim();
        this.email = email;
    }

    private void validarDados(String senha, String nome, String email) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    // Método para hashificar a senha usando BCrypt
    private String hashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(BCRYPT_COST));
    }

    // Método para verificar a senha
    public boolean validarSenha(String senha) {
        return BCrypt.checkpw(senha, this.senhaHash);
    }

    // Getters e Setters
    public void setId(int id) {
        if (this.id == -1) {
            this.id = id; // Somente atribui um id se ainda não foi atribuído
        }
    }
    public int getId() {
        return id;
    }

    public String getHashSenha() {
        return this.senhaHash;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
