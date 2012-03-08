package io.obj;

import java.io.FileNotFoundException;

import javax.media.j3d.BranchGroup;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import io.Importer;

public class ObjImporter implements Importer {

	@Override
	public BranchGroup LoadObject(String file) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
	   	ObjectFile f = new ObjectFile ();
	    f.setFlags (ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
	    
	    return f.load (file).getSceneGroup();
	}

}
