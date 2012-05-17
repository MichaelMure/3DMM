package app;

import io.FileType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import model.MorphableModel;
import model.MorphableModelBuilder;

import render.FittingStrategy;

public class FittingApp {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);

		try {
			MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);
			BufferedImage target = ImageIO.read(new File("blah.png"));

			FittingStrategy fs = new FittingStrategy(mm, target);
			fs.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
