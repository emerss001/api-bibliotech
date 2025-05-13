package service;

import com.google.gson.JsonObject;
import dao.AvaliacaoDAO;
import dao.PessoaDAO;
import dto.AvaliacaoDTO;
import dto.AvaliacaoResponseDTO;
import model.material.Avaliacao;
import model.pessoa.Pessoa;
import util.TokenUtil;

import java.util.ArrayList;
import java.util.Objects;

public class AvaliacaoService {
    private final AvaliacaoDAO avaliacaoDAO;
    private final PessoaDAO pessoaDAO;

    public AvaliacaoService(AvaliacaoDAO avaliacaoDAO, PessoaDAO pessoaDAO) {
        this.avaliacaoDAO = Objects.requireNonNull(avaliacaoDAO, "DAO das avaliações não pode ser nulo");
        this.pessoaDAO = Objects.requireNonNull(pessoaDAO, "DAO dos usuários não pode ser nulo");
    }

    public Avaliacao addAvaliacao(AvaliacaoDTO dto){
        if (dto == null) throw new IllegalArgumentException("Dados da avaliação inválidos");

        if (!dto.valido()) throw new IllegalArgumentException("Dados obrigatórios não informados");

        Avaliacao avaliacao = new Avaliacao(dto);
        avaliacao.setId(avaliacao.salvar(avaliacaoDAO));
        return avaliacao;
    }

    public void removeAvaliacao(JsonObject json){
        if (json == null) throw new IllegalArgumentException("Dados da avaliação inválidos");

        Integer id = json.has("id") && json.get("id").getAsInt() > 0 ? json.get("id").getAsInt() : null;

        avaliacaoDAO.removeAvaliacao(id);
    }

    public ArrayList<AvaliacaoResponseDTO> readAvaliacao(Integer materialId){
        if (materialId == null || materialId < 0) throw new IllegalArgumentException("Dados da avaliação inválidos");

        return avaliacaoDAO.readAvaliacao(materialId);
    }

    public Integer tokenTOId(String token){
        Pessoa aluno = pessoaDAO.buscarPorEmail(TokenUtil.extrairEmail(token));

        return aluno.getId();
    }
}
