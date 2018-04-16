package char_control_ai;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
	private Object character;
	private List<MoveData> moveList;
	private Map<String, Method> methodMap;
	private MoveData currentInput;
	private MoveData currentExecute;
	private boolean inputAdded;
	
	/**
	 * Constructor for the CharControl class. It uses the first entry of the "inputs"
	 * as the default action given to the character.
	 * 
	 * @param otherObject: The character desired to act in an intelligent manner.
 * @param inputs: The list of desired method names to be called.
	 * @param methods: The methods for the actions given in inputs.
	 */
	public CharControl(Object otherObject, String[] inputs, Method[] methods) {
		this.character = otherObject;
		this.inputAdded = false;
		moveList = new ArrayList<MoveData>();
		methodMap = new HashMap<String,Method>();
		for(int i=0;i<inputs.length;i++) {
			for(int j=0;j<methods.length;j++) {
				if(methods[j].toString().contains(inputs[i])) {
					methodMap.put(inputs[i],methods[j]);
				}
			}
		}
		MoveData defInit = new MoveData("NULL");
		moveList.add(defInit);
		currentInput = defInit;
		currentExecute = new MoveData("NULL");
	}
	
	/**
	 * This signals the character to perform the next action, based
	 * on the data trained from the user controlling the player.
	 */
	public void act() {
		if(inputAdded) {
			String action = currentExecute.getWord();
			if(!currentExecute.getWord().equals("NULL")) {
				Method toAct = methodMap.get(action);
				try {
					toAct.invoke(character);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					Stage warning = new Stage();
					Label warningLabel = new Label("Warning: Incorrect methods, inputs, or object provided to CharControl");
					Scene toShow = new Scene(warningLabel);
					warning.setScene(toShow);
					warning.show();
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
		if(!currentExecute.getWord().equals("NULL")) {
			return action;
		}
		return "NULL";
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

}
