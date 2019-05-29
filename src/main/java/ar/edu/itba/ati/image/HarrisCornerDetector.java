package ar.edu.itba.ati.image;

public class HarrisCornerDetector {

    private final static double GAUSS_MASK_STD = 2.0;
    private final static int GAUSS_MASK_SIZE = 7;
    private final static double k = 0.04;

    public Image apply(Image image) {

        Image Ix = image.prewittFirstOperator();
        Image Ix2 = Ix.multiply(Ix);
        Ix2 = Ix2.gaussMaskFilter(GAUSS_MASK_STD, GAUSS_MASK_SIZE, false);

        Image Iy = image.prewittSecondOperator();
        Image Iy2 = Iy.multiply(Iy);
        Iy2 = Iy2.gaussMaskFilter(GAUSS_MASK_STD, GAUSS_MASK_SIZE, false);

        Image Ixy = Ix.multiply(Iy);
        Image Ixy2 = Ixy.multiply(Ixy);
        Ixy2 = Ixy2.gaussMaskFilter(GAUSS_MASK_STD, GAUSS_MASK_SIZE, false);

        /*
         * (Ix^2 * Iy^2 - Ixy^2) - k * (Ix^2 + Iy^2)^2
         */
        Image firstTerm = Ix2.multiply(Iy2).denormalizedSubstract(Ixy2);
        Image base = Ix2.denormalizedAdd(Iy2);
        Image secondTerm = base.multiply(base).multiply(k, false);
        Image result = firstTerm.denormalizedSubstract(secondTerm);

        if (result.getType().equals(Image.ImageType.RGB)) {
            result.normalizeColor();
        } else {
            result.normalize();
        }

        return result.otsuThreshold();
    }
}
