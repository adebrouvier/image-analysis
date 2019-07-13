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

public class GlobalThresholdTest {

    @Test
    public void noChanges() {
        List<Integer> values = Arrays.asList(128, 128, 128, 130, 130, 130);
        List<Pixel> pixels = values.stream().map(GrayScalePixel::new).collect(Collectors.toList());

        Image image = new Image(3, 2, pixels, Image.ImageType.GRAY_SCALE, Image.Format.PGM);
        Image newImage = image.globalThreshold();

        assertEquals(Constants.BLACK, newImage.getPixel(0, 0).getRed());
        assertEquals(Constants.BLACK, newImage.getPixel(1, 0).getRed());
        assertEquals(Constants.BLACK, newImage.getPixel(2, 0).getRed());
        assertEquals(Constants.WHITE, newImage.getPixel(0, 1).getRed());
        assertEquals(Constants.WHITE, newImage.getPixel(1, 1).getRed());
        assertEquals(Constants.WHITE, newImage.getPixel(2, 1).getRed());
    }
}
