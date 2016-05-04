package oozelike.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import oozelike.roguelike.Core;
import oozelike.roguelike.World;

/**************************************************************
 * Create a dungeon overview screen - a navigable map.
 * Currently *ignores* known info, etc. It's for cheaters! :V 
 **************************************************************/
public class DungeonOverviewScreen implements Screen{
	private World dungeon; 
	OrthographicCamera camera;
	long inputWait = 0; 
	long lastTime = 0; 
	public DungeonOverviewScreen(World d){
		dungeon = d; 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		System.out.println(camera.position.x+","+camera.position.y);
	}

	@Override
	public void show() {
		System.out.println("A dungeon screen"); 
	}

	@Override
	/******************************************************************************
	 * Render the map. 
	 * Also takes input, if it's available and it's been sufficiently long ago.
	 * @param delta used by LibGDX, just ignore it 
	 ******************************************************************************/
	public void render(float delta) {
		inputWait += System.currentTimeMillis()-lastTime;
		lastTime = System.currentTimeMillis(); 
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		Core.batch.setProjectionMatrix(camera.combined);
		Core.batch.begin(); 
		dungeon.render(true);
		Core.batch.end();
		if (inputWait > 100){
			if (Gdx.input.isKeyJustPressed(Keys.E)){
				Core.game.setScreen(new DungeonExploreScreen(dungeon));
			}
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				camera.position.x-=Core.tileSize;
				if (camera.position.x < 0) {
					camera.position.x = 0; 
					camera.position.x -= camera.position.x%(Core.tileSize);
					camera.position.x += Core.tileSize/2; 
				}
				inputWait = 0; 
			}
			else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
				camera.position.x+=Core.tileSize;
				inputWait = 0; 
			}
			else if (Gdx.input.isKeyPressed(Keys.UP)){
				camera.position.y += Core.tileSize; 
				inputWait = 0; 
			}
			else if (Gdx.input.isKeyPressed(Keys.DOWN)){
				camera.position.y-=Core.tileSize;
				if (camera.position.y < 0){
					camera.position.y = 0;
					camera.position.y -= camera.position.y%(Core.tileSize);
					camera.position.y += Core.tileSize/2;
				}
				inputWait = 0; 
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
}
