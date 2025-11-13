package controller;

import dao.ProdutoDAO;
import model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ProdutosController {

    @FXML private TableView<Produto> tableView;
    @FXML private TableColumn<Produto, Integer> colId;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, Double> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TextField txtNome, txtDescricao, txtPreco, txtEstoque;

    private ObservableList<Produto> produtosList;
    private ProdutoDAO produtoDAO;

    @FXML
    public void initialize() {
        produtoDAO = new ProdutoDAO();
        produtosList = FXCollections.observableArrayList();
        tableView.setItems(produtosList);

        // Configuração das colunas da tabela
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colDescricao.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());
        colEstoque.setCellValueFactory(cellData -> cellData.getValue().estoqueProperty().asObject());

        carregarProdutos();
        carregarCategorias();
    }

    private void carregarProdutos() {
        try {
            produtosList.clear();
            produtosList.addAll(produtoDAO.read());
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void carregarCategorias() {
        // Aqui você pode adicionar as categorias disponíveis, usando o DAO ou configurando categorias fixas
        cbCategoria.getItems().addAll("Categoria 1", "Categoria 2", "Categoria 3");
    }

    @FXML
    private void handleSalvar() {
        if (validarCampos()) {
            try {
                Produto produto = new Produto();
                produto.setNome(txtNome.getText());
                produto.setDescricao(txtDescricao.getText());
                produto.setPreco(Double.parseDouble(txtPreco.getText()));
                produto.setEstoque(Integer.parseInt(txtEstoque.getText()));
                // Setar categoria se necessário (você pode associar categorias ao banco)
                produtoDAO.create(produto);
                carregarProdutos();
                limparCampos();
                mostrarAlerta("Produto salvo com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro ao salvar produto: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAtualizar() {
        Produto produtoSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null && validarCampos()) {
            try {
                produtoSelecionado.setNome(txtNome.getText());
                produtoSelecionado.setDescricao(txtDescricao.getText());
                produtoSelecionado.setPreco(Double.parseDouble(txtPreco.getText()));
                produtoSelecionado.setEstoque(Integer.parseInt(txtEstoque.getText()));
                produtoDAO.update(produtoSelecionado);
                carregarProdutos();
                limparCampos();
                mostrarAlerta("Produto atualizado com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro ao atualizar produto: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Selecione um produto para atualizar!");
        }
    }

    @FXML
    private void handleExcluir() {
        Produto produtoSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            try {
                produtoDAO.delete(produtoSelecionado.getId());
                carregarProdutos();
                limparCampos();
                mostrarAlerta("Produto excluído com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro ao excluir produto: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Selecione um produto para excluir!");
        }
    }

    @FXML
    private void handleLimpar() {
        limparCampos();
        tableView.getSelectionModel().clearSelection();
    }

    private void limparCampos() {
        txtNome.clear();
        txtDescricao.clear();
        txtPreco.clear();
        txtEstoque.clear();
        cbCategoria.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        if (txtNome.getText().isEmpty() || txtDescricao.getText().isEmpty() ||
            txtPreco.getText().isEmpty() || txtEstoque.getText().isEmpty()) {
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
