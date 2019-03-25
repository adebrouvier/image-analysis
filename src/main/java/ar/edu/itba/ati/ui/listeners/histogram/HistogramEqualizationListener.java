package ar.edu.itba.ati.ui.listeners.histogram;

import ar.edu.itba.ati.image.Histogram;
import ar.edu.itba.ati.ui.HistogramContainer;
import ar.edu.itba.ati.ui.WindowContext;
import org.jfree.chart.JFreeChart;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HistogramEqualizationListener implements ActionListener {

    private WindowContext windowContext;

    public HistogramEqualizationListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Histogram histogram = new Histogram(windowContext.getImageContainer().getImage());
        histogram.equalize();
        JFreeChart histogramChart = histogram.createChart();
        HistogramContainer.show(histogramChart);
        windowContext.getImageContainer().renderImage();
    }
}
