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
import java.util.HashMap;
import java.util.Map;

public class Histogram {

    private Image image;
    private Map<Integer, Integer> absoluteFrequencies;
    private Map<Integer, Double> relativeFrequencies;

    public Histogram(Image image) {
        this.image = image;
        create();
    }

    private void create(){

        this.absoluteFrequencies = initializeFrequencies();

        for (Pixel p : image.getPixels()){
            addValue(((GrayScalePixel) p).getGrayScale());
        }

        calculateRelativeFrequencies();
    }

    private Map<Integer, Integer> initializeFrequencies() {

        absoluteFrequencies = new HashMap<>();

        for (int i = 0; i < 256 ; i++){
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
        double pixels = image.getWidth()*image.getHeight();
        for (Map.Entry<Integer, Integer> entry : absoluteFrequencies.entrySet()) {
            double relativeFrequency = entry.getValue()/pixels;
            relativeFrequencies.put(entry.getKey(), relativeFrequency);
        }
    }

    public JFreeChart createChart() {
        IntervalXYDataset dataSet = createDataSet();
        JFreeChart histogram = ChartFactory.createXYBarChart("Histogram",
                "Gray scale",false, "Relative frequency", dataSet);

        histogram.removeLegend();
        XYPlot plot = (XYPlot) histogram.getPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setLowerBound(0);
        axis.setUpperBound(255);
        XYBarRenderer r = (XYBarRenderer) plot.getRenderer();
        r.setBarPainter(new StandardXYBarPainter());
        r.setSeriesPaint(0, Color.black);
        return histogram;
    }

    private IntervalXYDataset createDataSet() {

        XYSeries series = new XYSeries("Grayscale level");

        for (Map.Entry<Integer, Double> entry : relativeFrequencies.entrySet()) {
            series.add((double) entry.getKey(), entry.getValue());
        }

        return new XYBarDataset(new XYSeriesCollection(series), 1.0);
    }

    public void equalize() {
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

        updateImage(equalized);
        create();
    }

    private void updateImage(Map<Integer, Integer> equalized) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++){
                int currentPixel = image.getPixel(x, y).getRed();
                Pixel newPixel = new GrayScalePixel(equalized.get(currentPixel));
                image.changePixel(x, y, newPixel);
            }
        }
    }
}
