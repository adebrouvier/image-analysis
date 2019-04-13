package ar.edu.itba.ati;

import ar.edu.itba.ati.image.Histogram;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.io.RAWImageIO;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class HistogramTest {

    private static Image image;

    @BeforeClass
    public static void loadImage() throws IOException {
        Scanner sc = new Scanner(new File("LENA.txt"));
        int width = sc.nextInt();
        int height = sc.nextInt();
        RAWImageIO reader = new RAWImageIO(width, height);
        image = reader.read(new File("LENA.RAW"));
    }

    @Test
    public void pdf(){
        Histogram histogram = new Histogram(image);
        Map<Integer, Double> pdf = histogram.pdf();
        assertEquals(256, pdf.size());
        assertEquals(1.0, pdf.get(255), 0.001);
    }

}
