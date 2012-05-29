package render;

import java.awt.image.BufferedImage;

public class FittingRater {
	private BufferedImage target;
	private BufferedImage render;
	private boolean dirty;

	private double rate;
	private long nb_pixels;
	private double ratio;

	public FittingRater(BufferedImage target) {
		this.target = target;
	}

	public void setRender(BufferedImage render) {
		if(target.getWidth() != render.getWidth() || target.getHeight() != render.getHeight())
			throw new IllegalArgumentException("Image don't have the same size.");
		this.render = render;
		this.dirty = true;
	}

	public double getRate() {
		ensureResult();
		return rate;
	}

	public long getNbPixels() {
		ensureResult();
		return nb_pixels;
	}

	public double getRatio() {
		ensureResult();
		return ratio;
	}

	private void ensureResult() {
		if(render == null)
			throw new IllegalStateException("No render to rate !");
		if(dirty == false)
			return;

		nb_pixels = 0;

		long total_error = 0;
		int pixel, alpha, red, green, blue;
		int pixel_ref, red_ref, green_ref, blue_ref;
		int red_diff, green_diff, blue_diff;

		for(int x = 0; x < render.getWidth(); x++)
			for(int y = 0; y < render.getHeight(); y++) {
				pixel = render.getRGB(x, y);
				alpha = (pixel >> 24) & 0xff;
				if(alpha > 110) {
					red   = (pixel >> 16) & 0xff;
			    green = (pixel >> 8) & 0xff;
			    blue  = (pixel) & 0xff;

			    pixel_ref = target.getRGB(x, y);
			    red_ref   = (pixel_ref >> 16) & 0xff;
			    green_ref = (pixel_ref >> 8) & 0xff;
			    blue_ref  = (pixel_ref) & 0xff;

			    red_diff = red - red_ref;
			    green_diff = green - green_ref;
			    blue_diff = blue - blue_ref;

			    total_error += Math.sqrt(red_diff * red_diff + green_diff * green_diff + blue_diff * blue_diff);

			    nb_pixels++;
				}
			}

		ratio = (double) nb_pixels / (double) (render.getWidth() * render.getHeight());
		rate = ratio * ((double) total_error / (double)  nb_pixels);
		//rate = total_error;

		dirty = false;
	}
}
