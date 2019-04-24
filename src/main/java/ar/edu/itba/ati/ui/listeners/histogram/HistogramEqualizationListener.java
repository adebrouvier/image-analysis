package ar.edu.itba.ati.ui.listeners.histogram;

import ar.edu.itba.ati.image.Histogram;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
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
        Histogram histogram = new Histogram(windowContext.getImageContainer().getImage(), Image.Channel.GRAY);
        Image newImage = histogram.equalize();
        Histogram newHistogram = new Histogram(newImage, Image.Channel.GRAY);
        JFreeChart histogramChart = newHistogram.createChart();
        HistogramContainer.show(histogramChart);
        FrameHelper.create(newImage);
    }
}
