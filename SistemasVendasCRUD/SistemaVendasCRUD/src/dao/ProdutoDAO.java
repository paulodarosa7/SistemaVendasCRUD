package dao;

import model.Produto;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private Produto map(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setPreco(rs.getDouble("preco"));
        p.setEstoque(rs.getInt("estoque"));
        p.setCategoriaId(rs.getInt("categoria_id"));

        if (hasColumn(rs, "categoria_nome"))
            p.setCategoriaNome(rs.getString("categoria_nome"));

        return p;
    }

    /** Verifica se coluna existe no ResultSet */
    private boolean hasColumn(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /* ==================== CRUD ==================== */

    public void create(Produto produto) throws SQLException {
        String sql = """
            INSERT INTO produtos (nome, descricao, preco, estoque, categoria_id)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            s
