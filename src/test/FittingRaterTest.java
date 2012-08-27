package test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import render.FittingRater;

public class FittingRaterTest {

	private FittingRater rater;
	private BufferedImage target;

	@Before
	public void setUp() throws IOException{
		target = ImageIO.read(new File("babar.png"));
		rater = new FittingRater(target);
	}

	@Test
	public void sameImage() {
		rater.setRender(target);
		assertEquals(rater.getRate(), 0, 1e-10);
	}

}
