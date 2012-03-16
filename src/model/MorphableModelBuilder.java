package model;

import java.io.File;

import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogLevel;
import util.Log.LogType;

import io.Importer;
import io.Importer.FileType;

public class MorphableModelBuilder {

	public static MorphableModel LoadDirectory(String directory, FileType filetype) {
		File dir = new File(directory);
		if(!dir.exists()) {
			Log.print(LogType.IO, LogLevel.ERROR, "Morphable model builder: Directory not found.");
			return null;
		}
		
		if(dir.listFiles().length == 0) {
			Log.print(LogType.IO, LogLevel.WARNING, "Morphable model builder: No files found to load.");
		}
		
		MorphableModel mm = new MorphableModel();
		Importer importer = filetype.getImporter();
		
		for(File file : dir.listFiles()) {
			if(file.getName().endsWith(filetype.getExtension())) {
				Shape3D shape3d = importer.loadObject(file);

				if(shape3d != null)
					mm.addFace(new Face(shape3d));
			}
		}

		return mm;
	}
}
