package render;

import util.Log;
import util.Log.LogType;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;

public class RenderParameter {

	private final static int CAMERA_DISTANCE = 0;

	private final static int OBJECT_SCALE = 1;

	private final static int OBJECT_POSITION_X = 2;
	private final static int OBJECT_POSITION_Y = 3;

	private final static int OBJECT_ROTATION_X = 4;
	private final static int OBJECT_ROTATION_Y = 5;
	private final static int OBJECT_ROTATION_Z = 6;
	private final static int OBJECT_ROTATION_W = 7;

	private final static int AMBIENT_COLOR_R = 8;
	private final static int AMBIENT_COLOR_G = 9;
	private final static int AMBIENT_COLOR_B = 10;

	private final static int DIRECTED_LIGHT_COLOR_R = 11;
	private final static int DIRECTED_LIGHT_COLOR_G = 12;
	private final static int DIRECTED_LIGHT_COLOR_B = 13;

	private final static int DIRECTED_LIGHT_DIRECTION_X = 14;
	private final static int DIRECTED_LIGHT_DIRECTION_Y = 15;
	private final static int DIRECTED_LIGHT_DIRECTION_Z = 16;

	private final static int COLOR_OFFSET_R = 17;
	private final static int COLOR_OFFSET_G = 18;
	private final static int COLOR_OFFSET_B = 19;

	private final static int COLOR_GAIN_R = 20;
	private final static int COLOR_GAIN_G = 21;
	private final static int COLOR_GAIN_B = 22;

	private final static int OBJECT_SHININESS = 23;

	public final static int LAST_PARAM = OBJECT_SHININESS;
	private final static int PARAMS_SIZE = OBJECT_SHININESS +1;


	private DenseDoubleMatrix1D matrix = new DenseDoubleMatrix1D(PARAMS_SIZE);

	public RenderParameter() {
		setCameraDistance(3.0);
		setObjectScale(1.0);
		setObjectPosition(new Vector3f());
		setObjectRotation(new Quaternion());
		setAmbientLightColor(ColorRGBA.White);
		setDirectedLightColor(ColorRGBA.White);
		setDirectedLightDirection(new Vector3f(-0.5f,-1,-1));
		setColorsOffsets(new ColorRGBA(0f, 0f, 0f, 0f));
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
	}

	public double get(int index) {
		return matrix.getQuick(index);
	}

	public void set(int index, double value) {
		matrix.setQuick(index, value);
	}

	@Override
	public String toString() {
		String result = "Render Parameters: \n";
		result += "Camera distance          : " + getCameraDistance() + "\n";
		result += "Object scale             : " + getObjectScale() + "\n";
		result += "Object position          : " + getObjectPosition() + "\n";
		result += "Object rotation          : " + getObjectRotation() + "\n";
		result += "Ambient color            : " + getAmbientLightColor() + "\n";
		result += "Directed light color     : " + getDirectedLightColor() + "\n";
		result += "Directed light direction : " + getDirectedLightDirection() + "\n";
		result += "Color offset             : " + getColorsOffsets() + "\n";
		result += "Color gain               : " + getColorsGains() + "\n";
		result += "Object shininess         : " + getObjectShininess() + "\n";

		return result;
	}

	public void copy(RenderParameter params) {
		matrix.assign(params.matrix);
	}

	/** Multiply the specified parameter by a ratio. */
	public void scaleParam(int index, double ratio) {
		if(index < 0 || index > LAST_PARAM)
			throw new IllegalArgumentException("Unexpected index");

		matrix.setQuick(index, matrix.getQuick(index) * ratio);
	}

	public float getCameraDistance() {
		return (float) matrix.getQuick(CAMERA_DISTANCE);
	}

	public void setCameraDistance(double cameraDistance) {
		matrix.setQuick(CAMERA_DISTANCE, cameraDistance);
	}

	public float getObjectScale() {
		return (float) matrix.getQuick(OBJECT_SCALE);
	}

	public void setObjectScale(double objectScale) {
		matrix.setQuick(OBJECT_SCALE, objectScale);
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
	}

	public Vector3f getObjectPosition() {
		return new Vector3f((float) matrix.getQuick(OBJECT_POSITION_X),
				(float) matrix.getQuick(OBJECT_POSITION_Y),
				0.0f);
	}

	public void setObjectPosition(Vector3f objectPosition) {
		matrix.setQuick(OBJECT_POSITION_X, objectPosition.x);
		matrix.setQuick(OBJECT_POSITION_Y, objectPosition.y);
	}

	public Quaternion getObjectRotation() {
		return new Quaternion((float) matrix.getQuick(OBJECT_ROTATION_X),
													(float) matrix.getQuick(OBJECT_ROTATION_Y),
													(float) matrix.getQuick(OBJECT_ROTATION_Z),
													(float) matrix.getQuick(OBJECT_ROTATION_W));
	}

	public void setObjectRotation(Quaternion objectRotation) {
		matrix.setQuick(OBJECT_ROTATION_X, objectRotation.getX());
		matrix.setQuick(OBJECT_ROTATION_Y, objectRotation.getY());
		matrix.setQuick(OBJECT_ROTATION_Z, objectRotation.getZ());
		matrix.setQuick(OBJECT_ROTATION_W, objectRotation.getW());
	}

	public ColorRGBA getAmbientLightColor() {
		return new ColorRGBA((float) matrix.getQuick(AMBIENT_COLOR_R),
												 (float) matrix.getQuick(AMBIENT_COLOR_G),
												 (float) matrix.getQuick(AMBIENT_COLOR_B),
												 1.0f);
	}

	public void setAmbientLightColor(ColorRGBA ambientLightColor) {
		matrix.setQuick(AMBIENT_COLOR_R, ambientLightColor.r);
		matrix.setQuick(AMBIENT_COLOR_G, ambientLightColor.g);
		matrix.setQuick(AMBIENT_COLOR_B, ambientLightColor.b);
	}

	public ColorRGBA getDirectedLightColor() {
		return new ColorRGBA((float) matrix.getQuick(DIRECTED_LIGHT_COLOR_R),
												 (float) matrix.getQuick(DIRECTED_LIGHT_COLOR_G),
												 (float) matrix.getQuick(DIRECTED_LIGHT_COLOR_B),
												 1.0f);
	}

	public void setDirectedLightColor(ColorRGBA directedLightColor) {
		matrix.setQuick(DIRECTED_LIGHT_COLOR_R, directedLightColor.r);
		matrix.setQuick(DIRECTED_LIGHT_COLOR_G, directedLightColor.g);
		matrix.setQuick(DIRECTED_LIGHT_COLOR_B, directedLightColor.b);
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
	}

	public ColorRGBA getColorsOffsets() {
		return new ColorRGBA((float) matrix.getQuick(COLOR_OFFSET_R),
												 (float) matrix.getQuick(COLOR_OFFSET_G),
												 (float) matrix.getQuick(COLOR_OFFSET_B),
												 0.0f);
	}

	public void setColorsOffsets(ColorRGBA offsets) {
		matrix.setQuick(COLOR_OFFSET_R, offsets.r);
		matrix.setQuick(COLOR_OFFSET_G, offsets.g);
		matrix.setQuick(COLOR_OFFSET_B, offsets.b);
	}

	public ColorRGBA getColorsGains() {
		return new ColorRGBA((float) matrix.getQuick(COLOR_GAIN_R),
												 (float) matrix.getQuick(COLOR_GAIN_G),
												 (float) matrix.getQuick(COLOR_GAIN_B),
												 1.0f);
	}

	public void setColorsGains(ColorRGBA gains) {
		matrix.setQuick(COLOR_GAIN_R, gains.r);
		matrix.setQuick(COLOR_GAIN_G, gains.g);
		matrix.setQuick(COLOR_GAIN_B, gains.b);
	}

	public float getObjectShininess() {
		return (float) matrix.getQuick(OBJECT_SHININESS);
	}

	public void setObjectShininess(float objectShininess) {
		matrix.setQuick(OBJECT_SHININESS, objectShininess);
	}
}
