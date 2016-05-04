package oozelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import oozelike.roguelike.Core;
import oozelike.roguelike.World;

/****************************************************
 * A screen for exploring the dungeon (as an actor)
 ****************************************************/
public class DungeonExploreScreen implements Screen {
	private World dungeon;
	private OrthographicCamera camera; 
	boolean first = true; 
	public DungeonExploreScreen(World d) {
		dungeon = d; 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 530);
	}

	@Override
	/**********************************************
	 * Called when the screen is initially shown 
	 **********************************************/
	public void show() {
		camera.position.x = Core.focusX*Core.tileSize; 
		camera.position.y = Core.focusY*Core.tileSize; 
	}

	@Override
	/**********************************************
	 * Called when the screen is rendered
	 * @param delta time since last render
	 **********************************************/
	public void render(float delta) {
		Gdx.gl.glClearColor(0.f, 0.f, 0.f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Core.batch.setProjectionMatrix(camera.combined);
		camera.position.x = (int) (Core.focusX*Core.tileSize+Core.tileSize/2); 
		camera.position.y = (int) (Core.focusY*Core.tileSize+Core.tileSize/2); 
		updateViewport(); 
		Core.batch.begin();
		dungeon.render(Core.viewLX, Core.viewLY, Core.viewHX, Core.viewHY);
		Core.redraw = false; 
		camera.update();
		Core.batch.end();
		if (!first){
			act();
		}
		first = false; 
	}

	/**************************************************
	 * Makes the dungeon act until it's time to redraw
	 **************************************************/
	public void act() {
		while (!Core.redraw){
			dungeon.runTurn();
		}
	}

	/*************************************
	 * Updates the viewport position
	 *************************************/
	public void updateViewport(){
		Core.viewLX = (int) (Core.focusX-camera.viewportWidth/Core.tileSize/2);
		Core.viewLY = (int) (Core.focusY-camera.viewportHeight/Core.tileSize/2+4);
		Core.viewHX = (int) (Core.focusX+camera.viewportWidth/Core.tileSize/2+1);
		Core.viewHY = (int) (Core.focusY+camera.viewportHeight/Core.tileSize/2+2);

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	/************************************************************************
	 * Called on resume. Guarantees a render completes before any actions.
	 ************************************************************************/
	@Override
	public void resume() {
		first = true; 
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	} 
}
