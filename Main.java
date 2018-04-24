import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {
    static double width = 1000;
    static double height = 750;
    static final int blockNumber = 7;
    private static final int towerNumber = 3;
    static Block[] blocks = new Block[blockNumber];
    static Tower[] towers = new Tower[towerNumber];
    private static String commandQueue = "";
    private static int commandsExecuted = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hanoi");
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        for(int i = 0; i < towerNumber; ++i) {
            towers[i] = new Tower(i);
        }
        for(int i = 0; i < blockNumber; ++i) {
            blocks[i] = new Block(i);
        }
        try {
            for (int i = blockNumber-1; i >= 0; --i) {
                towers[0].putOnTop(blocks[i]);
            }
        } catch(FullTowerException e) {
            e.printStackTrace();
        }

        Button b = new Button("Move Stack");
        root.getChildren().add(b);
        b.setOnAction(actionEvent -> {
            moveStack(0,1, blockNumber);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            addCommand("moveBlock(0,1)");
        });

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                executeCommand();
                gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
                for(Tower t : towers) {
                    t.draw(gc);
                }
                System.out.println(commandsExecuted);
            }
        }.start();
    }

    public static void moveStack(int initial, int end, int height) {
        if(height == 1) {
            addCommand("moveBlock(" + initial + "," + end + ")");
        } else {
            int target2;
            for(target2 = 0; target2 < 3; ++target2) {
                if(target2 != initial && target2 != end) {
                    break;
                }
            }
            moveStack(initial, target2, height - 1);
            addCommand("moveBlock(" + initial + "," + end + ")");
            moveStack(target2, end, height - 1);
        }
    }

    public static void addCommand(String command) {
        commandQueue = commandQueue + command + ";";
    }

    public static void executeCommand() {
        String command = null;
        if(commandQueue.contains(";")) {
            command = commandQueue.substring(0,commandQueue.indexOf(";"));
        } else {
            return;
        }

        if(command.contains("moveBlock")) {
            moveBlock(Integer.valueOf(command.substring(command.indexOf("(")+1,command.indexOf("(")+2)), Integer.valueOf(command.substring(command.indexOf(")")-1,command.indexOf(")"))));
            commandQueue = commandQueue.substring(commandQueue.indexOf(";")+1);
            commandsExecuted++;
        }
        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void moveBlock(int initial, int end) {
        try {
            towers[end].putOnTop(towers[initial].getTopBlock());
            towers[initial].removeTopBlock();
        } catch(EmptyTowerException | FullTowerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}