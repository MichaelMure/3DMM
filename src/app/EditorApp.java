package app;

import io.FileType;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.MorphableModel;
import model.MorphableModelBuilder;

import editor.Editor;

public class EditorApp {

	public static void main(String[] args) throws IOException {
		Logger.getLogger("").setLevel(Level.WARNING);
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);

		@SuppressWarnings("unused")
		Editor editor = new Editor(mm);
	}
}
