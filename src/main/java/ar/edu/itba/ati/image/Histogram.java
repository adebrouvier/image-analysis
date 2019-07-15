package ar.edu.itba.ati.image;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Histogram {

    private Image image;
    private Map<Integer, Integer> absoluteFrequencies;
    private Map<Integer, Double> relativeFrequencies;

    public Histogram(Image image, Image.Channel channel) {
        this.image = image;
        create(channel);
    }

    private void create(Image.Channel channel) {

        this.absoluteFrequencies = initializeFrequencies();

        for (Pixel p : image.getPixels()) {
            addValue(getChannel(p, channel));
        }

        calculateRelativeFrequencies();
    }

    private int getChannel(Pixel p, Image.Channel channel) {
        switch (channel){
            case GREEN:
                return p.getBlue();
            case BLUE:
                return p.getGreen();
            default:
                return p.getRed();
        }
    }

    private Map<Integer, Integer> initializeFrequencies() {

        absoluteFrequencies = new HashMap<>();

        for (int i = 0; i < 256; i++) {
            absoluteFrequencies.put(i, 0);
        }

        return absoluteFrequencies;
    }

    private void addValue(int value) {
        Integer frequency = absoluteFrequencies.get(value);
        absoluteFrequencies.put(value, frequency + 1);
    }

    private void calculateRelativeFrequencies() {
        this.relativeFrequencies = new HashMap<>();
        double pixels = image.getWidth() * image.getHeight();
        for (Map.Entry<Integer, Integer> entry : absoluteFrequencies.entrySet()) {
            double relativeFrequency = entry.getValue() / pixels;
            relativeFrequencies.put(entry.getKey(), relativeFrequency);
        }
    }

    public Map<Integer, Double> pdf() {
        Map<Integer, Double> pdf = new HashMap<>();

        pdf.put(0, relativeFrequencies.get(0));

        for (int i = 1; i < relativeFrequencies.size(); i++) {
            double probability = pdf.get(i - 1) + relativeFrequencies.get(i);
            pdf.put(i, probability);
        }

        return pdf;
    }

    public double globalMean() {

        double globalMean = 0;

        for (int i = 0; i < relativeFrequencies.size(); i++) {
            globalMean += i * relativeFrequencies.get(i);
        }

        return globalMean;
    }

    public Map<Integer, Double> accumulativeMean() {
        Map<Integer, Double> acumMean = new HashMap<>();

        for (int i = 0; i < relativeFrequencies.size(); i++) {
            double acum = 0;
            for (int j = 0; j <= i; j++) {
                acum += j * relativeFrequencies.get(j);
            }
            acumMean.put(i, acum);
        }

        return acumMean;
    }

    public JFreeChart createChart() {
        return createChart(relativeFrequencies.entrySet(), 255, "Histogram", "Grayscale", "Relative frequency");
    }

    public static JFreeChart createChart(Collection<Map.Entry<Integer, Double>> entries, int xMax, String title,
                                         String xAxisLabel, String yAxisLabel) {
        IntervalXYDataset dataSet = createDataSet(entries);
        JFreeChart histogram = ChartFactory.createXYBarChart(title,
                xAxisLabel, false, yAxisLabel, dataSet);

        histogram.removeLegend();
        XYPlot plot = (XYPlot) histogram.getPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setLowerBound(0);
        axis.setUpperBound(xMax);
        XYBarRenderer r = (XYBarRenderer) plot.getRenderer();
        r.setBarPainter(new StandardXYBarPainter());
        r.setSeriesPaint(0, Color.black);
        return histogram;
    }

    private static IntervalXYDataset createDataSet(Collection<Map.Entry<Integer, Double>> entries) {

        XYSeries series = new XYSeries("Serie");

        for (Map.Entry<Integer, Double> entry : entries) {
            series.add((double) entry.getKey(), entry.getValue());
        }

        return new XYBarDataset(new XYSeriesCollection(series), 1.0);
    }

    public Image equalize() {
        Map<Integer, Integer> cdf = new HashMap<>();
        int acummulated = 0;
        int min = Integer.MAX_VALUE;

        /* Calculate min grey and cumulative distribution function */
        for (Map.Entry<Integer, Integer> entry : absoluteFrequencies.entrySet()) {
            int grey = entry.getKey();
            int count = entry.getValue();
            if (count > 0) {
                if (grey < min) {
                    min = grey;
                }
            }
            acummulated += count;
            cdf.put(grey, acummulated);
        }

        /* Calculate equalization */
        Map<Integer, Integer> equalized = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : cdf.entrySet()) {
            int equalizedValue = (int) Math.floor((((entry.getValue() - min) / (image.getPixels().size() - min * 1.0)) * 255.0));
            equalized.put(entry.getKey(), equalizedValue);
        }

        return createImage(equalized);
    }

    private Image createImage(Map<Integer, Integer> equalized) {
        Image newImage = image.copy();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int currentPixel = image.getPixel(x, y).getRed();
                Pixel newPixel = new GrayScalePixel(equalized.get(currentPixel));
                newImage.changePixel(x, y, newPixel);
            }
        }
        return newImage;
    }
}
