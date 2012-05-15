package render;

import util.Log;
import util.Log.LogType;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

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

	private final static int PARAMS_SIZE = OBJECT_SHININESS +1;


	private DenseDoubleMatrix1D matrix = new DenseDoubleMatrix1D(PARAMS_SIZE);

	public RenderParameter() {
		setCameraDistance(3.0);
		setObjectScale(1.0);
		setObjectPosition(new Vector3f());
		setObjectRotation(new Quaternion());
		setAmbientLightColor(ColorRGBA.White);
		setDirectedLightColor(ColorRGBA.White);
		setDirectedLightDirection(new Vector3f(0,1,0));

		float[] offsets = new float[4];
		offsets[0] = 1.0f;
		offsets[1] = 1.0f;
		offsets[2] = 1.0f;
		offsets[3] = 1.0f;
		setColorsOffsets(offsets);

		float[] gains = new float[4];
		gains[0] = 0.0f;
		gains[1] = 0.0f;
		gains[2] = 0.0f;
		gains[3] = 0.0f;
		setColorsGains(gains);

		setObjectShininess(1f);
	}

	public DenseDoubleMatrix1D getMatrix() {
		return matrix;
	}

	public void setMatrix(DenseDoubleMatrix1D matrix) {
		this.matrix = matrix;
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

	public void initObjectScale(Geometry geom) {
		BoundingBox bb = (BoundingBox) geom.getModelBound();
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

	public float[] getColorsOffsets() {
		float[] tab = new float[4];
		tab[0] = (float) matrix.getQuick(COLOR_OFFSET_R);
		tab[1] = (float) matrix.getQuick(COLOR_OFFSET_G);
		tab[2] = (float) matrix.getQuick(COLOR_OFFSET_B);
		tab[3] = 0.0f;
		return tab;
	}

	public void setColorsOffsets(float[] colorsOffsets) {
		if(colorsOffsets.length != 3 && colorsOffsets.length != 4)
			throw new IllegalArgumentException("Unexpected array length for colors offsets.");
		matrix.setQuick(COLOR_OFFSET_R, colorsOffsets[0]);
		matrix.setQuick(COLOR_OFFSET_G, colorsOffsets[1]);
		matrix.setQuick(COLOR_OFFSET_B, colorsOffsets[2]);
	}

	public float[] getColorsGains() {
		float[] tab = new float[4];
		tab[0] = (float) matrix.getQuick(COLOR_GAIN_R);
		tab[1] = (float) matrix.getQuick(COLOR_GAIN_G);
		tab[2] = (float) matrix.getQuick(COLOR_GAIN_B);
		tab[3] = 1.0f;
		return tab;
	}

	public void setColorsGains(float[] colorsGains) {
		if(colorsGains.length != 3 && colorsGains.length != 4)
			throw new IllegalArgumentException("Unexpected array length for colors gains.");
		matrix.setQuick(COLOR_GAIN_R, colorsGains[0]);
		matrix.setQuick(COLOR_GAIN_G, colorsGains[1]);
		matrix.setQuick(COLOR_GAIN_B, colorsGains[2]);
	}

	public float getObjectShininess() {
		return (float) matrix.getQuick(OBJECT_SHININESS);
	}

	public void setObjectShininess(float objectShininess) {
		matrix.setQuick(OBJECT_SHININESS, objectShininess);
	}
}
