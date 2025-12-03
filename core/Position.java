package core;
/** Position simple (x,y) */
public class Position {
    public int x, y;
    public Position(int x, int y) { this.x = x; this.y = y; }
    @Override public boolean equals(Object o) {
        if (!(o instanceof Position)) return false;
        Position p = (Position)o; return p.x==x && p.y==y;
    }
    @Override public int hashCode() { return 31*x + y; }
    @Override public String toString() { return "(" + x + "," + y + ")"; }
}
