package io;

import java.io.IOException;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogLevel;
import util.Log.LogType;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;

public abstract class Importer {

	public Shape3D loadObject(String file) throws IncorrectFormatException, ParsingErrorException, IOException {
		/* Actual loading */
		Shape3D shape = doLoadObject(file);

		/* Debug information */
		GeometryArray geometry = (GeometryArray) shape.getGeometry();
		if(geometry == null)
			return shape;
		
		Log.print(LogType.IO, LogLevel.DEBUG, "Loaded mesh: " + geometry.getVertexCount() + " vertices.");
		return shape;
	}
	
	protected abstract Shape3D doLoadObject(String file) throws IncorrectFormatException, ParsingErrorException, IOException;
}
