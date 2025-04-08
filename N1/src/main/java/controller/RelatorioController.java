package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Medicamento;
import service.FornecedorService;
import service.MedicamentoService;

public class RelatorioController implements Initializable {
    @FXML private ListView<String> lvRelatorios;
    @FXML private TableView<Medicamento> tblResultados;
    @FXML private TableColumn<Medicamento, String> colCodigo;
    @FXML private TableColumn<Medicamento, String> colNome;
    @FXML private TableColumn<Medicamento, String> colValidade;
    @FXML private TableColumn<Medicamento, Integer> colEstoque;
    @FXML private TableColumn<Medicamento, String> colFornecedor;
    @FXML private TableColumn<Medicamento, String> colControlado;
    @FXML private Label lblTotal;

    private MedicamentoService medicamentoService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        medicamentoService = new MedicamentoService(new FornecedorService());

        configurarTabela();
        carregarRelatorios();
    }

    private void configurarTabela() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colValidade.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataValidade().toString()));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
        colFornecedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFornecedor().getRazaoSocial()));
        colControlado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isControlado() ? "S" : "N")
        );
    }

    private void carregarRelatorios() {
        lvRelatorios.setItems(FXCollections.observableArrayList(
                "Medicamentos próximos ao vencimento (30 dias)",
                "Medicamentos com estoque baixo (<5 unidades)",
                "Valor total do estoque por fornecedor",
                "Quantidade de medicamentos controlados vs não controlados"
        ));

        lvRelatorios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> gerarRelatorio(newValue));
    }

    private void gerarRelatorio(String relatorio) {
        if (relatorio == null) return;

        switch (relatorio) {
            case "Medicamentos próximos ao vencimento (30 dias)":
                List<Medicamento> proximosVencimento = medicamentoService.medicamentosProximoVencimento();
                tblResultados.setItems(FXCollections.observableArrayList(proximosVencimento));
                lblTotal.setText("Total: " + proximosVencimento.size() + " medicamentos");
                break;

            case "Medicamentos com estoque baixo (<5 unidades)":
                List<Medicamento> estoqueBaixo = medicamentoService.medicamentosEstoqueBaixo();
                tblResultados.setItems(FXCollections.observableArrayList(estoqueBaixo));
                lblTotal.setText("Total: " + estoqueBaixo.size() + " medicamentos");
                break;

            case "Valor total do estoque por fornecedor":
                List<Medicamento> lista = medicamentoService.listarMedicamentos();
                tblResultados.setItems(FXCollections.observableArrayList(lista));
                lblTotal.setText("Selecione um medicamento para ver o valor total do estoque do fornecedor.");

                tblResultados.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
                    if (selecionado != null) {
                        String fornecedorSelecionado = selecionado.getFornecedor().getRazaoSocial();
                        BigDecimal valorTotalFornecedor = lista.stream()
                                .filter(m -> m.getFornecedor().getRazaoSocial().equals(fornecedorSelecionado))
                                .map(m -> m.getPreco().multiply(BigDecimal.valueOf(m.getQuantidadeEstoque())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        lblTotal.setText("Fornecedor: " + fornecedorSelecionado +
                                " | Valor total do estoque: R$ " + valorTotalFornecedor);
                    }
                });
                break;

            case "Quantidade de medicamentos controlados vs não controlados":
                List<Medicamento> todosMedicamentos = medicamentoService.listarMedicamentos();
                tblResultados.setItems(FXCollections.observableArrayList(todosMedicamentos));

                long controlados = todosMedicamentos.stream().filter(Medicamento::isControlado).count();
                long naoControlados = todosMedicamentos.size() - controlados;

                lblTotal.setText(String.format("Controlados: %d | Não controlados: %d", controlados, naoControlados));
                break;

        }
    }
}