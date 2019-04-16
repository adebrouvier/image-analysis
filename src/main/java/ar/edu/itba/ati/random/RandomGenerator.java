package ar.edu.itba.ati.random;

import java.util.Random;

public abstract class RandomGenerator {

    private Random random;

    public RandomGenerator() {
        this.random = new Random();
    }

    public double getUniform() {
        return random.nextDouble();
    }

    public abstract double getDouble();
}
