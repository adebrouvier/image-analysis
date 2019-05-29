package ar.edu.itba.ati;

import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ImageOperationsTest {

    @Test
    public void addInRange() {
        final List<Integer> values = Arrays.asList(10, 10, 10, 10);
        final List<Pixel> pixels = intArrayGrayScaleToPixels(values);

        final Image firstImage = new Image(2, 2, pixels, Image.Format.PGM);
        final Image secondImage = new Image(2, 2, new ArrayList<>(pixels), Image.Format.PGM);
        Image result = firstImage.add(secondImage);

        assertEquals(20, result.getPixel(0, 0).getRed());
        assertEquals(20, result.getPixel(1, 0).getRed());
        assertEquals(20, result.getPixel(0, 1).getRed());
        assertEquals(20, result.getPixel(1, 1).getRed());
    }

    @Test
    public void addOutOfRange() {
        final List<Integer> firstValues = Arrays.asList(255, 255, 255, 255);
        final List<Pixel> pixels = intArrayGrayScaleToPixels(firstValues);

        final List<Integer> secondValues = Arrays.asList(255, 255, 255, 255);
        final List<Pixel> secondPixels = intArrayGrayScaleToPixels(secondValues);

        final Image firstImage = new Image(2, 2, pixels, Image.Format.PGM);
        final Image secondImage = new Image(2, 2, secondPixels, Image.Format.PGM);
        Image result = firstImage.add(secondImage);

        assertEquals(255, result.getPixel(0, 0).getRed());
        assertEquals(255, result.getPixel(1, 0).getRed());
        assertEquals(255, result.getPixel(0, 1).getRed());
        assertEquals(255, result.getPixel(1, 1).getRed());
    }

    @Test
    public void denormalizedAdd() {
        final List<Integer> firstValues = Arrays.asList(255, 255, 255, 255);
        final List<Pixel> pixels = intArrayGrayScaleToPixels(firstValues);

        final List<Integer> secondValues = Arrays.asList(10, 20, 30, 40);
        final List<Pixel> secondPixels = intArrayGrayScaleToPixels(secondValues);

        final Image firstImage = new Image(2, 2, pixels, Image.Format.PGM);
        final Image secondImage = new Image(2, 2, secondPixels, Image.Format.PGM);
        Image result = firstImage.denormalizedAdd(secondImage);

        assertEquals(265, result.getPixel(0, 0).getRed());
        assertEquals(275, result.getPixel(1, 0).getRed());
        assertEquals(285, result.getPixel(0, 1).getRed());
        assertEquals(295, result.getPixel(1, 1).getRed());
    }

    @Test
    public void denormalizedSubstract() {
        final List<Integer> firstValues = Arrays.asList(0, 0, 0, 0);
        final List<Pixel> pixels = intArrayGrayScaleToPixels(firstValues);

        final List<Integer> secondValues = Arrays.asList(10, 20, 30, 40);
        final List<Pixel> secondPixels = intArrayGrayScaleToPixels(secondValues);

        final Image firstImage = new Image(2, 2, pixels, Image.Format.PGM);
        final Image secondImage = new Image(2, 2, secondPixels, Image.Format.PGM);
        Image result = firstImage.denormalizedSubstract(secondImage);

        assertEquals(-10, result.getPixel(0, 0).getRed());
        assertEquals(-20, result.getPixel(1, 0).getRed());
        assertEquals(-30, result.getPixel(0, 1).getRed());
        assertEquals(-40, result.getPixel(1, 1).getRed());
    }

    @Test
    public void scalarMultiplication() {
        final List<Integer> firstValues = Arrays.asList(10, 20, 30, 40);
        final List<Pixel> pixels = intArrayGrayScaleToPixels(firstValues);

        final Image firstImage = new Image(2, 2, pixels, Image.Format.PGM);
        final Image result = firstImage.multiply(2.0, false);

        assertEquals(20, result.getPixel(0, 0).getRed());
        assertEquals(40, result.getPixel(1, 0).getRed());
        assertEquals(60, result.getPixel(0, 1).getRed());
        assertEquals(80, result.getPixel(1, 1).getRed());
    }

    @Test
    public void multiplyImages() {
        final List<Integer> firstValues = Arrays.asList(10, 10, 10, 10);
        final List<Pixel> pixels = intArrayGrayScaleToPixels(firstValues);

        final List<Integer> secondValues = Arrays.asList(1, 2, 3, 4);
        final List<Pixel> secondPixels = intArrayGrayScaleToPixels(secondValues);

        final Image firstImage = new Image(2, 2, pixels, Image.Format.PGM);
        final Image secondImage = new Image(2, 2, secondPixels, Image.Format.PGM);
        Image result = firstImage.multiply(secondImage);

        assertEquals(10, result.getPixel(0, 0).getRed());
        assertEquals(20, result.getPixel(1, 0).getRed());
        assertEquals(30, result.getPixel(0, 1).getRed());
        assertEquals(40, result.getPixel(1, 1).getRed());
    }

    private List<Pixel> intArrayGrayScaleToPixels(List<Integer> secondValues) {
        return secondValues.stream().map(GrayScalePixel::new).collect(Collectors.toList());
    }
}
