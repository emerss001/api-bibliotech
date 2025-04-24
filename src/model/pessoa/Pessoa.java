package model.pessoa;

import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDate;

public class Pessoa {
    private int id;
    private String senhaHash;
    private String nome;
    private String email;

    public Pessoa(String senha, String nome, String email) {
        this.id = -1;
        this.senhaHash = hashSenha(senha);
        this.nome = nome;
        this.email = email;
    }

    // Método para hashificar a senha usando BCrypt
    private String hashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
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
