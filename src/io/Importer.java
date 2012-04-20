package io;


import java.io.File;
import java.io.IOException;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogType;

public abstract class Importer {

	public Shape3D loadObject(String filename) {
		File file = new File(filename);

		return loadObject(file);
	}

	public Shape3D loadObject(File file) {
		Log.debug(LogType.IO, "Loading: " + file.getName());

		Shape3D shape = null;
		try {
			shape = doLoadObject(file);
		} catch (IOException e) {
			Log.error(LogType.IO, "Importer IO error for " + file + ": " + e.getMessage());
		}

		/* Debug information */
		GeometryArray geometry = (GeometryArray) shape.getGeometry();
		if (geometry == null)
			return shape;

		Log.debug(LogType.IO, "Loaded mesh: " + geometry.getVertexCount() + " vertices.");
		return shape;
	}

	protected abstract Shape3D doLoadObject(File file) throws IOException;
}
