package editor;

import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class Slider extends JPanel {

	private JSlider slider;
	private JLabel label;
	private double scale;
	private double min;


	public Slider(String label, double min, double max, double resolution, double value) {
		this.label = new JLabel(label);

		this.min = min;
		double extent = max - min;
		int increment = (int) (extent / resolution);
		scale = increment / extent;

		this.slider = new JSlider(0, increment, 0);
		setValue(value);
		this.add(this.label);
		this.add(slider);
	}

	private double getValue() {
		return slider.getValue() * scale + min;
	}

	private void setValue(double value) {
		slider.setValue((int) ((value - min) / scale));
	}
}
