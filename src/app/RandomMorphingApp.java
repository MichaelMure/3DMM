package app;

import gui.SimpleFaceGUI;
import io.Importer.FileType;

import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;

import model.FaceParameter;
import model.MorphableModel;
import model.MorphableModelBuilder;
import util.Log;
import util.Log.LogType;

public class RandomMorphingApp {

	public static void main(String[] args) {
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);
		Log.info(LogType.APP, mm.toString());

		SimpleFaceGUI gui = new SimpleFaceGUI();
		Shape3D shape = mm.getFace(FaceParameter.getRandomFaceParameter(mm.getSize())).getShape3D();

		RandomMorphingUpdater updater = new RandomMorphingUpdater(mm, (IndexedTriangleArray) shape.getGeometry());

		gui.displayStaticShape(shape);
		gui.addBehavior(updater.getUpdateBehavior());
		gui.run();
	}

}
