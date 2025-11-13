package controller;

import dao.CategoriaDAO;
import model.Categoria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CategoriasController {

    @FXML private TableView<Categoria> tableView;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colNome;
    @FXML private TableColumn<Categoria, String> colDescricao;
    @FXML private TextField txtNome, txtDescricao;

    private ObservableList<Categoria> categoriasList;
    private CategoriaDAO categoriaDAO;

    @FXML
    public void initialize() {
        categoriaDAO = new CategoriaDAO();
        categoriasList = FXCollections.observableArrayList();
        tableView.setItems(categoriasList);

        // Configuração das colunas da tabela
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colDescricao.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());

        carregarCategorias();
    }

    private void carregarCategorias() {
        try {
            categoriasList.clear();
            categoriasList.addAll(categoriaDAO.read());
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    @FXML
    private void handleSalvar() {
        if (validarCampos()) {
            try {
                Categoria categoria = new Categoria();
                categoria.setNome(txtNome.getText());
                categoria.setDescricao(txtDescricao.getText());
                categoriaDAO.create(categoria);
                carregarCategorias();
                limparCampos();
                mostrarAlerta("Categoria salva com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro ao salvar categoria: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAtualizar() {
        Categoria categoriaSelecionada = tableView.getSelectionModel().getSelectedItem();
        if (categoriaSelecionada != null && validarCampos()) {
            try {
                categoriaSelecionada.setNome(txtNome.getText());
                categoriaSelecionada.setDescricao(txtDescricao.getText());
                categoriaDAO.update(categoriaSelecionada);
                carregarCategorias();
                limparCampos();
                mostrarAlerta("Categoria atualizada com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro ao atualizar categoria: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Selecione uma categoria para atualizar!");
        }
    }

    @FXML
    private void handleExcluir() {
        Categoria categoriaSelecionada = tableView.getSelectionModel().getSelectedItem();
        if (categoriaSelecionada != null) {
            try {
                categoriaDAO.delete(categoriaSelecionada.getId());
                carregarCategorias();
                limparCampos();
                mostrarAlerta("Categoria excluída com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro ao excluir categoria: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Selecione uma categoria para excluir!");
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
    }

    private boolean validarCampos() {
        if (txtNome.getText().isEmpty() || txtDescricao.getText().isEmpty()) {
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
