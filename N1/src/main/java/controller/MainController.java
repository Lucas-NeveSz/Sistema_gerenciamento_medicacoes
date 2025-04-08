package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {
    @FXML private Button btnGerenciarMedicamentos;
    @FXML private Button btnRelatorios;

    @FXML
    private void gerenciarMedicamentos() throws IOException {
        abrirJanela("/view/medicamento.fxml", "Gerenciamento de Medicamentos");
    }

    @FXML
    private void abrirRelatorios() throws IOException {
        abrirJanela("/view/relatorio.fxml", "Relatórios");
    }

    private void abrirJanela(String fxml, String titulo) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.show();
    }
}