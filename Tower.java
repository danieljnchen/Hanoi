import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tower {
    private final int stackHeight = Main.blockNumber;
    private final int towerWidth = 50;
    private int towerNumber;
    public Block[] stack = new Block[stackHeight];

    public Tower(int towerNumber) {
        this.towerNumber = towerNumber;
    }

    /**
     * @return the index of the first open space in the stack
     */
    public int getTowerHeight() {
        for(int i = 0; i < stackHeight; ++i) {
            if(stack[i] == null) {
                return i;
            }
        }
        return stackHeight;
    }

    public boolean isEmpty() {
        return getTowerHeight() == 0;
    }

    public void putOnTop(Block b) throws FullTowerException {
        try {
            stack[getTowerHeight()] = b;
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new FullTowerException();
        }
    }

    public Block getTopBlock() throws EmptyTowerException {
        try {
            return stack[getTowerHeight()-1];
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new EmptyTowerException();
        }
    }

    public void removeTopBlock() throws EmptyTowerException {
        try {
            stack[getTowerHeight()-1] = null;
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new EmptyTowerException();
        }
    }

    public void draw(GraphicsContext gc) {
        int towerCenter = towerNumber * 250 + 200;
        int towerHeight = stackHeight * Block.blockHeight;

        gc.setFill(Color.BLACK);
        for(int i = 0; i < getTowerHeight(); ++i) {
            Block current = stack[i];
            int blockWidth = 100 + current.getSize()*10;
            gc.fillRect(towerCenter - blockWidth/2, Main.height - Block.blockHeight * (i + 1), blockWidth, Block.blockHeight);
        }

        gc.setFill(Color.RED);
        gc.fillRect(towerCenter-towerWidth/2, Main.height - towerHeight, towerWidth, towerHeight);
    }
}
