package oozelike.roguelike;

import java.io.Serializable;

/***********************
 * Represents a stair
 ***********************/
public class StairData implements Serializable {
	private static final long serialVersionUID = -7553601581645833720L;
	private boolean up; 
	private Dungeon container; 
	private Dungeon portalTo; 
	private StairData partner; 
	
	/******************************************************************************
	 * Create a stair data
	 * @param up whether this goes up
	 * @param container the dungeon this is in
	 * @param portalTo the dungeon it goes to
	 * @param partner the corresponding stair on the other dungeon (if it exists)
	 ******************************************************************************/
	public StairData(boolean up, Dungeon container, Dungeon portalTo, StairData partner){
		this.up = up; 
		this.container = container;
		this.portalTo = portalTo; 
		this.partner = partner; 
	}
	
	//Getters and setters
	public StairData getPartner(){ return partner; }
	public boolean getUp(){ return up; }
	public Dungeon getContainer(){ return container; }
	public Dungeon getPortalTo(){ return portalTo; }

	public void setPortalTo(Dungeon portalTo){ this.portalTo = portalTo; }
	public void setPartner(StairData partner) { this.partner = partner; }
}
