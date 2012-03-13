package app;

import javax.media.j3d.Shape3D;

import io.Importer;
import io.ply.PlyImporter;
import gui.Simple3DGUI;

public class DefaultApp {

	public static void main(String[] args) {
		Simple3DGUI gui = new Simple3DGUI();
		
		//ObjImporter importer = new ObjImporter();
		Importer importer = new PlyImporter();
		
		Shape3D face;
		try {
			//face = importer.LoadObject("data/00001_20061015_00418_neutral_face05.obj");
			//face = importer.loadObject("data/face_blender.obj");
			face = importer.loadObject("data/00293_20080104_03413_neutral_face05.ply");
			gui.displayRotatingShape(face);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
