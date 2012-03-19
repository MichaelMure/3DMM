package io;

import io.obj.ObjImporter;
import io.ply.PlyImporter;

import java.io.File;
import java.io.IOException;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogLevel;
import util.Log.LogType;

public abstract class Importer {

	public enum FileType {
		PLY, OBJ;

		public String getExtension() {
			switch (this) {
			case PLY:
				return ".ply";
			case OBJ:
				return ".obj";
			}
			assert false;
			return null;
		}

		public Importer getImporter() {
			switch (this) {
			case PLY:
				return new PlyImporter();
			case OBJ:
				return new ObjImporter();
			}
			assert false;
			return null;
		}
	}

	public Shape3D loadObject(String filename) {
		File file = new File(filename);

		return loadObject(file);
	}

	public Shape3D loadObject(File file) {
		Log.print(LogType.IO, LogLevel.DEBUG, "Loading: " + file.getName());
		
		Shape3D shape = null;
		try {
			shape = doLoadObject(file);
		} catch (IOException e) {
			Log.print(LogType.IO, LogLevel.ERROR, "Importer IO error for " + file + ": " + e.getMessage());
		}

		/* Debug information */
		GeometryArray geometry = (GeometryArray) shape.getGeometry();
		if (geometry == null)
			return shape;

		Log.print(LogType.IO, LogLevel.DEBUG, "Loaded mesh: " + geometry.getVertexCount() + " vertices.");
		return shape;
	}

	protected abstract Shape3D doLoadObject(File file) throws IOException;
}
