package entity.pessoa;

import dao.PessoaDAO;
import dto.PessoaDTO;

public class Bibliotecario extends Pessoa{
    private String codigo;
    public Bibliotecario(PessoaDTO dto, String codigo, boolean senhaJaHasheada) {
        super(0,dto.senha(), dto.nome(), dto.email(), senhaJaHasheada);
        this.codigo = codigo;
    }

    public void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código não pode ser vazio");
        }
    }

    public Bibliotecario cadastrarBibliotecario(PessoaDAO dao) {
        int idNovaPessoa = salvar(dao);
        return dao.addBibliotecario(this, idNovaPessoa);
    }

    public String getCodigo() {
        return this.codigo;
    }
}
