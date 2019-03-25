package ar.edu.itba.ati.ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;

public class HistogramContainer {

    public static void show(JFreeChart histogramChart) {
        JPanel panel = new ChartPanel(histogramChart);
        JFrame frame = new JFrame("Histogram");
        frame.add(panel);
        frame.setSize(500, 350);
        frame.setVisible(true);
    }
}
