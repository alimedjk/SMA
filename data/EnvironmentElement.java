package data;

public class EnvironmentElement {
    protected Block block;

    public EnvironmentElement(Block block){
        this.block = block;
    }

    public EnvironmentElement() {

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
