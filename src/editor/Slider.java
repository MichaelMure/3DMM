package editor;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Slider extends JPanel {

	private static final long serialVersionUID = -4409767652756651033L;
	private JSlider slider;
	private JLabel description;
	private JLabel valueLabel;
	private double scale;
	private double min;


	public Slider(String label, double min, double max, double resolution, double value) {
		this.description = new JLabel(label);

		this.min = min;
		double extent = max - min;
		int increment = (int) (extent / resolution);
		scale = increment / extent;

		this.slider = new JSlider(0, increment, 0);
		slider.addChangeListener(new SliderListener());
		this.add(this.description);
		this.add(slider);

		this.valueLabel = new JLabel();
		this.add(this.valueLabel);

		setValue(value);
	}

	private double getValue() {
		return slider.getValue() / scale + min;
	}

	private void setValue(double value) {
		slider.setValue((int) ((value - min) * scale));
	}

	class SliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			//valueLabel.setText(new DecimalFormat("#.##").format(getValue()));
			valueLabel.setText(String.format("%+.2f",getValue()));
		}
	}
}
