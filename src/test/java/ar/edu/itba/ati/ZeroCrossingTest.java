package ar.edu.itba.ati;

import ar.edu.itba.ati.image.Constants;
import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ZeroCrossingTest {

    private static int threshold = 0;

    @Test
    public void noChanges() {
        List<Integer> values = Arrays.asList(-1, -1, -1, -1, -1, -1);
        List<Pixel> pixels = values.stream().map(GrayScalePixel::new).collect(Collectors.toList());

        Image image = new Image(3, 2, pixels, Image.Format.PGM);
        Image zeroCrossing = image.zeroCrossing(threshold);

        assertEquals(Constants.BLACK, zeroCrossing.getPixel(0, 0).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(1, 0).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(2, 0).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(0, 1).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(1, 1).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(2, 1).getRed());
    }

    @Test
    public void horizontalSignChange() {
        List<Integer> values = Arrays.asList(1, -1, -1);
        List<Pixel> pixels = values.stream().map(GrayScalePixel::new).collect(Collectors.toList());

        Image image = new Image(3, 1, pixels, Image.Format.PGM);
        Image zeroCrossing = image.zeroCrossing(threshold);

        assertEquals(Constants.WHITE, zeroCrossing.getPixel(0, 0).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(1, 0).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(2, 0).getRed());
    }

    @Test
    public void verticalSignChange() {
        List<Integer> values = Arrays.asList(1, -1, -1);
        List<Pixel> pixels = values.stream().map(GrayScalePixel::new).collect(Collectors.toList());

        Image image = new Image(1, 3, pixels, Image.Format.PGM);
        Image zeroCrossing = image.zeroCrossing(threshold);

        assertEquals(Constants.WHITE, zeroCrossing.getPixel(0, 0).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(0, 1).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(0, 2).getRed());
    }

    @Test
    public void horizontalZeroCrossing() {
        List<Integer> values = Arrays.asList(1, 0, -1);
        List<Pixel> pixels = values.stream().map(GrayScalePixel::new).collect(Collectors.toList());

        Image image = new Image(3, 1, pixels, Image.Format.PGM);
        Image zeroCrossing = image.zeroCrossing(threshold);

        assertEquals(Constants.BLACK, zeroCrossing.getPixel(0, 0).getRed());
        assertEquals(Constants.WHITE, zeroCrossing.getPixel(1, 0).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(2, 0).getRed());
    }

    @Test
    public void verticalZeroCrossing() {
        List<Integer> values = Arrays.asList(1, 0, -1);
        List<Pixel> pixels = values.stream().map(GrayScalePixel::new).collect(Collectors.toList());

        Image image = new Image(1, 3, pixels, Image.Format.PGM);
        Image zeroCrossing = image.zeroCrossing(threshold);

        assertEquals(Constants.BLACK, zeroCrossing.getPixel(0, 0).getRed());
        assertEquals(Constants.WHITE, zeroCrossing.getPixel(0, 1).getRed());
        assertEquals(Constants.BLACK, zeroCrossing.getPixel(0, 2).getRed());
    }
}
