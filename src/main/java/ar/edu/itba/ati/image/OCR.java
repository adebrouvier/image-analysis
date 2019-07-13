package ar.edu.itba.ati.image;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCR {

    public static void run(Image image) {
        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath("./tessdata");
        tesseract.setLanguage("eng");

        try {
            String result = tesseract.doOCR(ImageUtils.ImageToBufferedImage(image));
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
