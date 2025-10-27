import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class Chart extends JFrame {
    public Chart(String WindowTitle, List<String> countries, List<Double> totalNetworth) {
        super(WindowTitle);

        // Построение графика
        JFreeChart Chart = ChartFactory.createBarChart(
                "Общий капитал миллиардеров Forbes по странам (топ-10)",
                "Страна",
                "Общий капитал (млрд $)",
                createDataset(countries, totalNetworth),
                PlotOrientation.VERTICAL,
                false, true, false);

        ChartPanel chartPanel = new ChartPanel(Chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        setContentPane(chartPanel);
    }

    // Данные графика
    private CategoryDataset createDataset(List<String> countries, List<Double> networth) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < countries.size() && i < 10; i++) {
            dataset.addValue(networth.get(i), "Общий капитал", countries.get(i));
        }

        return dataset;
    }
}