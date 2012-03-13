package io.obj;

import java.io.FileNotFoundException;

import javax.media.j3d.Shape3D;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import io.Importer;

public class ObjImporter extends Importer {

	@Override
	public Shape3D doLoadObject(String file) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
	   	ObjectFile f = new ObjectFile ();	    
	    return (Shape3D) f.load (file).getSceneGroup().getChild(0);
	}

}
