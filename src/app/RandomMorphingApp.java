package app;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import parameter.ModelParameter;

import gui.Simple3DGUI;
import io.FileType;

import model.Model;
import model.MorphableModel;
import model.MorphableModelBuilder;
import util.Log;
import util.Log.LogType;

public class RandomMorphingApp {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);
		Log.info(LogType.APP, mm.toString());

		Simple3DGUI gui = new Simple3DGUI();

		ModelParameter.setMorphableModel(mm);
		ModelParameter origin = ModelParameter.getRandom();
		ModelParameter target = ModelParameter.getRandom();
		long start = Calendar.getInstance().getTimeInMillis();
		Model model = mm.getModel(origin);

		gui.displayUnshaded(model);

		while (true) {
			double period = 800d;
			long now = Calendar.getInstance().getTimeInMillis();

			if((now - start) >= period) {
				start += Math.floor((now - start) / period) * period;
				origin = target;
				target = ModelParameter.getRandom();
			}

			mm.updateModel(model, origin.linearApplication(target, (now - start) / period));
		}
	}

}
