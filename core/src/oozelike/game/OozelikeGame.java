package oozelike.game;
import com.badlogic.gdx.Game;
import oozelike.roguelike.Core;
import oozelike.screens.MainScreen;

public class OozelikeGame extends Game {
	boolean sltest = false; 

	@Override
	/*****************************************************************
	 * Create a new game. 
	 * Sets itself up in the core, gets a seed ready, and sets screen
	 *****************************************************************/
	public void create () {
		Core.game = this; 
		Core.computeNewSeed();
		this.setScreen(new MainScreen());
	}

	@Override
	/****************************************************
	 * Draw the screen. 
	 * (It's a little ambiguous-looking, but it works.)
	 ****************************************************/
	public void render () {
		super.render();
	}
}
