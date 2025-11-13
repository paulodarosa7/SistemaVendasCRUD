package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class GraficoController {

    @FXML private LineChart<String, Number> lineChart;

    @FXML
    public void initialize() {
        // Exemplo de gráfico simples com dados fictícios
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Vendas");
        series.getData().add(new XYChart.Data<>("Jan", 1500));
        series.getData().add(new XYChart.Data<>("Feb", 2000));
        series.getData().add(new XYChart.Data<>("Mar", 1800));
        lineChart.getData().add(series);
    }
}
