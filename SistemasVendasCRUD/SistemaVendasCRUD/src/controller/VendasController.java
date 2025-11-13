package controller;

import dao.VendaDAO;
import model.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class VendasController {

    @FXML private TableView<Venda> tableView;
    @FXML private TableColumn<Venda, Integer> colId;
    @FXML private TableColumn<Venda, LocalDate> colDataVenda;
    @FXML private TableColumn<Venda, Double> colValorTotal;
    @FXML private TextField txtClienteId, txtProdutoId, txtQuantidade, txtValorTotal;

    private ObservableList<Venda> vendasList;
    private VendaDAO vendaDAO;

    @FXML
    public void initialize() {
        vendaDAO = new VendaDAO();
        vendasList = FXCollections.observableArrayList();
        tableView.setItems(vendasList);

        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colDataVenda.setCellValueFactory(cellData -> cellData.getValue().dataVendaProperty());
        colValorTotal.setCellValueFactory(cellData -> cellData.getValue().valorTotalProperty().asObject());

        carregarVendas();
    }

    private void carregarVendas() {
        try {
            vendasList.clear();
            vendasList.addAll(vendaDAO.read());
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar vendas: " + e.getMessage());
        }
    }

    @FXML
    private void handleSalvar() {
        if (validarCampos()) {
            try {
                Venda venda = new Venda();
                venda.setClienteId(Integer.parseInt(txtClienteId.getText()));
                venda.setProdutoId(Integer.parseInt(txtProdutoId.getText()));
                venda.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
                venda.setValorTotal(Double.parseDouble(txtValorTotal.getText()));
                venda.setDataVenda(LocalDate.now());
                vendaDAO.create(venda);
                carregarVendas();
                limparCampos();
                mostrarAlerta("Venda salva com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro ao salvar venda: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLimpar() {
        limparCampos();
        tableView.getSelectionModel().clearSelection();
    }

    private void limparCampos() {
        txtClienteId.clear();
        txtProdutoId.clear();
        txtQuantidade.clear();
        txtValorTotal.clear();
    }

    private boolean validarCampos() {
        if (txtClienteId.getText().isEmpty() || txtProdutoId.getText().isEmpty() ||
            txtQuantidade.getText().isEmpty() || txtValorTotal.getText().isEmpty()) {
            mostrarAlerta("Todos os campos são obrigatórios!");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
