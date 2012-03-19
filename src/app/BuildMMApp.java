package app;

import gui.Simple3DGUI;
import io.Importer.FileType;
import model.MorphableModel;
import model.MorphableModelBuilder;

public class BuildMMApp {

	public static void main(String[] args) {
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);
		System.out.println(mm);
		
		Simple3DGUI gui = new Simple3DGUI();
		gui.displayStaticShape(mm.getAverage().getShape3D());
		//gui.displayRotatingShape(mm.getAverage().getShape3D());
	}
}
