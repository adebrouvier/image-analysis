package ar.edu.itba.ati.image;

public class MaskRotator {

    public static Double[] rotate3x3Mask(Double[] mask) {
        if (mask.length != 9) {
            throw new IllegalArgumentException("Wrong mask size");
        }
        Double[] newMask = new Double[9];

        newMask[0] = mask[3];
        newMask[1] = mask[0];
        newMask[2] = mask[1];
        newMask[3] = mask[6];
        newMask[4] = mask[4];
        newMask[5] = mask[2];
        newMask[6] = mask[7];
        newMask[7] = mask[8];
        newMask[8] = mask[5];

        return newMask;
    }
}
