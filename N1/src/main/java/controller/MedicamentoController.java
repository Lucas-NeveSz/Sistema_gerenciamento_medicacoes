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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Fornecedor;
import model.Medicamento;
import service.FornecedorService;
import service.MedicamentoService;
import util.DateUtil;
import util.ValidacaoUtil;

public class MedicamentoController implements Initializable {
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtPrincipioAtivo;
    @FXML private DatePicker dpDataValidade;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtPreco;
    @FXML private ComboBox<Fornecedor> cbFornecedor;
    @FXML private Button btnCadastrar;
    @FXML private Button btnExcluir;
    @FXML private Button btnConsultar;
    @FXML private Button btnLimpar;
    @FXML private TableView<Medicamento> tblMedicamentos;
    @FXML private TableColumn<Medicamento, String> colCodigo;
    @FXML private TableColumn<Medicamento, String> colNome;
    @FXML private TableColumn<Medicamento, String> colFornecedor;
    @FXML private TableColumn<Medicamento, Integer> colQuantidade;
    @FXML private Label lblControlado;
    @FXML private CheckBox chkControlado;
    @FXML private Button btnAlterar;

    private MedicamentoService medicamentoService;
    private FornecedorService fornecedorService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fornecedorService = new FornecedorService();
        medicamentoService = new MedicamentoService(fornecedorService);

        configurarTabela();
        carregarFornecedores();
        carregarMedicamentos();
    }


    private void configurarTabela() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Modificação para trabalhar com a classe Fornecedor atual
        colFornecedor.setCellValueFactory(cellData -> {
            Fornecedor fornecedor = cellData.getValue().getFornecedor();
            return new SimpleStringProperty(fornecedor != null ? fornecedor.getRazaoSocial() : "");
        });

        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));

        tblMedicamentos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarMedicamento(newValue));
    }

    private void carregarFornecedores() {
        cbFornecedor.setItems(FXCollections.observableArrayList(fornecedorService.listarFornecedores()));
    }

    private void carregarMedicamentos() {
        tblMedicamentos.setItems(FXCollections.observableArrayList(medicamentoService.listarMedicamentos()));
    }

    @FXML
    private void cadastrarMedicamento() {
        if (validarCampos()) {
            try {
                Medicamento medicamento = new Medicamento();
                medicamento.setCodigo(txtCodigo.getText());
                medicamento.setNome(txtNome.getText());
                medicamento.setDescricao(txtDescricao.getText());
                medicamento.setPrincipioAtivo(txtPrincipioAtivo.getText());
                medicamento.setDataValidade(dpDataValidade.getValue());
                medicamento.setQuantidadeEstoque(Integer.parseInt(txtQuantidade.getText()));
                medicamento.setPreco(new BigDecimal(txtPreco.getText()));
                medicamento.setControlado(chkControlado.isSelected());
                medicamento.setFornecedor(cbFornecedor.getValue());

                medicamentoService.cadastrarMedicamento(medicamento);
                carregarMedicamentos();
                limparCampos();
                mostrarAlerta("Sucesso", "Medicamento cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Erro ao cadastrar medicamento: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void excluirMedicamento() {
        Medicamento medicamento = tblMedicamentos.getSelectionModel().getSelectedItem();
        if (medicamento != null) {
            medicamentoService.removerMedicamento(medicamento);
            carregarMedicamentos();
            limparCampos();
            mostrarAlerta("Sucesso", "Medicamento excluído com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Aviso", "Selecione um medicamento para excluir", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void consultarMedicamento() {
        String codigo = txtCodigo.getText();
        if (!codigo.isEmpty()) {
            Medicamento medicamento = medicamentoService.buscarMedicamentoPorCodigo(codigo);
            if (medicamento != null) {
                preencherCampos(medicamento);
                mostrarAlerta("Sucesso", "Medicamento encontrado!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Aviso", "Medicamento não encontrado", Alert.AlertType.WARNING);
            }
        } else {
            mostrarAlerta("Aviso", "Informe o código para consulta", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void limparCampos() {
        txtCodigo.clear();
        txtNome.clear();
        txtDescricao.clear();
        txtPrincipioAtivo.clear();
        dpDataValidade.setValue(null);
        txtQuantidade.clear();
        txtPreco.clear();
        cbFornecedor.getSelectionModel().clearSelection();
        chkControlado.setSelected(false);
        tblMedicamentos.getSelectionModel().clearSelection();
    }

    @FXML
    private void toggleControlado() {
        lblControlado.setText(lblControlado.getText().equals("NÃO") ? "SIM" : "NÃO");
    }

    private void selecionarMedicamento(Medicamento medicamento) {
        if (medicamento != null) {
            preencherCampos(medicamento);
        }
    }

    private void preencherCampos(Medicamento medicamento) {
        txtCodigo.setText(medicamento.getCodigo());
        txtNome.setText(medicamento.getNome());
        txtDescricao.setText(medicamento.getDescricao());
        txtPrincipioAtivo.setText(medicamento.getPrincipioAtivo());
        dpDataValidade.setValue(medicamento.getDataValidade());
        txtQuantidade.setText(String.valueOf(medicamento.getQuantidadeEstoque()));
        txtPreco.setText(medicamento.getPreco().toString());
        cbFornecedor.setValue(medicamento.getFornecedor());
        chkControlado.setSelected(medicamento.isControlado());
    }

    private boolean validarCampos() {
        if (!ValidacaoUtil.validarCodigoMedicamento(txtCodigo.getText())) {
            mostrarAlerta("Erro", "Código inválido! Deve ter 4 caracteres alfanuméricos.", Alert.AlertType.ERROR);
            return false;
        }

        if (txtNome.getText().isEmpty() || txtNome.getText().length() < 3) {
            mostrarAlerta("Erro", "Nome inválido! Deve ter pelo menos 3 caracteres.", Alert.AlertType.ERROR);
            return false;
        }

        if (dpDataValidade.getValue() == null || dpDataValidade.getValue().isBefore(LocalDate.now())) {
            mostrarAlerta("Erro", "Data de validade inválida! Deve ser uma data futura.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            if (quantidade < 0) {
                mostrarAlerta("Erro", "Quantidade inválida! Deve ser um número positivo.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Quantidade inválida! Deve ser um número inteiro.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            BigDecimal preco = new BigDecimal(txtPreco.getText());
            if (preco.compareTo(BigDecimal.ZERO) < 0) {
                mostrarAlerta("Erro", "Preço inválido! Não pode ser negativo.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Preço inválido! Deve ser um valor numérico.", Alert.AlertType.ERROR);
            return false;
        }


        if (cbFornecedor.getValue() == null) {
            mostrarAlerta("Erro", "Selecione um fornecedor.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}