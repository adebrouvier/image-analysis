package ar.edu.itba.ati.image;

import ar.edu.itba.ati.ui.HistogramContainer;
import ar.edu.itba.ati.utils.Pair;
import org.jfree.chart.JFreeChart;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

        JFreeChart histogramChartY = Histogram.createChart(globalMean(mapY), image.getWidth(), "Horizontal Edge Histogram", "Column Number", "Difference");
        JFreeChart histogramChartX = Histogram.createChart(globalMean(mapX), image.getHeight(), "Vertical Edge Histogram","Row Number", "Difference");
        HistogramContainer.show(histogramChartY);
        HistogramContainer.show(histogramChartX);

//        Pair<Integer, Integer> regionY = calculateRegion(globalMean(mapY)).first().getX();
//        Pair<Integer, Integer> regionX = calculateRegion(globalMean(mapX)).first().getX();
        Image firstSubImage = null;
        for (Pair<Pair<Integer, Integer>, Double> regionYvalue: calculateRegion(globalMean(mapY))) {
            Pair<Integer, Integer> regionY = regionYvalue.getX();
            for (Pair<Pair<Integer, Integer>, Double> regionXvalue: calculateRegion(globalMean(mapX))) {
                Pair<Integer, Integer> regionX = regionXvalue.getX();
                int x1 = Math.max(0, regionY.getX() - 3);
                int x2 = Math.min(image.getWidth(), regionY.getY() + 3);
                int y1 = Math.max(0, regionX.getX() - 3);
                int y2 = Math.min(image.getHeight(), regionX.getY() + 3);
                Image subImage = image.getSubimage(x1, y1, x2, y2);
                Image processedSubImage = subImage.copyToGrayscale().getNegative();
                if (firstSubImage == null) {
                    firstSubImage = subImage;
                }
                String response = OCR.run(processedSubImage);
                response = matchLicensePlate(response);
                if (response != null) {
                    System.out.println("Patente argentina encontrada: " + response);
                    return subImage;
                }
            }
        }
        System.out.println("Texto encontrado: " + OCR.run(firstSubImage));
        return firstSubImage;
//        Image subImage = image.getSubimage(regionY.getX() - 3,
//                regionX.getX() - 3,
//                regionY.getY() + 3,
//                regionX.getY() + 3);
//        return subImage;
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

    private static TreeSet<Pair<Pair<Integer, Integer>, Double>> calculateRegion(List<Map.Entry<Integer, Double>> differences) {
        Integer prevKey = differences.get(0).getKey();
        TreeSet<Pair<Pair<Integer, Integer>, Double>> regions = new TreeSet<>(
                (a, b) -> b.getY().compareTo(a.getY())
        );
//        Pair<Pair<Integer, Integer>, Double> maxRegion = new Pair(null, 0.0);
        Double currentMax = differences.get(0).getValue();
        Integer fromRegion = prevKey;
        for (int i = 1 ; i < differences.size() ; i++) {
            if (differences.get(i).getKey() == prevKey + 1) {
                if (differences.get(i).getValue() > currentMax)
                    currentMax = differences.get(i).getValue();
            }
            else {
//                if (currentMax > maxRegion.getY())
//                    maxRegion = new Pair(new Pair(fromRegion, prevKey), currentMax);
                regions.add(new Pair(new Pair(fromRegion, prevKey), currentMax));
                currentMax = differences.get(i).getValue();
                fromRegion = differences.get(i).getKey();
            }
            prevKey = differences.get(i).getKey();
        }
        regions.add(new Pair(new Pair(fromRegion, prevKey), currentMax));
//        if (currentMax > maxRegion.getY())
//            maxRegion = new Pair(new Pair(fromRegion, prevKey), currentMax);
//        return maxRegion.getX();
        return regions;
    }

    private static String matchLicensePlate(String str) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("\\s", "");
        Pattern pattern = Pattern.compile("[A-Z]{3}[0-9]{3}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        pattern = Pattern.compile("[A-Z]{2}[0-9]{3}[A-Z]{2}");
        matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
