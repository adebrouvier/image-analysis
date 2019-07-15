package ar.edu.itba.ati.image;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;

public class OCR {

    public static void run(Image image) {
        ITesseract tesseract = new Tesseract();

        File tessDataFolder = new File("tessdata");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        tesseract.setLanguage("eng");

        try {
            String result = tesseract.doOCR(ImageUtils.ImageToBufferedImage(image));
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
