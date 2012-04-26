package app;

import gui.SimpleFaceGUI;
import io.FileType;
import model.MorphableModel;
import model.MorphableModelBuilder;
import util.Log;
import util.Log.LogType;

public class DisplayEigenFace {

	public static void main(String[] args) {
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);
		Log.info(LogType.APP, mm.toString());

		SimpleFaceGUI gui = new SimpleFaceGUI();
		gui.displayStaticShape(mm.getReducedModel(0).getShape3D());
		gui.run();

		SimpleFaceGUI gui2 = new SimpleFaceGUI();
		gui2.displayStaticShape(mm.getReducedModel(1).getShape3D());
		gui2.run();

		SimpleFaceGUI gui3 = new SimpleFaceGUI();
		gui3.displayStaticShape(mm.getReducedModel(2).getShape3D());
		gui3.run();

		SimpleFaceGUI gui4 = new SimpleFaceGUI();
		gui4.displayStaticShape(mm.getReducedModel(3).getShape3D());
		gui4.run();

		SimpleFaceGUI gui5 = new SimpleFaceGUI();
		gui5.displayStaticShape(mm.getReducedModel(4).getShape3D());
		gui5.run();

		Log.info(LogType.APP, mm.toString());
	}

}
