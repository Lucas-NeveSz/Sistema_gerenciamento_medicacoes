package service;

import java.util.ArrayList;
import java.util.List;

import model.Fornecedor;

public class FornecedorService {
    private List<Fornecedor> fornecedores;
    private ArquivoService arquivoService;

    public FornecedorService() {
        arquivoService = new ArquivoService();
        fornecedores = arquivoService.carregarFornecedores();

        if (fornecedores.isEmpty()) {
            fornecedores = criarFornecedoresPadrao(); // <-- seus novos dados
            arquivoService.salvarFornecedores(fornecedores);
        }
    }

    public void cadastrarFornecedor(Fornecedor fornecedor) {
        if (!fornecedores.contains(fornecedor)) {
            fornecedores.add(fornecedor);
            arquivoService.salvarFornecedores(fornecedores);
        }
    }

    public void removerFornecedor(Fornecedor fornecedor) {
        fornecedores.remove(fornecedor);
        arquivoService.salvarFornecedores(fornecedores);
    }

    public List<Fornecedor> listarFornecedores() {
        return new ArrayList<>(fornecedores);
    }

    public Fornecedor buscarFornecedorPorCnpj(String cnpj) {
        return fornecedores.stream()
                .filter(f -> f.getCnpj().equals(cnpj))
                .findFirst()
                .orElse(null);
    }

    private List<Fornecedor> criarFornecedoresPadrao() {
        List<Fornecedor> lista = new ArrayList<>();
        lista.add(new Fornecedor("78.564.219/0001-88", "Farmacauticos Ltda", "62984456379", "cleusa@farmacaut.com.br", "Goiânia", "GO"));
        lista.add(new Fornecedor("78.965.237/0001-65", "Medieval drugs", "1184568742", "vendas@medidurg.com.br", "São Paulo", "RJ"));
        lista.add(new Fornecedor("91.365.845/0001-93", "Agustinho Saude Distribuidora", "62984563351", "contabil@agusaude.com.br", "Goiânia", "GO"));
        return lista;
    }
}
