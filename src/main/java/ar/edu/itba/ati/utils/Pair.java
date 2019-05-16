package ar.edu.itba.ati.utils;

import java.util.Objects;

public class Pair<X,Y> {

    private X x;
    private Y y;

    public Pair(X x, Y y){
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return x.equals(pair.x) &&
                y.equals(pair.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
