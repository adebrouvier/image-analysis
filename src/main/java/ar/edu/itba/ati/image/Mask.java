package ar.edu.itba.ati.image;

import java.util.List;

@FunctionalInterface
interface MaskApplier {

    Pixel apply(List<Pixel> pixels);

}
