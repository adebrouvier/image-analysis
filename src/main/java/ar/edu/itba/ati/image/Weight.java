package ar.edu.itba.ati.image;

@FunctionalInterface
public interface Weight {
    Double get(Double std, Integer x, Integer y);
}
