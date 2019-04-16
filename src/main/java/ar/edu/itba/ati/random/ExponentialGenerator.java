package ar.edu.itba.ati.random;

public class ExponentialGenerator extends RandomGenerator {

    private double lambda;


    public ExponentialGenerator(double lambda) {
        super();
        this.lambda = lambda;
    }

    public double getDouble() {
        double x = getUniform();
        return (-1 / lambda) * Math.log(x);
    }
}
