package io;

import io.ply.PlyImporter;

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
		}
		assert false;
		return null;
	}
}