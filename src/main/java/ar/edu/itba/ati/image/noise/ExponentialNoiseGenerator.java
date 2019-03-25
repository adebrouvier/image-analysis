package ar.edu.itba.ati.image.noise;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.random.ExponentialGenerator;

public class ExponentialNoiseGenerator {

    public void addNoise(Image image, double percentage, double lambda) {

        if (percentage < 0 && percentage > 1.0){
            throw new IllegalArgumentException("Invalid image noise percentage");
        }

        ExponentialGenerator generator = new ExponentialGenerator(lambda);

        for (int i = 0; i < image.getHeight(); i++){
            for (int j = 0; j < image.getWidth(); j++){
                if (Math.random() < percentage){
                    //generator.getDouble()
                    //TODO: multiply noise
                }
            }
        }
    }
}
