package render;

import java.awt.image.BufferedImage;

public class FittingRater {
	private BufferedImage target;
	private BufferedImage render;
	private float[][][] targetLinear;
	private boolean dirty;

	private double rate;
	private long nb_pixels;
	private double ratio;

	public FittingRater(BufferedImage target) {
		this.target = target;
		this.targetLinear = new float[target.getWidth()][target.getHeight()][3];

		int pixel;
		for (int x = 0; x < target.getWidth(); x++) {
			for (int y = 0; y < target.getHeight(); y++) {
				pixel = target.getRGB(x, y);
				targetLinear[x][y][0] = sRGB2RGB(((pixel >> 16) & 0xff) / 255f);
				targetLinear[x][y][1] = sRGB2RGB(((pixel >> 8) & 0xff) / 255f);
				targetLinear[x][y][2] = sRGB2RGB(((pixel) & 0xff) / 255f);
			}
		}
	}

	public void setRender(BufferedImage render) {
		if (target.getWidth() != render.getWidth()
				|| target.getHeight() != render.getHeight())
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
		if (render == null)
			throw new IllegalStateException("No render to rate !");
		if (dirty == false)
			return;

		nb_pixels = 0;

		double total_error = 0;
		int pixel, alpha;
		float red, green, blue;
		float red_diff, green_diff, blue_diff;

		for (int x = 0; x < render.getWidth(); x++)
			for (int y = 0; y < render.getHeight(); y++) {
				pixel = render.getRGB(x, y);
				alpha = (pixel >> 24) & 0xff;
				if (alpha > 110) {
					red = sRGB2RGB(((pixel >> 16) & 0xff) / 255f);
					green = sRGB2RGB(((pixel >> 8) & 0xff) / 255f);
					blue = sRGB2RGB(((pixel) & 0xff) / 255f);

					red_diff = red - targetLinear[x][y][0];
					green_diff = green - targetLinear[x][y][1];
					blue_diff = blue - targetLinear[x][y][2];

					total_error += red_diff * red_diff + green_diff * green_diff
							+ blue_diff * blue_diff;

					nb_pixels++;
				}
			}

		ratio = (double) nb_pixels / (double) (render.getWidth() * render.getHeight());
		// rate = ratio * ((double) total_error / (double) nb_pixels);
		rate = 255 * total_error;

		dirty = false;
	}

	public float sRGB2RGB(float in) {
		float out;

		if (in < 0)
			in = 0f;
		if (in > 1)
			in = 1f;
		if (in <= 0.03928f)
			out = (float) (in / 12.92);
		else
			out = (float) (Math.exp(2.4 * Math.log((in + 0.055) / 1.055)));

		return out;
	}
}
