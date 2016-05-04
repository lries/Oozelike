package oozelike.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*****************************************************
 * KeyLog - recall all keystrokes
 * In the future, this will allow the game to replay 
 * It's not implemented yet
 *****************************************************/
public class KeyLog implements Serializable {
	private static final long serialVersionUID = -6800283170295039796L;

	private List<Integer> log; 

	/********************
	 * Create a key log
	 ********************/
	public KeyLog(){ 
		log = new ArrayList<Integer>(); 
	}

	/*********************************
	 * Log a key
	 * @param key the keycode to log
	 *********************************/
	public void log(int key) {
		log.add(key); 
	}
}
