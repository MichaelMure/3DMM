package app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import model.MorphableModel;
import model.MorphableModelLoader;

import render.ErrorTracer;
import util.Log;
import util.Log.LogType;

public class ErrorTracerApp {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		Log.disableType(LogType.TIME);

		try {
			MorphableModel mm = MorphableModelLoader.loadMAT(10);
			BufferedImage target = ImageIO.read(new File("target.png"));

			ErrorTracer fs = new ErrorTracer(mm, target);
			fs.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
