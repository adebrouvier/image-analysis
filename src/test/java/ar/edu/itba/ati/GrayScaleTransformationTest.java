package ar.edu.itba.ati;

import ar.edu.itba.ati.image.*;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GrayScaleTransformationTest {

    @Test
    public void transform() {

        final List<List<Integer>> values = Arrays.asList(Arrays.asList(100, 40, 10));
        final List<Pixel> pixels = values.stream()
                                            .map(a -> new RGBPixel(a.get(0), a.get(1), a.get(2)))
                                            .collect(Collectors.toList());

        final Image rgbImage = new Image(1, 1, pixels, Image.ImageType.GRAY_SCALE, Image.Format.PPM);
        Image result = GrayScaleTransformation.apply(rgbImage);

        assertThat(result.getPixel(0, 0), IsInstanceOf.instanceOf(GrayScalePixel.class));
        assertEquals(50, result.getPixel(0, 0).getGrayscale());
    }

}
