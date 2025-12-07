package engine.mapElements;

public class Block {

    private int line;
    private int column;
    private boolean isObstacle;

    public Block(int line, int column) {
        this.line = line;
        this.column = column;
        this.isObstacle = false; 
    }

    public Block(int line, int column, boolean isObstacle) {
        this.line = line;
        this.column = column;
        this.isObstacle = isObstacle;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void set(int column, int line){
        this.line = line;
        this.column = column;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        else if( !(obj instanceof Block) ) {
            return false;
        }

        Block objPos = (Block) obj;

        return objPos.getLine() == this.line && objPos.getColumn() == this.column;
    }



    @Override
    public String toString() {
        return "Block [line=" + line + ", column=" + column + ", isObstacle=" + isObstacle + "]";
    }
}