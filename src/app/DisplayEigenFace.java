package app;

import java.util.logging.Level;
import java.util.logging.Logger;

import gui.Simple3DGUI;
import io.FileType;
import model.MorphableModel;
import model.MorphableModelBuilder;
import util.Log;
import util.Log.LogType;

public class DisplayEigenFace {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);
		/* Compute the maximum number of eigen face available */
		mm.compute(mm.getSize());

		Log.info(LogType.APP, mm.toString());

		Simple3DGUI gui = new Simple3DGUI();
		gui.displayUnshaded(mm.getReducedModel(0));

		Log.info(LogType.APP, mm.toString());
	}

}
