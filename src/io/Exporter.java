package io;

import java.io.File;
import java.io.IOException;

import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogType;

public abstract class Exporter {

	public void exportObject(Shape3D object, String filename) {
		File file = new File(filename);

		exportObject(object, file);
	}

	public void exportObject(Shape3D object, File file) {
		Log.debug(LogType.IO, "Exporting: " + file.getName());

		try {
			doExportObject(object, file);
		} catch (IOException e) {
			Log.error(LogType.IO, "Exporter IO error for " + file + ": " + e.getMessage());
		}
	}

	protected abstract void doExportObject(Shape3D object, File file) throws IOException;
}