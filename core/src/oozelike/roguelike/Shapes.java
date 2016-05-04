package oozelike.roguelike;
import java.util.ArrayList;
import java.util.List;

public class Shapes {
	/********************************************************
	 * Draw a line using a Bresenham variant
	 * Based off an implementation from tech-algorithm.com
	 * @param sx starting x
	 * @param sy starting y
	 * @param gx goal x
	 * @param gy goal y
	 * @return a List<int[]> representing the line
	 ********************************************************/
	public static List<int[]> getLine(int x, int y, int x2, int y2) {
		List<int[]> ret = new ArrayList<int[]>(); 
		int w = x2 - x ;
		int h = y2 - y ;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
		if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
		if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
		if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
		int longest = Math.abs(w) ;
		int shortest = Math.abs(h) ;
		if (!(longest>shortest)) {
			longest = Math.abs(h) ;
			shortest = Math.abs(w) ;
			if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
			dx2 = 0 ;            
		}
		int numerator = longest >> 1 ;
		for (int i=0;i<=longest;i++) {
			int[] loc = {x, y};
			ret.add(loc); 
			numerator += shortest ;
			if (!(numerator<longest)) {
				numerator -= longest ;
				x += dx1 ;
				y += dy1 ;
			} else {
				x += dx2 ;
				y += dy2 ;
			}
		}
		return ret; 
	}
}