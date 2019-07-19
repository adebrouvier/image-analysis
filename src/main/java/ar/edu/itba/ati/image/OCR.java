package ar.edu.itba.ati.image;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCR {

    public static String run(Image image) {
        ITesseract tesseract = new Tesseract();

        File tessDataFolder = new File("tessdata");
        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
        tesseract.setLanguage("eng");
        //tesseract.setOcrEngineMode(1);

        try {
            return tesseract.doOCR(ImageUtils.ImageToBufferedImage(image));
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
