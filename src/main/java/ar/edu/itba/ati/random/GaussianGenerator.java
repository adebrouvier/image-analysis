package ar.edu.itba.ati.random;

public class GaussianGenerator extends RandomGenerator{

    private double stdev;
    private double mean;

    public GaussianGenerator(double stdev, double mean){
        super();
        this.stdev = stdev;
        this.mean = mean;
    }

    @Override
    public double getDouble() {

        double x1 = getUniform();
        double x2 = getUniform();

        double y1 = Math.sqrt(-2 * Math.log(x1)) * Math.cos(2*Math.PI*x2);
        return y1 * stdev + mean;
    }
}
