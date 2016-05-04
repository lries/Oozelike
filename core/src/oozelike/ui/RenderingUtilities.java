package oozelike.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import oozelike.roguelike.Actor;
import oozelike.roguelike.Core;
import oozelike.roguelike.Visible;

/***************************************************************
 * A class for holding static rendering utility methods.
 * Some may later be integrated into other classes; who knows. 
 ***************************************************************/
public class RenderingUtilities {

	private final static Visible greenHealthBar = new Visible(new Texture(Gdx.files.internal("UI/hb_green.png")), "UI/hb_green.png");
	private final static Visible redHealthBar = new Visible (new Texture(Gdx.files.internal("UI/hb_red.png")), "UI/hb_red.png");
	
	/***********************************************
	 * Draws a health bar for the provided actor
	 * @param a the actor whose health bar to draw
	 ***********************************************/
	public static void drawHealthBar(Actor a){
		float pGreen = (float)a.getCombatManger().getHP() / (float)a.getCombatManger().getMaxHP();
		int nGreen = (int) ((Core.tileSize-2)*pGreen); 
		for (int x=1; x<Core.tileSize-1; x++){
			if (x-1 < nGreen) greenHealthBar.render(a.getX()*Core.tileSize+x, a.getY()*Core.tileSize, 1);
			else redHealthBar.render(a.getX()*Core.tileSize+x, a.getY()*Core.tileSize, 1);
		}
	}
}
