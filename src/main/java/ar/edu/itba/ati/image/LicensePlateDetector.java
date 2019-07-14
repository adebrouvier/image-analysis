package ar.edu.itba.ati.image;

import ar.edu.itba.ati.ui.HistogramContainer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LicensePlateDetector {

    public static Image detect(Image image) {

        Image grayscale = GrayScaleTransformation.apply(image);
        //Histogram histogram = new Histogram(grayscale, Image.Channel.GRAY);
        //Image equalized = histogram.equalize();
        //Image median = equalized.medianFilter(9);
        Image filtered = grayscale.bilateralFilter(3, 10.0, 10.0);
        Image sobel = filtered.sobelXOperator();
        Image subimage = sobel.getSubimage(1, 1, sobel.getWidth() - 1, sobel.getHeight() - 1);
        //Image meanFiltered = sobel.meanFilter(3, false);
        subimage.normalize();

        Map<Integer, Double> map = new HashMap<>();

        for (int x = 0; x < subimage.getWidth(); x++) {
            double difference = 0;
            for (int y = 0; y < subimage.getHeight(); y++) {
                //difference += Math.abs(sobel.getPixel(x, y).getGrayscale());
                difference += subimage.getPixel(x, y).getGrayscale();
            }
            map.put(x, difference);
        }

        JFreeChart histogramChart = createChart(map, image.getWidth());
        HistogramContainer.show(histogramChart);

        //Image threshold = sobel.otsuThreshold();
        //Image threshold = sobel.threshold(30.0);

        //OCR.run(image);

        return filtered;
    }

    private static JFreeChart createChart(Map<Integer, Double> map, int maxX) {

        IntervalXYDataset dataSet = createDataSet(map);
        JFreeChart histogram = ChartFactory.createXYBarChart("Histogram",
                "Number", false, "Difference", dataSet);

        histogram.removeLegend();
        XYPlot plot = (XYPlot) histogram.getPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setLowerBound(0);
        axis.setUpperBound(maxX);
        XYBarRenderer r = (XYBarRenderer) plot.getRenderer();
        r.setBarPainter(new StandardXYBarPainter());
        r.setSeriesPaint(0, Color.black);
        return histogram;
    }

    private static IntervalXYDataset createDataSet(Map<Integer, Double> map) {

        XYSeries series = new XYSeries("Grayscale level");

        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            series.add((double) entry.getKey(), entry.getValue());
        }

        return new XYBarDataset(new XYSeriesCollection(series), 1.0);
    }
}
