package ar.edu.itba.ati.image;

import java.util.List;

@FunctionalInterface
public interface MaskApplier {

    Pixel apply(List<Pixel> pixels);

}
