package oozelike.io;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import oozelike.roguelike.Core;

public class KeyLogger implements Serializable {
	private static final long serialVersionUID = 4189897956251693289L;
	private static final long speed = 125; 
	private static long lastTime = System.currentTimeMillis();

	/*************************************************************************
	 * Check if a specific keycode has been pressed or held down long enough
	 * @param keycode the keycode value
	 * @return true if it hsa been triggered, else false
	 *************************************************************************/
	public boolean checkKey(int keycode){
		long time = System.currentTimeMillis();
		if ((Gdx.input.isKeyPressed(keycode) && time-lastTime > speed) || Gdx.input.isKeyJustPressed(keycode)){
			Core.keyLog.log(keycode);
			lastTime = System.currentTimeMillis(); 
			return true; 
		}
		return false; 
	}

	/*******************************************
	 * Check if a key has been newly pressed
	 * @param keycode the keycode to check
	 * @return whether it is newly pressed
	 *******************************************/
	public boolean checkNewKey(int keycode){
		if (Gdx.input.isKeyJustPressed(keycode)){
			Core.keyLog.log(keycode);
			lastTime = System.currentTimeMillis(); 
			return true; 
		}
		return false; 
	}
	
	/********************************************
	 * Check keys for presses
	 * Automatically converts synonymous keys
	 * @return the highest priority active key
	 ********************************************/
	public Integer checkKeys(){
		if (checkKey(Keys.UP) || checkKey(Keys.W)) return Keys.UP;
		if (checkKey(Keys.DOWN) || checkKey(Keys.S)) return Keys.DOWN;
		if (checkKey(Keys.LEFT) || checkKey(Keys.A)) return Keys.LEFT;
		if (checkKey(Keys.RIGHT) || checkKey(Keys.D)) return Keys.RIGHT;
		if (checkNewKey(Keys.X)) return Keys.X;
		if (checkNewKey(Keys.E)) return Keys.E; 
		return null; 
	}
}
