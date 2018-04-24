import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block {
    public static final int blockHeight = 40;
    private int size;

    public Block(int size) {
        this.size = size;
    }

    public void moveBlock(int position) {
    }

    public int getSize() {
        return size;
    }
}
