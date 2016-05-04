package oozelike.roguelike;
import java.io.Serializable;

import com.badlogic.gdx.graphics.Texture;

/********************************************************************************
 * Represents an item that can be rendered. 
 * This is the only source of direct LibGDX render calls outside of the Screens.
 ********************************************************************************/
public class Visible implements Serializable {
	private static final long serialVersionUID = -4670454837384299178L;
	private transient Texture t; 
	private String loc; 
	
	/***************************************************************
	 * Create a visible
	 * @param texture the texture to use
	 * @param loc the location it's at (for serialization purposes)
	 ***************************************************************/
	public Visible(Texture texture, String loc) {
		t = texture; 
		this.loc = loc; 
	}

	/***********************************
	 * Regenerate the texture from file
	 * (probably on reload)
	 ***********************************/
	public void regenTexture(){
		t = Core.visFactory.get(loc);
	}
	
	/***********************************
	 * Render the texture at tile x, y
	 * @param x position
	 * @param y position
	 ***********************************/
	public void render(int x, int y) {
		if (t == null) regenTexture(); 
		Core.batch.draw(t, x*Core.tileSize, y*Core.tileSize);
	}
	
	/**********************************************
	 * Render the tile at pixel x*offset, y*offset
	 * @param x position
	 * @param y position 
	 * @param offset position multiplier
	 **********************************************/
	public void render(int x, int y, int offset){
		if (t == null) regenTexture(); 
		Core.batch.draw(t, x*offset, y*offset);
	}
	
	/*********************************************************************
	 * Determine equality.
	 * Uses texture memory equality (since no texture is loaded twice.)
	 * @param v the Visible to check against
	 * @return if they are equal. 
	 *********************************************************************/
	public boolean equals(Visible v){
		return t == v.t;
	}
}
