package model;

import io.Importer;
import io.Importer.FileType;

import java.io.File;

import javax.media.j3d.Shape3D;

import util.Log;
import util.Log.LogType;

/** This class is a helper to create a MorphableModel and load it with 3D scan from a directory. */
public class MorphableModelBuilder {

	/** @return a new MorphableModel, loaded with all the files from a directory.
	 *  @param directory the path of the directory to load
	 *  @param the FileType to load (the importer is automatically choosen).
	 */
	public static MorphableModel LoadDirectory(String directory, FileType filetype) {
		File dir = new File(directory);
		if(!dir.exists()) {
			Log.error(LogType.IO, "Morphable model builder: Directory not found.");
			return null;
		}

		if(dir.listFiles().length == 0) {
			Log.warning(LogType.IO, "Morphable model builder: No files found to load.");
		}

		MorphableModel mm = new MorphableModel();
		Importer importer = filetype.getImporter();

		for(File file : dir.listFiles()) {
			if(file.getName().endsWith(filetype.getExtension())) {
				Shape3D shape3d = importer.loadObject(file);

				if(shape3d != null)
					mm.addModel(new Model(shape3d));
			}
		}

		return mm;
	}
}
