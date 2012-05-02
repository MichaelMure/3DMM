package io;

import java.io.File;
import java.io.IOException;

import com.jme3.scene.Mesh;

import util.Log;
import util.Log.LogType;

public abstract class Exporter {

	public void exportObject(Mesh object, String filename) {
		File file = new File(filename);

		exportObject(object, file);
	}

	public void exportObject(Mesh object, File file) {
		Log.debug(LogType.IO, "Exporting: " + file.getName());

		try {
			doExportObject(object, file);
		} catch (IOException e) {
			Log.error(LogType.IO, "Exporter IO error for " + file + ": " + e.getMessage());
		}
	}

	protected abstract void doExportObject(Mesh object, File file) throws IOException;
}