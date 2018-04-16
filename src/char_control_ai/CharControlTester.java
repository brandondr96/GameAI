package char_control_ai;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
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
	private final Paint bg = Color.AZURE;
	private Group root;
	private ExampleEntity player;
	private ExampleEntity enemy;
	private Queue<String> inputQueue = new LinkedList<String>();
	private CharControl enemyAI;
	private int buffer = 10;
	
	/**
	 * This method starts the window for the example.
	 */
	public void start (Stage stage) {
		root = new Group();
		Scene tester = new Scene(root,600,600,bg);
		tester.setOnKeyPressed(click->{inputQueue.add(click.getCode().toString());});
		stage.setScene(tester);
		stage.setTitle("CharControl Tester");
		stage.show();
		KeyFrame frame = new KeyFrame(Duration.millis(1000/60),
                e -> step(1/60));
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
		player = new ExampleEntity(80,80,Color.GOLD);
		enemy = new ExampleEntity(500,500,Color.BLACK);
		root.getChildren().addAll(player.getRect(),enemy.getRect());
		
		//Now to test the CharControl
		@SuppressWarnings("rawtypes")
		Class c = player.getClass();
		String[] inputs = {"LEFT","RIGHT","UP","DOWN"};
		Method[] methods = c.getDeclaredMethods();
		enemyAI = new CharControl(enemy,inputs,methods);
	}
	
	/**
	 * This demonstrates how to make the entity
	 * with CharControl perform an action or
	 * method based on the trained data.
	 * 
	 * @param timeStep: The length of the frame.
	 */
	private void step(double timeStep) {
		if(!inputQueue.isEmpty()) {
			String next = inputQueue.poll();
			enemyAI.addInput(next);
			handleInput(player, next);
		}
		buffer--;
		if(buffer<0) {
			buffer = 10;
			enemyAI.act();
		}
	}
	
	/**
	 * This simulates how input is received by the player,
	 * as it different for every VOOGA implementation.
	 * 
	 * @param entity: The entity controlled by the player.
	 * @param input: The input received from the input queue.
	 */
	private void handleInput(ExampleEntity entity, String input) {
		if(input.equals("LEFT")) {
			entity.LEFT();
		}
		else if(input.equals("RIGHT")) {
			entity.RIGHT();
		}
		else if(input.equals("UP")) {
			entity.UP();
		}
		else if(input.equals("DOWN")) {
			entity.DOWN();
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
