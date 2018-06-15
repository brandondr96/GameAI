package char_control_ai;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * This is the example entity class that can move in the
 * four cardinal directions. The methods are named to 
 * match KeyCode strings to reduce translation code in
 * the example.
 * 
 * @author Brandon Dalla Rosa
 *
 */
public class ExampleEntity {
	private static final int CHAR_SIZE = 20;
	private static final int EXAMPLE_STAGE_SIZE = 600;
	private Rectangle rect;
	
	/**
	 * The constructor for the example entity, taking in the 
	 * location and color to place the rectangle. The size of
	 * the rectangle is a 20x20.
	 * 
	 * @param xLoc: The x-location of the rectangle.
	 * @param yLoc: The y-location of the rectangle.
	 * @param color: The color of the rectangle.
	 */
	public ExampleEntity(double xLoc, double yLoc, Color color) {
		rect = new Rectangle(xLoc,yLoc,CHAR_SIZE,CHAR_SIZE);
		rect.setFill(color);
	}
	
	/**
	 * Method to move the entity left.
	 */
	public void moveLeft() {
		if(inBounds(rect.getX()-CHAR_SIZE,rect.getY())) {
			rect.setX(rect.getX()-CHAR_SIZE);
		}
	}
	
	/**
	 * Method to move the entity right.
	 */
	public void moveRight() {
		if(inBounds(rect.getX()+CHAR_SIZE,rect.getY())) {
			rect.setX(rect.getX()+CHAR_SIZE);
		}
	}
	
	/**
	 * Method to move the entity up.
	 */
	public void moveUp() {
		if(inBounds(rect.getX(),rect.getY()-CHAR_SIZE)) {
			rect.setY(rect.getY()-CHAR_SIZE);
		}
	}
	
	/**
	 * Method to move the entity down.
	 */
	public void moveDown() {
		if(inBounds(rect.getX(),rect.getY()+CHAR_SIZE)) {
			rect.setY(rect.getY()+CHAR_SIZE);
		}
	}
	
	/**
	 * Method to return the rectangle object of the
	 * entity, so that it can be added into the scene.
	 * 
	 * @return: The Rectangle object of the entity.
	 */
	public Rectangle getRect() {
		return rect;
	}
	
	/**
	 * Internal method to check if the rectangle is within bounds.
	 * 
	 * @param x: The x-location of the rectangle.
	 * @param y: The y-location of the rectangle.
	 * @return: The state of the rectangle.
	 */
	private boolean inBounds(double x, double y) {
		if(x<EXAMPLE_STAGE_SIZE&&x>-CHAR_SIZE&&y>-CHAR_SIZE&&y<EXAMPLE_STAGE_SIZE) {
			return true;
		}
		else {
			return false;
		}
	}
}
