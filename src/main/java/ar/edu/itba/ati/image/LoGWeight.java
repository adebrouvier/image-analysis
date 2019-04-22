package ar.edu.itba.ati.image;

public class LoGWeight implements Weight {

    @Override
    public Double get(Double std, Integer x, Integer y) {
        double firstTerm = -1 / (Math.sqrt(2 * Math.PI) * Math.pow(std, 3));
        double aux = (x * x + y * y) / (std * std);
        double secondTerm = (2 - aux);
        double exponent = -aux / 2.0;
        return firstTerm * secondTerm * Math.exp(exponent);
    }
}
