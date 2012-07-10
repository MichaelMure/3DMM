package parameter;

public interface Parameter {

	public double get(int index);
	public void set(int index, double value);

	public double getMin(int index);
	public double getMax(int index);
	public String getDescription(int index);

	public void addListener(ParameterListener listener);
}
