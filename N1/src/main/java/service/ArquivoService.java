package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.Fornecedor;
import model.Medicamento;

public class ArquivoService {
    private static final String ARQUIVO_MEDICAMENTOS = "medicamentos.csv";
    private static final String ARQUIVO_FORNECEDORES = "fornecedores.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void salvarMedicamentos(List<Medicamento> medicamentos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_MEDICAMENTOS))) {
            for (Medicamento med : medicamentos) {
                writer.write(med.getCodigo() + ";");
                writer.write(med.getNome() + ";");
                writer.write(med.getDescricao() + ";");
                writer.write(med.getPrincipioAtivo() + ";");
                writer.write(med.getDataValidade().format(DATE_FORMATTER) + ";");
                writer.write(med.getQuantidadeEstoque() + ";");
                writer.write(med.getPreco() + ";");
                writer.write(med.isControlado() + ";");
                writer.write(med.getFornecedor().getCnpj());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Medicamento> carregarMedicamentos(List<Fornecedor> fornecedores) {
        List<Medicamento> medicamentos = new ArrayList<>();
        File arquivo = new File(ARQUIVO_MEDICAMENTOS);

        if (!arquivo.exists()) {
            return medicamentos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_MEDICAMENTOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 9) {
                    String codigo = dados[0];
                    String nome = dados[1];
                    String descricao = dados[2];
                    String principioAtivo = dados[3];
                    LocalDate dataValidade = LocalDate.parse(dados[4], DATE_FORMATTER);
                    int quantidadeEstoque = Integer.parseInt(dados[5]);
                    BigDecimal preco = new BigDecimal(dados[6]);
                    boolean controlado = Boolean.parseBoolean(dados[7]);
                    String cnpjFornecedor = dados[8];

                    Fornecedor fornecedor = fornecedores.stream()
                            .filter(f -> f.getCnpj().equals(cnpjFornecedor))
                            .findFirst()
                            .orElse(null);

                    if (fornecedor != null) {
                        Medicamento medicamento = new Medicamento(
                                codigo, nome, descricao, principioAtivo,
                                dataValidade, quantidadeEstoque, preco,
                                controlado, fornecedor);
                        medicamentos.add(medicamento);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return medicamentos;
    }

    public void salvarFornecedores(List<Fornecedor> fornecedores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_FORNECEDORES))) {
            for (Fornecedor forn : fornecedores) {
                writer.write(forn.getCnpj() + ";");
                writer.write(forn.getRazaoSocial() + ";");
                writer.write(forn.getTelefone() + ";");
                writer.write(forn.getEmail() + ";");
                writer.write(forn.getCidade() + ";");
                writer.write(forn.getEstado());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Fornecedor> carregarFornecedores() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        File arquivo = new File(ARQUIVO_FORNECEDORES);

        if (!arquivo.exists()) {
            return fornecedores;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_FORNECEDORES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 6) {
                    Fornecedor fornecedor = new Fornecedor(
                            dados[0], dados[1], dados[2],
                            dados[3], dados[4], dados[5]);
                    fornecedores.add(fornecedor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fornecedores;
    }
}