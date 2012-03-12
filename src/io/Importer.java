package io;

import java.io.IOException;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogLevel;
import util.Log.LogType;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;

public abstract class Importer {

	public BranchGroup loadObject(String file) throws IncorrectFormatException, ParsingErrorException, IOException {
		/* Actual loading */
		BranchGroup obj = doLoadObject(file);

		/* Debug information */
		Shape3D shape = (Shape3D) obj.getChild(0);
		if(shape == null)
			return obj;
		
		GeometryArray geometry = (GeometryArray) shape.getGeometry();
		if(geometry == null)
			return obj;
		
		Log.print(LogType.IO, LogLevel.DEBUG, "Loaded mesh: " + geometry.getVertexCount() + " vertices.");
		return obj;
	}
	
	protected abstract BranchGroup doLoadObject(String file) throws IncorrectFormatException, ParsingErrorException, IOException;
}
