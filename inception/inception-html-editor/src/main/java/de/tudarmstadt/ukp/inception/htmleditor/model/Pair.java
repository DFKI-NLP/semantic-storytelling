package de.tudarmstadt.ukp.inception.htmleditor.model;

import java.io.Serializable;

public class Pair<L,R> implements Serializable {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        assert left != null;
        assert right != null;

        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() {
        return left.hashCode() * right.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return (
            (this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight()))
            || (this.left.equals(pairo.getRight()) && this.right.equals(pairo.getLeft()))
        );
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + "," + this.right.toString() + ")";
    }
}