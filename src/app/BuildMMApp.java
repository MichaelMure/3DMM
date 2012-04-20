package app;

import gui.SimpleFaceGUI;
import io.FileType;
import model.MorphableModel;
import model.MorphableModelBuilder;
import util.Log;
import util.Log.LogType;

public class BuildMMApp {

	public static void main(String[] args) {
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);
		Log.info(LogType.APP, mm.toString());

		SimpleFaceGUI gui = new SimpleFaceGUI();
		//gui.displayStaticShape(mm.getAverage().getShape3D());
		//gui.displayRotatingShape(mm.getAverage().getShape3D());
		gui.displayRotatingShape(mm.getAverage().getShape3D());
		Log.info(LogType.APP, mm.toString());
		gui.run();
	}
}
