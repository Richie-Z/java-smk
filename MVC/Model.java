package mvc;

public class Model {
    private int x;

    public Model() {
        x = 1;
    }

    public Model(int x) {
        this.x = x;
    }

    public void incX() {
        x = x + 2;
    }

    public int getX() {
        return x;
    }
}