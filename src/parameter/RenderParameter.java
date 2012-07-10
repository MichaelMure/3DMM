package parameter;

import javax.swing.event.EventListenerList;

import util.Log;
import util.Log.LogType;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;

public class RenderParameter implements Parameter {

	public final static int CAMERA_DISTANCE = 0;

	public final static int OBJECT_SCALE = 1;

	public final static int OBJECT_POSITION_X = 2;
	public final static int OBJECT_POSITION_Y = 3;

	public final static int OBJECT_ROTATION_X = 4;
	public final static int OBJECT_ROTATION_Y = 5;
	public final static int OBJECT_ROTATION_Z = 6;

	public final static int AMBIENT_COLOR_R = 7;
	public final static int AMBIENT_COLOR_G = 8;
	public final static int AMBIENT_COLOR_B = 9;

	public final static int DIRECTED_LIGHT_COLOR_R = 10;
	public final static int DIRECTED_LIGHT_COLOR_G = 11;
	public final static int DIRECTED_LIGHT_COLOR_B = 12;

	public final static int DIRECTED_LIGHT_DIRECTION_X = 13;
	public final static int DIRECTED_LIGHT_DIRECTION_Y = 14;
	public final static int DIRECTED_LIGHT_DIRECTION_Z = 15;

	public final static int COLOR_OFFSET_R = 16;
	public final static int COLOR_OFFSET_G = 17;
	public final static int COLOR_OFFSET_B = 18;

	public final static int COLOR_GAIN_R = 19;
	public final static int COLOR_GAIN_G = 20;
	public final static int COLOR_GAIN_B = 21;

	public final static int OBJECT_SHININESS = 22;

	public final static int LAST_PARAM = OBJECT_SHININESS;
	public final static int PARAMS_SIZE = LAST_PARAM +1;

	private final static float EPSILON = 0.0001f;

	private DenseDoubleMatrix1D matrix = new DenseDoubleMatrix1D(PARAMS_SIZE);

	private final EventListenerList listeners = new EventListenerList();

	public RenderParameter() {
		setCameraDistance(3.0);
		setObjectScale(1.0);
		setObjectPosition(new Vector3f(EPSILON, EPSILON, EPSILON));
		setObjectRotation(Quaternion.DIRECTION_Z);
		setAmbientLightColor(ColorRGBA.White);
		setDirectedLightColor(ColorRGBA.White);
		setDirectedLightDirection(new Vector3f(-0.5f,-1,-1));
		setColorsOffsets(new ColorRGBA(EPSILON, EPSILON, EPSILON, EPSILON));
		setColorsGains(new ColorRGBA(1f, 1f, 1f, 1f));
		setObjectShininess(80f);
	}

	public RenderParameter(RenderParameter param) {
		this.matrix = new DenseDoubleMatrix1D(PARAMS_SIZE);
		this.matrix.assign(param.matrix);
	}

	public DenseDoubleMatrix1D getMatrix() {
		return matrix;
	}

	public void setMatrix(DenseDoubleMatrix1D matrix) {
		this.matrix = matrix;
		fireParamChanged();
	}

	@Override
	public double get(int index) {
		return matrix.getQuick(index);
	}

	@Override
	public void set(int index, double value) {
		matrix.setQuick(index, value);
		fireParamChanged();
	}

	@Override
	public String toString() {
		String result = "Render Parameters: \n";
		result += "Camera distance          : " + getCameraDistance() + "\n";
		result += "Object scale             : " + getObjectScale() + "\n";
		result += "Object position          : " + getObjectPosition() + "\n";
		result += "Object rotation          : (" + matrix.getQuick(OBJECT_ROTATION_X)
				+ ", " + matrix.getQuick(OBJECT_ROTATION_Y) + ", " + matrix.getQuick(OBJECT_ROTATION_Z) + ")\n";
		result += "Ambient color            : " + getAmbientLightColor() + "\n";
		result += "Directed light color     : " + getDirectedLightColor() + "\n";
		result += "Directed light direction : " + getDirectedLightDirection() + "\n";
		result += "Color offset             : " + getColorsOffsets() + "\n";
		result += "Color gain               : " + getColorsGains() + "\n";
		result += "Object shininess         : " + getObjectShininess() + "\n";

		return result;
	}

	public String getDataString() {
		String result = "";
		for(int x = 0; x < matrix.size() -1; x++)
			result += String.format("%f\t", matrix.getQuick(x));
		return result + String.format("%f", matrix.getQuick(matrix.size()-1));
	}

	public void copy(RenderParameter params) {
		matrix.assign(params.matrix);
	}

	public static double getStandartDeviation(int index) {
		switch (index) {
		case OBJECT_SCALE: return 1.0/300.0;
		case CAMERA_DISTANCE: return 1;
		case OBJECT_SHININESS: return 0.2;
		case OBJECT_POSITION_X:
		case OBJECT_POSITION_Y: return 0.05;
		case DIRECTED_LIGHT_DIRECTION_X:
		case DIRECTED_LIGHT_DIRECTION_Y:
		case DIRECTED_LIGHT_DIRECTION_Z: return 0.1;

		default: return 1.0;
		}
	}

	public float getCameraDistance() {
		return (float) matrix.getQuick(CAMERA_DISTANCE);
	}

	public void setCameraDistance(double cameraDistance) {
		matrix.setQuick(CAMERA_DISTANCE, cameraDistance);
		fireParamChanged();
	}

	public float getObjectScale() {
		return (float) matrix.getQuick(OBJECT_SCALE);
	}

	public void setObjectScale(double objectScale) {
		matrix.setQuick(OBJECT_SCALE, objectScale);
		fireParamChanged();
	}

	public void initObjectScale(Mesh mesh) {
		BoundingBox bb = (BoundingBox) mesh.getBound();
		Vector3f extent = bb.getExtent(null);

		if(extent.length() > 1.0) {
			float scale = 1f / extent.length();
			Log.info(LogType.GUI, "Auto-scaling: "+ scale);

			setObjectScale(scale);
			return;
		}
		Log.info(LogType.GUI, "Not auto-scaling");
		fireParamChanged();
	}

	public Vector3f getObjectPosition() {
		return new Vector3f((float) matrix.getQuick(OBJECT_POSITION_X),
				(float) matrix.getQuick(OBJECT_POSITION_Y),
				0.0f);
	}

	public void setObjectPosition(Vector3f objectPosition) {
		matrix.setQuick(OBJECT_POSITION_X, objectPosition.x);
		matrix.setQuick(OBJECT_POSITION_Y, objectPosition.y);
		fireParamChanged();
	}

	public Quaternion getObjectRotation() {
		return new Quaternion().fromAngles(OBJECT_ROTATION_X, OBJECT_ROTATION_Y, OBJECT_ROTATION_Z);
	}

	public void setObjectRotation(Quaternion objectRotation) {
		float[] angles = new float[3];
		objectRotation.toAngles(angles);
		matrix.setQuick(OBJECT_ROTATION_X, angles[0]);
		matrix.setQuick(OBJECT_ROTATION_Y, angles[1]);
		matrix.setQuick(OBJECT_ROTATION_Z, angles[2]);
		fireParamChanged();
	}

	public ColorRGBA getAmbientLightColor() {
		ColorRGBA c = new ColorRGBA();
		c.r = (float) matrix.getQuick(AMBIENT_COLOR_R);
		c.g = (float) matrix.getQuick(AMBIENT_COLOR_G);
		c.b = (float) matrix.getQuick(AMBIENT_COLOR_B);
		c.a = 1.0f;
		return c;
	}

	public void setAmbientLightColor(ColorRGBA ambientLightColor) {
		matrix.setQuick(AMBIENT_COLOR_R, ambientLightColor.r);
		matrix.setQuick(AMBIENT_COLOR_G, ambientLightColor.g);
		matrix.setQuick(AMBIENT_COLOR_B, ambientLightColor.b);
		fireParamChanged();
	}

	public ColorRGBA getDirectedLightColor() {
		ColorRGBA c = new ColorRGBA();
		c.r = (float) matrix.getQuick(DIRECTED_LIGHT_COLOR_R);
		c.g = (float) matrix.getQuick(DIRECTED_LIGHT_COLOR_G);
		c.b = (float) matrix.getQuick(DIRECTED_LIGHT_COLOR_B);
		c.a = 1.0f;
		return c;
	}

	public void setDirectedLightColor(ColorRGBA directedLightColor) {
		matrix.setQuick(DIRECTED_LIGHT_COLOR_R, directedLightColor.r);
		matrix.setQuick(DIRECTED_LIGHT_COLOR_G, directedLightColor.g);
		matrix.setQuick(DIRECTED_LIGHT_COLOR_B, directedLightColor.b);
		fireParamChanged();
	}

	public Vector3f getDirectedLightDirection() {
		return new Vector3f((float) matrix.getQuick(DIRECTED_LIGHT_DIRECTION_X),
												(float) matrix.getQuick(DIRECTED_LIGHT_DIRECTION_Y),
												(float) matrix.getQuick(DIRECTED_LIGHT_DIRECTION_Z));
	}

	public void setDirectedLightDirection(Vector3f directedLightDirection) {
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_X, directedLightDirection.x);
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_Y, directedLightDirection.y);
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_Z, directedLightDirection.z);
		fireParamChanged();
	}

	public ColorRGBA getColorsOffsets() {
		ColorRGBA c = new ColorRGBA();
		c.r = (float) matrix.getQuick(COLOR_OFFSET_R);
		c.g = (float) matrix.getQuick(COLOR_OFFSET_G);
		c.b = (float) matrix.getQuick(COLOR_OFFSET_B);
		c.a = 0.0f;
		return c;
	}

	public void setColorsOffsets(ColorRGBA offsets) {
		matrix.setQuick(COLOR_OFFSET_R, offsets.r);
		matrix.setQuick(COLOR_OFFSET_G, offsets.g);
		matrix.setQuick(COLOR_OFFSET_B, offsets.b);
		fireParamChanged();
	}

	public ColorRGBA getColorsGains() {
		ColorRGBA c = new ColorRGBA();
		c.r = (float) matrix.getQuick(COLOR_GAIN_R);
		c.g = (float) matrix.getQuick(COLOR_GAIN_G);
		c.b = (float) matrix.getQuick(COLOR_GAIN_B);
		c.a = 1.0f;
		return c;
	}

	public void setColorsGains(ColorRGBA gains) {
		matrix.setQuick(COLOR_GAIN_R, gains.r);
		matrix.setQuick(COLOR_GAIN_G, gains.g);
		matrix.setQuick(COLOR_GAIN_B, gains.b);
		fireParamChanged();
	}

	public float getObjectShininess() {
		return (float) matrix.getQuick(OBJECT_SHININESS);
	}

	public void setObjectShininess(float objectShininess) {
		matrix.setQuick(OBJECT_SHININESS, objectShininess);
		fireParamChanged();
	}

	@Override
	public String getDescription(int index) {
		switch(index) {
		case CAMERA_DISTANCE: return "Camera distance";
		case OBJECT_SCALE: return "Object scale";
		case OBJECT_POSITION_X: return "Object position X";
		case OBJECT_POSITION_Y: return "Object position Y";
		case OBJECT_ROTATION_X: return "Object rotation X";
		case OBJECT_ROTATION_Y: return "Object rotation Y";
		case OBJECT_ROTATION_Z: return "Object rotation Z";
		case AMBIENT_COLOR_R: return "Ambient color red";
		case AMBIENT_COLOR_G: return "Ambient color green";
		case AMBIENT_COLOR_B: return "Ambient color blue";
		case DIRECTED_LIGHT_COLOR_R: return "Directed light color red";
		case DIRECTED_LIGHT_COLOR_G: return "Directed light color green";
		case DIRECTED_LIGHT_COLOR_B: return "Directed light color blue";
		case DIRECTED_LIGHT_DIRECTION_X: return "Directed light rotation X";
		case DIRECTED_LIGHT_DIRECTION_Y: return "Directed light rotation Y";
		case DIRECTED_LIGHT_DIRECTION_Z: return "Directed light rotation Z";
		case COLOR_GAIN_R: return "Color gain red";
		case COLOR_GAIN_G: return "Color gain green";
		case COLOR_GAIN_B: return "Color gain blue";
		case COLOR_OFFSET_R: return "Color offset red";
		case COLOR_OFFSET_G: return "Color offset green";
		case COLOR_OFFSET_B: return "Color offset blue";
		case OBJECT_SHININESS: return "Object shininess";
		}
		assert(false);
		return "";
	}

	@Override
	public double getMin(int index) {
		switch(index) {
		case CAMERA_DISTANCE: return 2;
		case OBJECT_POSITION_X:
		case OBJECT_POSITION_Y: return -1;
		case OBJECT_ROTATION_X:
		case OBJECT_ROTATION_Y:
		case OBJECT_ROTATION_Z: return 0; /* TODO */
		case OBJECT_SCALE: return getObjectScale() / Math.sqrt(10);
		case OBJECT_SHININESS: return 1;
		}
		return 0;
	}

	@Override
	public double getMax(int index) {
		switch (index) {
		case CAMERA_DISTANCE: return 10;
		case OBJECT_SCALE: return getObjectScale() * Math.sqrt(10);
		case OBJECT_ROTATION_X:
		case OBJECT_ROTATION_Y:
		case OBJECT_ROTATION_Z: return 1; /* TODO */
		case AMBIENT_COLOR_R:
		case AMBIENT_COLOR_G:
		case AMBIENT_COLOR_B:
		case DIRECTED_LIGHT_COLOR_R:
		case DIRECTED_LIGHT_COLOR_G:
		case DIRECTED_LIGHT_COLOR_B: return 5;
		case COLOR_GAIN_R:
		case COLOR_GAIN_G:
		case COLOR_GAIN_B: return 2;
		case OBJECT_SHININESS: return 128;
		}
		return 1;
	}

	@Override
	public void addListener(ParameterListener listener) {
		this.listeners.add(ParameterListener.class, listener);
	}

	protected void fireParamChanged() {
		for(ParameterListener l : listeners.getListeners(ParameterListener.class)) {
			l.modelChanged();
		}
	}
}
