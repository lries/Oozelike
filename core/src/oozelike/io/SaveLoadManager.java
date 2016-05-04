package oozelike.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import oozelike.roguelike.Core;
import oozelike.roguelike.World;

public class SaveLoadManager {

	/**********************************
	 * Save a file to dungeon.save
	 * @param w the game world
	 **********************************/
	public static void save(World w){ 
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream("dungeon.save"));
			SaveFile s = new SaveFile(w, Core.asSaveCore());
			out.writeObject(s); 
			out.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/*********************************
	 * Load a file from dungeon.save
	 * @return the loaded game world
	 *********************************/
	public static World load(){
		ObjectInputStream in; 
		try {
			in = new ObjectInputStream(new FileInputStream("dungeon.save"));
			SaveFile s = (SaveFile) in.readObject(); 
			in.close();
			s.core.load(); 
			return s.dungeon; 
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		} 
		return null; 
	}
}
