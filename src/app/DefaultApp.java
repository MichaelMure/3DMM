package app;

import javax.media.j3d.Node;

import io.obj.ObjImporter;
import gui.Simple3DGUI;

public class DefaultApp {

	public static void main(String[] args) {
		Simple3DGUI gui = new Simple3DGUI();
		
		ObjImporter importer = new ObjImporter();
		
		Node face;
		try {
			//face = importer.LoadObject("data/00001_20061015_00418_neutral_face05.obj");
			face = importer.LoadObject("data/face_blender.obj");
			gui.displayRotatingNode(face);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
