package char_control_ai;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * This class demonstrates basic usage of the CharControl class,
 * using an example entity which has the ability to move in
 * the cardinal directions. It shows how the enemy is trained using
 * the user input.
 * 
 * @author Brandon Dalla Rosa
 *
 */
public class CharControlTester extends Application{
	private static final int PLAYER_START_LOC = 80;
	private static final int ENEMY_START_LOC = 500;
	private static final int EXAMPLE_STAGE_SIZE = 600;
	private static final int ANIMATION_SPEED = 60;
	private static final int MILLIS_PER_SECOND = 1000;
	private static final Paint BACKGROUND = Color.AZURE;
	private static final Color PLAYER_COLOR = Color.GOLD;
	private static final Color ENEMY_COLOR = Color.BLACK;
	private static final int BUFFER_MAX = 10;
	private static final int BUFFER_MIN = 0;
	private static final String TESTER_TITLE = "CharControl Tester";
	private static final KeyCode[] KEYCODES = {KeyCode.LEFT,KeyCode.UP,KeyCode.DOWN,KeyCode.RIGHT};
	private static final String[] METHODNAMES = {"moveLeft","moveUp","moveDown","moveRight"};
	private Map<KeyCode,String> keyToMethod = new HashMap<KeyCode,String>();
	private Group root;
	private ExampleEntity player;
	private ExampleEntity enemy;
	private Queue<String> inputQueue = new LinkedList<String>();
	private CharControl enemyAI;
	private int buffer = BUFFER_MAX;
	
	/**
	 * This method starts the window for the example.
	 */
	public void start (Stage stage) {
		root = new Group();
		Scene tester = new Scene(root,EXAMPLE_STAGE_SIZE,EXAMPLE_STAGE_SIZE,BACKGROUND);
		tester.setOnKeyPressed(click->{inputQueue.add(keyToMethod.get(click.getCode()));});
		stage.setScene(tester);
		stage.setTitle(TESTER_TITLE);
		stage.show();
		KeyFrame frame = new KeyFrame(Duration.millis(MILLIS_PER_SECOND/ANIMATION_SPEED),
                e -> step());
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
		initialize();
	}
	
	/**
	 * This demonstrates how to use the CharControl class
	 * and pass in the correct arguments. It is important
	 * that the strings for the inputs are the method names
	 * that are desired to be the abilities of the entity
	 * with CharControl.
	 */
	private void initialize() {
		player = new ExampleEntity(PLAYER_START_LOC,PLAYER_START_LOC,PLAYER_COLOR);
		enemy = new ExampleEntity(ENEMY_START_LOC,ENEMY_START_LOC,ENEMY_COLOR);
		root.getChildren().addAll(player.getRect(),enemy.getRect());
		for(int i=0;i<KEYCODES.length;i++) {
			keyToMethod.put(KEYCODES[i], METHODNAMES[i]);
		}
		
		//Now to test the CharControl
		enemyAI = new CharControl(enemy,METHODNAMES);
	}
	
	/**
	 * This demonstrates how to make the entity
	 * with CharControl perform an action or
	 * method based on the trained data.
	 * 
	 * @param timeStep: The length of the frame.
	 */
	private void step() {
		if(!inputQueue.isEmpty()) {
			String next = inputQueue.poll();
			enemyAI.addInput(next);
			handleInput(player, next);
		}
		//Buffer variable used to slow movement of example enemy.
		//Step is not slowed since it would reduce the player entity's
		//reaction speed.
		buffer--;
		if(buffer<BUFFER_MIN) {
			buffer = BUFFER_MAX;
			enemyAI.act();
		}
	}
	
	/**
	 * This simulates how input is received by the player,
	 * as it different for every implementation.
	 * 
	 * @param entity: The entity controlled by the player.
	 * @param input: The input received from the input queue.
	 */
	private void handleInput(ExampleEntity entity, String input) {
		if(input.equals(METHODNAMES[0])) {
			entity.moveLeft();
		}
		else if(input.equals(METHODNAMES[1])) {
			entity.moveUp();
		}
		else if(input.equals(METHODNAMES[2])) {
			entity.moveDown();
		}
		else if(input.equals(METHODNAMES[3])) {
			entity.moveRight();
		}
	}
	
	/**
	 * The main method to run the program.
	 * 
	 * @param args: The arguments for javaFX to start.
	 */
	public static void main (String[] args) {
        launch(args);
    }

}
