package oozelike.roguelike;

import java.io.Serializable;

/*******************************************************************************************
 * Enum representing body parts
 * For instance, a millipede might be represented as
 * {NECK:1, HEAD:1, LEFT_LEG: 50, RIGHT_LEG: 50, LEFT_FOOT: 50, RIGHT_FOOT: 50}
 * Mostly used for gear, e.g. pants require two matching legs
 * A millipede man could wear a hat, a necklace, and a lot of boots! 
 * Likely to be changed by refactoring, so don't depend on its values being unchanging! 
 *******************************************************************************************/
public enum BodyPart implements Serializable {
	LEFT_HAND,
	RIGHT_HAND,
	LEFT_ARM,
	RIGHT_ARM,
	
	LEFT_LEG,
	RIGHT_LEG,
	LEFT_FOOT,
	RIGHT_FOOT,
	
	CHEST,
	BELLY, 
	WAIST,
	SHOULDERS,
	
	NECK,
	HEAD,
	FACE,
	
	TAIL, JELLY; 
}
