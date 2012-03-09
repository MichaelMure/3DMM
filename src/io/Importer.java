package io;

import java.io.FileNotFoundException;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogLevel;
import util.Log.LogType;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;

public abstract class Importer {

	public BranchGroup loadObject(String file) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
		BranchGroup obj = doLoadObject(file);

		Shape3D shape = (Shape3D) obj.getChild(0);
		if(shape == null)
			return obj;
		
		GeometryArray geometry = (GeometryArray) shape.getGeometry();
		if(geometry == null)
			return obj;
		
		Log.print(LogType.IO, LogLevel.DEBUG, "Loaded mesh: " + geometry.getVertexCount() + " vertices.");
		return obj;
	}
	
	protected abstract BranchGroup doLoadObject(String file) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException;
}
