package ar.edu.itba.ati.filters;

import ar.edu.itba.ati.image.Weight;

public class GaussianMask {

    private Integer maskSize;
    private Double std;
    private Weight weight;

    public GaussianMask(Integer maskSize, Double std, Weight weight){
        this.maskSize = maskSize;
        this.std = std;
        this.weight = weight;
    }

    public Double[] getMask() {
        Double[] mask = new Double[maskSize * maskSize];
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                int x = i - maskSize / 2;
                int y = j - maskSize / 2;
                mask[i + j * maskSize] = weight.get(std, x, y);
            }
        }
        return mask;
    }
}
