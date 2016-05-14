package parser;

public enum Direction {
    Sell(-1),
    Buy(1);

    private int i;

    Direction(int i) {

        this.i = i;
    }

    public int getI() {
        return i;
    }
}
