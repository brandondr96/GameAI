package char_control_ai;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Class designed to give an entity the ability to learn and
 * mimic actions, similar to an AI. The entity should have a 
 * base of actions which are similar in nature to the player,
 * so that it can train from the user input data.
 * 
 * @author Brandon Dalla Rosa
 *
 */
public class CharControl {
	private static final String NULL_PLACEHOLDER = "NULL";
	private static final String REFLECTION_WARNING = "Warning: Incorrect inputs or object provided to CharControl";
	private Object character;
	private List<MoveData> moveList;
	private Map<String, Method> methodMap;
	private MoveData currentInput;
	private MoveData currentExecute;
	private boolean inputAdded;
	
	/**
	 * Constructor for the CharControl class. The default action of the
	 * entity is set to null until trained with data.
	 * 
	 * @param otherObject: The character desired to act in an intelligent manner.
	 * @param inputs: The desired method names to be called by the entity.
	 */
	public CharControl(Object otherObject, String... inputs) {
		this.character = otherObject;
		this.inputAdded = false;
		moveList = new ArrayList<MoveData>();
		methodMap = new HashMap<String,Method>();
		@SuppressWarnings("rawtypes")
		Class c = otherObject.getClass();
		Method[] methods = c.getDeclaredMethods();
		for(int i=0;i<inputs.length;i++) {
			for(int j=0;j<methods.length;j++) {
				if(methods[j].toString().contains(inputs[i])) {
					methodMap.put(inputs[i],methods[j]);
				}
			}
		}
		MoveData defInit = new MoveData(NULL_PLACEHOLDER);
		moveList.add(defInit);
		currentInput = defInit;
		currentExecute = new MoveData(NULL_PLACEHOLDER);
	}
	
	/**
	 * This signals the character to perform the next action, based
	 * on the data trained from the user controlling the player.
	 */
	public void act() {
		if(inputAdded) {
			String action = currentExecute.getWord();
			if(!currentExecute.getWord().equals(NULL_PLACEHOLDER)) {
				Method toAct = methodMap.get(action);
				try {
					if(toAct!=null) {
						toAct.invoke(character);
					}
					else {
						throw new CharControlException(REFLECTION_WARNING);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | CharControlException e) {
					System.out.println(e);
				}
			}
			String nextExecute = currentExecute.nextInput();
			for(MoveData data : moveList) {
				String c = data.getWord();
				if(nextExecute.equals(c)) {
					currentExecute = data;
				}
			}
		}
	}
	/**
	 * This returns the string of the next action, based
	 * on the data trained from the user controlling the player. 
	 */
	public String actString() {
		String action = currentExecute.getWord();
		String nextExecute = currentExecute.nextInput();
		for(MoveData data : moveList) {
			String c = data.getWord();
			if(nextExecute.equals(c)) {
				currentExecute = data;
			}
		}
		if(!currentExecute.getWord().equals(NULL_PLACEHOLDER)) {
			return action;
		}
		return NULL_PLACEHOLDER;
	}
	
	/**
	 * Allows training data to be added. Should be called whenever an input is
	 * given by the user to control the player.
	 * 
	 * @param input: The input given by the user to control the player.
	 */
	public void addInput(String input) {
		inputAdded = true;
		currentInput.addWord(input);
		boolean exists = false;
		for(MoveData data : moveList) {
			String c = data.getWord();
			if(input.equals(c)) {
				exists = true;
				currentInput = data;
			}
		}
		if(!exists) {
			MoveData newCurrent = new MoveData(input);
			moveList.add(newCurrent);
			
			currentInput = newCurrent;
		}
	}
	
	/**
	 * A structure to contain the inputs, in addition to the next actions
	 * to execute after the initial. The method to choose these actions is
	 * based on pseudo Markov chains.
	 * 
	 * @author Brandon Dalla Rosa
	 *
	 */
	private class MoveData{
		String input;
		ArrayList<String> nextInputs;
		MoveData(String thisInput){
			nextInputs = new ArrayList<String>();
			input = thisInput;
		}
		public void addWord(String next) {
			nextInputs.add(next);
		}
		public String getWord() {
			return input;
		}
		public String nextInput() {
			Random rand = new Random();
			String output = "";
			if(nextInputs.size()>0) {
				int w = rand.nextInt(nextInputs.size());
				output = nextInputs.get(w);
			}
			else {
				int w = rand.nextInt(moveList.size());
				output = moveList.get(w).getWord();
			}
			return output;
		}
	}
	/**
	 * Exception to be caught if an error is found in the reflection
	 * process for the acting entity.
	 * 
	 * @author Brandon Dalla Rosa
	 *
	 */
	@SuppressWarnings("serial")
	private class CharControlException extends Exception{
		String toShow;
		CharControlException(String message){
			toShow = message;
		}
		public String toString() {
			return "CharControlException: "+toShow;
		}
		
	}

}
