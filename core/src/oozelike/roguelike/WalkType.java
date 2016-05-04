package oozelike.roguelike;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public enum WalkType implements Serializable {
	LIGHT,
	OOZE, 
	WALKER,
	SWIMMING_WALKER,
	EXCLUSIVE_SWIMMER,
	FLYER,
	EXCLUSIVE_FLYER,
	SWIMMING_FLYER,
	EXCLUSIVE_MAGMA,
	MAGMA_WALKER,
	MAGMA_SWIMMER,
	MAGMA_FLYER,
	MAGMA_FLY_SWIM,
	DEITY;

	/**************************************
	 * Get all walktypes who move in magma
	 * @return the appropriate list
	 **************************************/
	public static Set<WalkType> getMagmaMovers(){
		Set<WalkType> magma = new HashSet<WalkType>();
		magma.add(EXCLUSIVE_MAGMA);
		magma.add(MAGMA_WALKER);
		magma.add(MAGMA_SWIMMER);
		magma.add(MAGMA_FLYER);
		magma.add(MAGMA_FLY_SWIM);
		magma.add(DEITY);
		return magma; 
	}
	
	/**************************************
	 * Get all walktypes who move in water
	 * @return the appropriate list
	 **************************************/
	public static Set<WalkType> getSwimmers(){
		Set<WalkType> swimmers = new HashSet<WalkType>();
		swimmers.add(EXCLUSIVE_SWIMMER);
		swimmers.add(SWIMMING_WALKER);
		swimmers.add(SWIMMING_FLYER);
		swimmers.add(MAGMA_SWIMMER);
		swimmers.add(MAGMA_FLY_SWIM);
		swimmers.add(DEITY);
		return swimmers;
	}

	/**************************************
	 * Get all walktypes who can fly
	 * @return the appropriate list
	 **************************************/
	public static Set<WalkType> getFlyers(){
		Set<WalkType> flyers = new HashSet<WalkType>();
		flyers.add(EXCLUSIVE_FLYER);
		flyers.add(MAGMA_FLY_SWIM);
		flyers.add(SWIMMING_FLYER);
		flyers.add(MAGMA_FLYER);
		flyers.add(FLYER);
		flyers.add(DEITY);
		return flyers; 
	}
}
