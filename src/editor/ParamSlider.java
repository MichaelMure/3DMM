package editor;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import parameter.ParameterListener;
import parameter.Parameter;

public class ParamSlider extends JPanel {

	private static final long serialVersionUID = -4409767652756651033L;

	private Parameter param;
	private int index;

	private JSlider slider;
	private JLabel description;
	private JLabel valueLabel;
	private double scale;
	private double min;


	public ParamSlider(Parameter param, int index) {
		this.param = param;
		this.index = index;

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.description = new JLabel(param.getDescription(index));

		this.min = param.getMin(index);
		double extent = param.getMax(index) - min;
		double resolution = extent / 10000;
		int increment = (int) (extent / resolution);
		scale = increment / extent;

		this.slider = new JSlider(0, increment, 0);
		slider.addChangeListener(new SliderListener());
		this.add(this.description);
		this.add(slider);

		this.valueLabel = new JLabel();
		this.add(this.valueLabel);

		setValue(param.get(index));
		valueLabel.setText(String.format("%+.2f",getValue()));

		param.addListener(new ModelListener());
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
			valueLabel.setText(String.format("%+.2f",getValue()));
			param.set(index, getValue());
		}
	}

	class ModelListener implements ParameterListener {
		@Override
		public void modelChanged() {
			setValue(param.get(index));
		}
	}
}
