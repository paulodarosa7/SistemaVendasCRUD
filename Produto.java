package model;

import javafx.beans.property.*;

public class Produto {

    private final IntegerProperty id;
    private final StringProperty nome;
    private final StringProperty descricao;
    private final DoubleProperty preco;
    private final IntegerProperty estoque;
    private final IntegerProperty categoriaId;

    public Produto() {
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.descricao = new SimpleStringProperty();
        this.preco = new SimpleDoubleProperty();
        this.estoque = new SimpleIntegerProperty();
        this.categoriaId = new SimpleIntegerProperty();
    }

    public Produto(String nome, String descricao, double preco, int estoque, int categoriaId) {
        this();
        this.nome.set(nome);
        this.descricao.set(descricao);
        this.preco.set(preco);
        this.estoque.set(estoque);
        this.categoriaId.set(categoriaId);
    }

    // Getters e Setters

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getDescricao() {
        return descricao.get();
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public double getPreco() {
        return preco.get();
    }

    public void setPreco(double preco) {
        this.preco.set(preco);
    }

    public DoubleProperty precoProperty() {
        return preco;
    }

    public int getEstoque() {
        return estoque.get();
    }

    public void setEstoque(int estoque) {
        this.estoque.set(estoque);
    }

    public IntegerProperty estoqueProperty() {
        return estoque;
    }

    public int getCategoriaId() {
        return categoriaId.get();
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId.set(categoriaId);
    }

    public IntegerProperty categoriaIdProperty() {
        return categoriaId;
    }

    @Override
    public String toString() {
        return nome.get();
    }
}
