package data;

public class Signal {

    private   int x;
    private int y;
    private Objet objet;

    public Signal(int x, int y, Objet objet){
        this.x = x;
        this.y = y;
        this.objet = objet;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Objet getObjet() {
        return objet;
    }
}
