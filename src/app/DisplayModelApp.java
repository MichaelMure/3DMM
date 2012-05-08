package app;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.Model;

import com.jme3.scene.Mesh;

import gui.Simple3DGUI;
import io.Importer;
import io.ply.PlyImporter;

public class DisplayModelApp {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		Simple3DGUI gui = new Simple3DGUI();

		Importer importer = new PlyImporter();

		try {
			Mesh face = importer.loadObject("data/00017_20061201_00812_neutral_face05_BAD.ply");
			gui.displayUnshaded(new Model(face));

			//face = importer.LoadObject("data/00001_20061015_00418_neutral_face05.obj");
			//face = importer.loadObject("data/face_blender.obj");
			//face = importer.loadObject("data/00293_20080104_03413_neutral_face05.ply");
			//gui.displayRotatingShape(face);
			//gui.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
