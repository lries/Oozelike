package oozelike.ui;
import java.util.List;
import oozelike.roguelike.Core;
public class Menu {
	public String[] entries;
	private int pointer; 
	private int start; 
	private String select; 
	
	/***********************************************
	 * Create a new text-only menu
	 * @param entries The text entries of the menu
	 ***********************************************/
	public Menu(List<String> entries){
		this.entries = new String[entries.size()];
		for (int x=0; x<this.entries.length; x++){
			this.entries[x] = entries.get(x); 
		}
		this.pointer = 0; 
		this.start = 0; 
		this.select = " >";
	}

	/***********************************************
	 * Create a new text-only menu
	 * @param entries The text entries of the menu
	 * @param select the string to select
	 ***********************************************/
	public Menu(List<String> entries, String select){
		this.entries = new String[entries.size()];
		for (int x=0; x<this.entries.length; x++){
			this.entries[x] = entries.get(x); 
		}
		this.pointer = 0; 
		this.start = 0; 
		this.select = select;
	}
	
	/************************************
	 * Create a new menu
	 * @param entries the menu's entries
	 ************************************/
	public Menu(String[] entries){
		this.entries = entries; 
		this.pointer = 0;
		this.select = " >";
	}

	/************************************
	 * Create a new menu
	 * @param entries the menu's entries
	 ************************************/
	public Menu(String[] entries, String select){
		this.entries = entries; 
		this.pointer = 0;
		this.select = select; 
	}

	/************************************
	 * Increase the position pointer
	 * Probably call this on keystroke
	 ************************************/
	public void increasePointer(){
		pointer++;
		bindPointer();
	}

	/************************************
	 * Decrease the position pointer
	 * Probably call this on keystroke
	 ***********************************/
	public void decreasePointer(){
		pointer--;
		bindPointer(); 
	}

	/******************************************
	 * Return the number of the selected entry
	 * @return the number of the selected entry
	 ******************************************/
	public int click() {
		return pointer; 
	}

	/*******************************************
	 * Render the menu
	 * @param sx the minimum x to render at
	 * @param sy the minimum y to render at
	 * @param gx the maximum x to render at
	 * @param gy the maximum y to render at
	 * @param xbuffer the amount to frame x with
	 * @param ybuffer the amount to frame y with
	 *******************************************/
	public void render(int sx, int sy, int gx, int gy, int xbuffer, int ybuffer){
		//Figure out rendering parameters 
		boolean turnOff = false; 
		if (!Core.batch.isDrawing()){
			turnOff = true; 
			Core.batch.begin();
		}
		
		int numLines = (int) (((gy-ybuffer)-(sy+ybuffer))/Core.font.getLineHeight()) - 1;
		int maxWidth = (int) ((int) ((gx-xbuffer)-(sx+xbuffer))/Core.font.getSpaceWidth());
		if (pointer < start) { 
			start = pointer; 
		}
		if (pointer > start + numLines){
			start = pointer - numLines; 
		}

		int pos = start; 
		//Render visible lines
		for (int y = gy-ybuffer; y > (int) (sy+Core.font.getLineHeight()+ybuffer);  y-=Core.font.getLineHeight()){
			if (pos >= entries.length) break; 
			if (pos == pointer){
				String draw = select+(entries[pos].trim());
				if (draw.length() > maxWidth) draw = draw.substring(0, maxWidth);
				Core.font.draw(Core.batch, draw, sx+xbuffer, y);

			}
			else {
				String draw = (entries[pos].trim());
				if (draw.length() > maxWidth) draw = draw.substring(0, maxWidth);
				Core.font.draw(Core.batch, draw, sx+xbuffer, y);
			}
			pos++;
		}
		if (turnOff) Core.batch.end();
	}

	/************************************
	 * Bind the pointer to a legal value
	 ************************************/
	private void bindPointer() { 
		if (pointer < 0) {
			pointer = entries.length - 1; 
		}
		if (pointer > entries.length - 1) { 
			pointer = 0; 
		}
	}
}
