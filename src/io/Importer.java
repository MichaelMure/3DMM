package io;


import java.io.File;
import java.io.IOException;

import com.jme3.scene.Mesh;

import util.Log;
import util.Log.LogType;

public abstract class Importer {

	public Mesh loadObject(String filename) {
		File file = new File(filename);

		return loadObject(file);
	}

	public Mesh loadObject(File file) {
		Log.debug(LogType.IO, "Loading: " + file.getName());

		Mesh m = null;
		try {
			m = doLoadObject(file);
		} catch (IOException e) {
			Log.error(LogType.IO, "Importer IO error for " + file + ": " + e.getMessage());
		}

		Log.debug(LogType.IO, "Loaded mesh: " + m.getVertexCount() + " vertices, "
				+ m.getTriangleCount() + " triangles.");
		return m;
	}

	protected abstract Mesh doLoadObject(File file) throws IOException;
}
