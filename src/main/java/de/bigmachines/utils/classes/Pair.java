package de.bigmachines.utils.classes;

import java.util.Objects;

public class Pair<X, Y> {
    public final X x;
    public final Y y;
    
    public Pair(X x, Y y) {
        super();
        this.x = x;
        this.y = y;
    }
    
    public Pair<Y, X> swap() {
        return new Pair<Y, X>(y, x);
    }
    
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (other instanceof Pair) {
            Pair p = (Pair) other;
            return (p.x.equals(x) && p.y.equals(y)) ||
                    (p.x.equals(y) && p.y.equals(x));
        }
        return false;
    }

    public int hashCode() {
        int xHash = x.hashCode();
        int yHash = y.hashCode();
        if (xHash < yHash) return Objects.hash(xHash, yHash);
        else return Objects.hash(yHash, xHash);
    }
}