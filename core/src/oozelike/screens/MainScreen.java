package oozelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Screen;

import oozelike.generators.ActorFactory;
import oozelike.generators.WorldFactory;
import oozelike.io.SaveLoadManager;
import oozelike.roguelike.Actor;
import oozelike.roguelike.Core;
import oozelike.roguelike.World;
import oozelike.ui.Menu;

/*********************************
 * Main menu screen class.
 *********************************/
public class MainScreen implements Screen {
	private Menu menu;
	private OrthographicCamera camera; 
	
	/**********************
	 * Create a main menu.
	 **********************/
	public MainScreen(){
		String[] options = {"Start a new game", "Load a saved game"};
		menu = new Menu(options);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 530);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.f, 0.f, 0.f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Core.batch.setProjectionMatrix(camera.combined);
		menu.render(0, 0, (int) camera.viewportWidth, (int) camera.viewportHeight, 5, 5);
		if (Gdx.input.isKeyJustPressed(Keys.UP)){
			menu.increasePointer();
		}
		else if (Gdx.input.isKeyJustPressed(Keys.DOWN)){
			menu.decreasePointer(); 
		}
		else if (Gdx.input.isKeyJustPressed(Keys.ENTER)){
			switch (menu.click()){
			case 0: 
				startNewGame(); break; 
			default:
				loadSavedGame(); break; 
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
	
	/***************************
	 * Start a brand new game
	 ***************************/
	private void startNewGame(){
		World w = WorldFactory.generateRandomAutomataWorld(100, 100, .10f, .90f, 5, false, 5);
		Actor player = ActorFactory.getPlayer();
		w.activeDungeon().randomlyAddActor(player);
		player.makeFocus();
		for (int x=0; x<100; x++){
			Actor ooze = ActorFactory.getOoze(); 
			w.activeDungeon().randomlyAddActor(ooze); 
		}
		for (int x=0; x<20; x++){
			Actor chompy = ActorFactory.getChompy();
			w.activeDungeon().randomlyAddActor(chompy);
		}
		SaveLoadManager.save(w);
		Core.game.setScreen(new DungeonExploreScreen(w));
	}
	
	/*********************
	 * Load a saved game
	 *********************/
	private void loadSavedGame(){
		World w = SaveLoadManager.load();
		if (w == null) startNewGame(); 
		Core.game.setScreen(new DungeonExploreScreen(w));
	}	
}
