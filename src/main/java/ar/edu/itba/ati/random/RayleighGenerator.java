package ar.edu.itba.ati.random;

public class RayleighGenerator extends RandomGenerator {

    private double phi;

    public RayleighGenerator(double phi){
        this.phi = phi;
    }

    @Override
    public double getDouble() {
        return phi * Math.sqrt(-2 * Math.log(1 - getUniform()));
    }
}
