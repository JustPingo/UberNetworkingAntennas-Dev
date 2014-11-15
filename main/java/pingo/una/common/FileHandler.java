package pingo.una.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraftforge.common.DimensionManager;

 /*
 * 
 * The FileHandler handles File management, it saves Satellites and load them.
 * 
 */

public class FileHandler {

	public boolean writeSatellites(String path, List<Satellite> toWrite) {
		
		if (toWrite == null) return false;
		if (DimensionManager.getCurrentSaveRootDirectory() == null) return false;
		
		boolean toReturn = false;
	    ObjectOutputStream oos = null; // Crash
	    try {
	    
	      File file = new File(DimensionManager.getCurrentSaveRootDirectory() + path);
	      if (file.exists()) file.delete();
	      file.createNewFile();
	      oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	      
	      for (Iterator<Satellite> it = toWrite.listIterator(); it.hasNext();) oos.writeObject(it.next());
	      
	      toReturn = true;
	      
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    try {
			if (oos != null) oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
    	return toReturn;
	}
	     
	public List<Satellite> loadSatellites(String path) {

		  ObjectInputStream ois = null;
		  List<Satellite> toReturn = new ArrayList<Satellite>();
	            
	      try {
	    	File file = new File(DimensionManager.getCurrentSaveRootDirectory() + path);
	    	
	    	if (file.exists()) {
		    	ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
		        
			    boolean loop = true;
			    while (loop) {
			    	try {
			    		toReturn.add((Satellite) ois.readObject());
			    	} catch (ClassNotFoundException e) {
			    		loop = false;
			    	} catch (java.io.EOFException e) {
			    		loop = false;
			    	}
			    }
	    	}
	      
		  } catch (FileNotFoundException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
		  }
	      
	      try {
			if (ois != null) ois.close();
	      } catch (IOException e) {
			e.printStackTrace();
	      }

		return toReturn;  	
		
	}
	
}
