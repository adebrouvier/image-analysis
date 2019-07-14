package ar.edu.itba.ati.image;

import ar.edu.itba.ati.ui.HistogramContainer;
import ar.edu.itba.ati.utils.Pair;
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
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class LicensePlateDetector {

    public static Image detect(Image image) {

        Image grayscale = GrayScaleTransformation.apply(image);
        Image filtered = grayscale.bilateralFilter(7, 10.0, 10.0);
        Image sobelY = filtered.sobelYOperator();
        sobelY = sobelY.meanFilter(3, true);
        Image subimageY = sobelY.getSubimage(1, 1, sobelY.getWidth() - 1, sobelY.getHeight() - 1);
        subimageY.normalize();

        Image sobelX = filtered.sobelXOperator();
        sobelX = sobelX.meanFilter(3, true);
        Image subimageX = sobelX.getSubimage(1, 1, sobelX.getWidth() - 1, sobelX.getHeight() - 1);
        subimageX.normalize();

        Map<Integer, Double> mapY = new HashMap<>();
        Map<Integer, Double> mapX = new HashMap<>();

        for (int x = 0; x < subimageY.getWidth(); x++) {
            double difference = 0;
            for (int y = 1; y < subimageY.getHeight(); y++) {
                difference += Math.abs(subimageY.getPixel(x, y).getGrayscale() - subimageY.getPixel(x, y-1).getGrayscale());
            }
            mapY.put(x, difference);
        }

        for (int y = 0; y < subimageX.getHeight(); y++) {
            double difference = 0;
            for (int x = 1; x < subimageX.getWidth(); x++) {
                difference += Math.abs(subimageX.getPixel(x, y).getGrayscale() - subimageX.getPixel(x - 1, y).getGrayscale());
            }
            mapX.put(y, difference);
        }

        JFreeChart histogramChartY = createChart(mapY, image.getWidth());
        JFreeChart histogramChartX = createChart(mapX, image.getHeight());
        HistogramContainer.show(histogramChartY);
        HistogramContainer.show(histogramChartX);

        Pair<Integer, Integer> regionY = calculateRegion(globalMean(mapY));
        Pair<Integer, Integer> regionX = calculateRegion(globalMean(mapX));

        Image subImage = image.getSubimage(regionY.getX() - 3,
                regionX.getX() - 3,
                regionY.getY() + 3,
                regionX.getY() + 3);
        return subImage;
    }

    public static List<Map.Entry<Integer, Double>> globalMean(Map<Integer, Double> map) {
        double threshold = map.values().stream().mapToDouble(value -> value).sum() / map.size();
//        double previousThreshold = 0;
//
//        while (Math.abs(previousThreshold - threshold) > 1) {
//            List<Double> g1 = new ArrayList<>();
//            List<Double> g2 = new ArrayList<>();
//
//            for (Double diff : map.values()) {
//                if (diff <= threshold) {
//                    g1.add(diff);
//                } else {
//                    g2.add(diff);
//                }
//            }
//            previousThreshold = threshold;
//            threshold = 0.5 * (
//                    g1.stream().mapToDouble(value -> value).average().getAsDouble() +
//                            g2.stream().mapToDouble(value -> value).average().getAsDouble());
//        }
//
//        double finalPreviousThreshold = previousThreshold;
        double finalPreviousThreshold = threshold;
        List<Map.Entry<Integer, Double>> entries = map.entrySet().stream().filter(
                entry -> entry.getValue() > finalPreviousThreshold
        ).collect(Collectors.toList());
        entries.sort(Comparator.comparing(Map.Entry::getKey));
        return entries;
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

        for (Map.Entry<Integer, Double> entry : globalMean(map)) {
            series.add((double) entry.getKey(), entry.getValue());
        }

        return new XYBarDataset(new XYSeriesCollection(series), 1.0);
    }

    private static Pair<Integer, Integer> calculateRegion(List<Map.Entry<Integer, Double>> differences) {
        Integer prevKey = differences.get(0).getKey();
        Pair<Pair<Integer, Integer>, Double> maxRegion = new Pair(null, 0.0);
        Double currentMax = differences.get(0).getValue();
        Integer fromRegion = prevKey;
        for (int i = 1 ; i < differences.size() ; i++) {
            if (differences.get(i).getKey() == prevKey + 1) {
                if (differences.get(i).getValue() > currentMax)
                    currentMax = differences.get(i).getValue();
            }
            else {
                if (currentMax > maxRegion.getY())
                    maxRegion = new Pair(new Pair(fromRegion, prevKey), currentMax);
                currentMax = differences.get(i).getValue();
                fromRegion = differences.get(i).getKey();
            }
            prevKey = differences.get(i).getKey();
        }
        if (currentMax > maxRegion.getY())
            maxRegion = new Pair(new Pair(fromRegion, prevKey), currentMax);
        return maxRegion.getX();
    }
}
