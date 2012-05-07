package model;

import io.FileType;
import io.Importer;

import java.io.File;

import com.jme3.scene.Mesh;

import util.Log;
import util.Log.LogType;
import util.TimeCounter;

/** This class is a helper to create a MorphableModel and load it with 3D scan from a directory. */
public class MorphableModelBuilder {

	/** @return a new MorphableModel, loaded with all the files from a directory.
	 *  @param directory the path of the directory to load
	 *  @param the FileType to load (the importer is automatically chosen).
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

		TimeCounter t = new TimeCounter("Loading files for the morphable model");
		for(File file : dir.listFiles()) {
			if(file.getName().endsWith(filetype.getExtension())) {
				Mesh mesh = importer.loadObject(file);

				if(mesh != null)
					mm.addModel(new Model(mesh));
			}
		}
		t.stop();

		return mm;
	}
}
