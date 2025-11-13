package dao;

import model.DatabaseConnection;
import model.Venda;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class VendaDAO {

    private Venda map(ResultSet rs) throws SQLException {
        Venda v = new Venda();
        v.setId(rs.getInt("id"));
        v.setDataVenda(rs.getDate("data_venda").toLocalDate());
        v.setClienteId(rs.getInt("cliente_id"));
        v.setProdutoId(rs.getInt("produto_id"));
        v.setQuantidade(rs.getInt("quantidade"));
        v.setValorTotal(rs.getDouble("valor_total"));

        if (hasColumn(rs, "cliente_nome"))
            v.setClienteNome(rs.getString("cliente_nome"));

        if (hasColumn(rs, "produto_nome"))
            v.setProdutoNome(rs.getString("produto_nome"));

        return v;
    }

    private boolean hasColumn(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /* ==================== CRUD ==================== */

    public void create(Venda venda) throws SQLException {
        String sql = """
            INSERT INTO vendas (data_venda, cliente_id, produto_id, quantidade, valor_total)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(venda.getDataVenda()));
            stmt.setInt(2, venda.getClienteId());
            stmt.setInt(3, venda.getProdutoId());
            stmt.setInt(4, venda.getQuantidade());
            stmt.setDouble(5, venda.getValorTotal());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) venda.setId(keys.getInt(1));
        }
    }

    public List<Venda> read() throws SQLException {
        List<Venda> vendas = new ArrayList<>();

        String sql = """
            SELECT v.*, c.nome AS cliente_nome, p.nome AS produto_nome
            FROM vendas v
            LEFT JOIN clientes c ON v.cliente_id = c.id
            LEFT JOIN produtos p ON v.produto_id = p.id
            ORDER BY v.data_venda DESC, v.id DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) vendas.add(map(rs));
        }

        return vendas;
    }

    public Venda findById(int id) throws SQLException {
        String sql = """
            SELECT v.*, c.nome AS cliente_nome, p.nome AS produto_nome
            FROM vendas v
            LEFT JOIN clientes c ON v.cliente_id = c.id
            LEFT JOIN produtos p ON v.produto_id = p.id
            WHERE v.id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        }

        return null;
    }

    public void update(Venda venda) throws SQLException {
        String sql = """
            UPDATE vendas
            SET data_venda=?, cliente_id=?, produto_id=?, quantidade=?, valor_total=?
            WHERE id=?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(venda.getDataVenda()));
            stmt.setInt(2, venda.getClienteId());
            stmt.setInt(3, venda.getProdutoId());
            stmt.setInt(4, venda.getQuantidade());
            stmt.setDouble(5, venda.getValorTotal());
            stmt.setInt(6, venda.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM vendas WHERE id = ?")) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /* ==================== CONSULTAS AVANÇADAS ==================== */

    public List<Venda> findByDateRange(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Venda> vendas = new ArrayList<>();

        String sql = """
            SELECT v.*, c.nome AS cliente_nome, p.nome AS produto_nome
            FROM vendas v
            LEFT JOIN clientes c ON v.cliente_id = c.id
            LEFT JOIN produtos p ON v.produto_id = p.id
            WHERE v.data_venda BETWEEN ? AND ?
            ORDER BY v.data_venda DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) vendas.add(map(rs));
        }

        return vendas;
    }

    public Map<String, Double> getVendasMensais() throws SQLException {
        Map<String, Double> vendas = new LinkedHashMap<>();

        String sql = """
            SELECT DATE_FORMAT(data_venda, '%Y-%m') AS mes, SUM(valor_total) AS total
            FROM vendas
            GROUP BY mes
            ORDER BY mes
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) vendas.put(rs.getString("mes"), rs.getDouble("total"));
        }

        return vendas;
    }
}