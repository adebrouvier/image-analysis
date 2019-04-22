package ar.edu.itba.ati.image;

public class GaussianWeight implements Weight {

    @Override
    public Double get(Double std, Integer x, Integer y) {
        double mult = 1 / (2 * Math.PI * std * std);
        double exponent = -(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(std, 2));
        return mult * Math.exp(exponent);
    }
}
