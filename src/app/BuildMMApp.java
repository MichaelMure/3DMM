package app;

import java.util.logging.Level;
import java.util.logging.Logger;

import parameter.ModelParameter;

import gui.Simple3DGUI;
import io.FileType;
import model.MorphableModel;
import model.MorphableModelBuilder;
import util.Log;
import util.Log.LogType;

public class BuildMMApp {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);


		Simple3DGUI gui = new Simple3DGUI();

		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);

		gui.displayUnshaded(mm.getAverage());
		Log.info(LogType.APP, mm.toString());
	}
}
