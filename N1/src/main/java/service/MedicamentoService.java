package service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Medicamento;

public class MedicamentoService {
    private List<Medicamento> medicamentos;
    private FornecedorService fornecedorService;
    private ArquivoService arquivoService;

    public MedicamentoService(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
        this.arquivoService = new ArquivoService();
        this.medicamentos = arquivoService.carregarMedicamentos(fornecedorService.listarFornecedores());
    }

    public void cadastrarMedicamento(Medicamento medicamento) {
        medicamentos.add(medicamento);
        arquivoService.salvarMedicamentos(medicamentos);
    }

    public void removerMedicamento(Medicamento medicamento) {
        medicamentos.remove(medicamento);
        arquivoService.salvarMedicamentos(medicamentos);
    }

    public List<Medicamento> listarMedicamentos() {
        return new ArrayList<>(medicamentos);
    }

    public Medicamento buscarMedicamentoPorCodigo(String codigo) {
        return medicamentos.stream()
                .filter(m -> m.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // Métodos para relatórios usando Stream API
    public List<Medicamento> medicamentosProximoVencimento() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(30);

        return medicamentos.stream()
                .filter(m -> m.getDataValidade().isAfter(hoje) &&
                        m.getDataValidade().isBefore(dataLimite))
                .collect(Collectors.toList());
    }

    public List<Medicamento> medicamentosEstoqueBaixo() {
        return medicamentos.stream()
                .filter(m -> m.getQuantidadeEstoque() < 5)
                .collect(Collectors.toList());
    }

    public BigDecimal valorTotalEstoquePorFornecedor(String cnpjFornecedor) {
        return medicamentos.stream()
                .filter(m -> m.getFornecedor().getCnpj().equals(cnpjFornecedor))
                .map(m -> m.getPreco().multiply(BigDecimal.valueOf(m.getQuantidadeEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long contarMedicamentosControlados(boolean controlado) {
        return medicamentos.stream()
                .filter(m -> m.isControlado() == controlado)
                .count();
    }
}