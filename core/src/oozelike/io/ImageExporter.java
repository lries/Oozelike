package oozelike.io;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import oozelike.generators.TileFactory;
import oozelike.roguelike.Dungeon;
import oozelike.roguelike.Tile;

public class ImageExporter {

	/*******************************************
	 * Export a dungeon as a PNG image
	 * @param d the dungeon
	 * @param filename the filename to save to
	 *******************************************/
	public static void ExportDungeonAsPNG(Dungeon d, String filename){
		int width = d.getWidth();
		int height = d.getHeight(); 
		Tile floor = TileFactory.getCaveFloorTile();
		//exports heightmap with waterlevel and mountainlevel to file filename

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		for (int i=0; i<width; i++){
			for (int i2=0; i2<height; i2++){
				if (d.getTile(i,i2).equals(floor)){
					g.setColor(new Color(1f,1f,1f));
					g.fillRect(i, i2, 1, 1);
				}
				else {
					g.setColor(new Color(0,0,0));
					g.fillRect(i, i2, 1, 1);

				}
			}
		}
		try {
			File file = new File(filename);
			ImageIO.write(bi, "png", file);
		} catch (IOException e) {
			System.out.println("World to image conversion failed.");
			return;
		}
	}
}
