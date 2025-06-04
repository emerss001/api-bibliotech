package model.pessoa;

import dao.PessoaDAO;
import org.mindrot.jbcrypt.BCrypt;

public class Pessoa {
    private static final int BCRYPT_COST = 12;

    private int id;
    private String senhaHash;
    private String nome;
    private String email;

    public Pessoa() {

    }

    public Pessoa(int id, String senha, String nome, String email, boolean senhaJaHasheada) {
        if (!senhaJaHasheada) {
            validarDados(senha, nome, email);
            this.senhaHash = hashSenha(senha);
        } else {
            this.senhaHash = senha;
        }
        this.id = id;
        this.nome = nome.trim();
        this.email = email;
    }

    public Pessoa(int id, String email, String senha) {
        this.id = id;
        this.email = email;
        this.senhaHash = senha;
    }

    // Construtor usado na rota de busca por detalhes de uma material
    public Pessoa(String nome) {
        this.nome = nome;
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

    public int salvar(PessoaDAO dao) {
         return dao.addPessoa(this);
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

    public void setNome(String nome) {
        this.nome = nome;
    }
}
