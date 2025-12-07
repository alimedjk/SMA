package engine.mapElements;

public class ArenaElement {
    protected Block block;

    public ArenaElement(Block block){
        this.block = block;
    }

    public ArenaElement() {

    }


    public synchronized Block getBlock() {
        return block;
    }

    public void setPosition(Block block) {
        this.block = block;
    }

	public void setBlock(Block newBlock) {
		this.block = newBlock;
	}


    public int getLine() {
        return block.getLine();
    }

    public int getColumn() {
        return block.getColumn();
    }

}
