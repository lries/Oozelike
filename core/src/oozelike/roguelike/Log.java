package oozelike.roguelike;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import oozelike.ui.Menu;

public class Log implements Serializable {
	private static final long serialVersionUID = -3820903880678263935L;
	private List<String> messages; 
	int size; 
	
	/***********************************************************
	 * Get a log
	 * Initializes to no messages
	 ***********************************************************/
	public Log(){
		messages = new ArrayList<String>(); 
		size = 0; 
	}
	
	/************************************************************
	 * Log a message
	 * @param message the message to be logged
	 ************************************************************/
	public void logMessage(String message){
		messages.add(message);
		size++; 
	}
	
	/************************************************************
	 * Gets an array of at most a provided length
	 * @param numMessages the maximum number of messages to get
	 * @return an array of the numMessages most recent messages 
	 ************************************************************/
	public String[] getNMessages(int numMessages){
		String[] ret = new String[Math.min(numMessages, size)]; 
		int loops = 0; 
		for (int x=size-1; x >= 0 && loops < numMessages; x++){
			ret[loops]=messages.get(x); 
			loops++; 
		}
		return ret; 
	}
	
	/************************************************
	 * Return the log as a no-select menu
	 * (effectively as a scrollable rendering item) 
	 * @return the menu it creates
	 ************************************************/
	public Menu asMenu(){
		return new Menu(messages, "");
	}
}
