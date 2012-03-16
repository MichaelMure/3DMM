package io.obj;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.media.j3d.Shape3D;

import com.sun.j3d.loaders.objectfile.ObjectFile;

import io.Importer;

public class ObjImporter extends Importer {

	@Override
	public Shape3D doLoadObject(File file) throws IOException {
		ObjectFile f = new ObjectFile ();
		return (Shape3D) f.load (new FileReader(file)).getSceneGroup().getChild(0);
	}

}
