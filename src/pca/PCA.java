package pca;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import util.Log;
import util.Log.LogType;
import util.TimeCounter;

public class PCA {
	protected int numComponents; /* Dimension of reduced data. */
	protected DoubleMatrix2D data = null; /* Original data */
	private DoubleMatrix2D reducedData = null; /* Data after PCA */
	protected DoubleMatrix2D basis = null; /* Transformation matrix between sample and feature space. */
	protected DoubleMatrix1D eigenValues = null; /* Eigen Values used for standard deviation */

	private DoubleMatrix1D mean = null;
	private boolean meanDirty = true;

	private boolean dataLock = false; /* If the data already has been centered. */

	public PCA() {
	}

	public PCA(int numComponents,
			DoubleMatrix2D reducedData,
			DoubleMatrix1D eigenValues,
			DoubleMatrix1D mean) {
		this.numComponents = numComponents;
		this.reducedData = reducedData;
		this.eigenValues = eigenValues;
		this.mean = mean;
		this.meanDirty = false;
		this.dataLock = true;
	}

	/** Create a new PCA with the provided data, with one column = one sample. */
	public PCA(DoubleMatrix2D data) {
		this.data = data;
	}

	/** Add a new sample in the PCA. The first sample added decide the sample's size.
	 *  If further add is done with a different size, an exception is throw.
	 */
	public void addSample(DoubleMatrix1D sample) {
		if(dataLock)
			throw new RuntimeException("Data already locked.");

		if(data == null) {
			/* Sub-optimal, involve 2 copies. */
			data = DoubleFactory2D.dense.make(sample.toArray(), sample.size());
		}
		else {
			if(data.rows() != sample.size())
				throw new RuntimeException("Unexpected sample length.");

			/* Sub-optimal, involve 3 copies. */
			DoubleMatrix2D sample2d = DoubleFactory2D.dense.make(sample.toArray(), sample.size());
			data = DoubleFactory2D.dense.appendColumns(data, sample2d).copy();
		}
	}

	/** Compute the PCA and reduce the data. */
	public void computePCA(int numComponents) {
		TimeCounter t = new TimeCounter("PCA: computation of basis and reduced data.");
		this.numComponents = numComponents;
		Log.info(LogType.MODEL, "PCA computation with " + numComponents + " dimensions.");
		centerData();
		doComputeBasis();

		/* Compute reduced data. */
		reducedData = new Algebra().mult(data, basis.viewDice());
		t.stop();
	}

	public boolean computationDone() {
		return dataLock;
	}

	/** This method should compute the basis matrix and the eigenValue matrix. */
	protected void doComputeBasis() {
		/* This class need to be subclassed to implement a PCA method. */
		assert(false);
	}

	/** @return the basis vector. */
	public DoubleMatrix2D getBasis() {
		assert basis != null;
		return basis;
	}

	/** @return a vector of the feature space basis. */
	public DoubleMatrix1D getBasisVector(int index) {
		assert basis != null;
		return basis.viewColumn(index);
	}

	/** @return one eigen value. */
	public double getEigenValue(int index) {
		ensureReducedData();
		return eigenValues.get(index);
	}

	/** @return the number of component of the feature space. */
	public int getNumComponents() {
		ensureReducedData();
		return numComponents;
	}

	/** @return the size of one sample. */
	public int getSampleSize() {
		if(data == null)
			return 0;
		return data.rows();
	}

	/** @return how many sample are stored. */
	public int getSampleNumber() {
		if(data == null)
			return 0;
		return data.columns();
	}

	/** @return one original sample. */
	public DoubleMatrix1D getSample(int index) {
		return data.viewColumn(index);
	}

	/** @return one of the reduced model. */
	public DoubleMatrix1D getFeatureSample(int index) {
		return reducedData.viewColumn(index).copy().assign(mean, Functions.plus);
	}

	/** @return a sample from the original data expressed in the feature space.
	 *  @param index the index of the sample in the original data.
	 */
	public DoubleMatrix1D sampleToFeatureSpace(int index) {
		return sampleToFeatureSpaceNoMean(data.viewColumn(index));
	}

	/** @return an arbitrary sample expressed in the feature space.
	 *  @param sample the sample data. Length should be the same as the original data.
	 */
	public DoubleMatrix1D sampleToFeatureSpace(DoubleMatrix1D sample) {
		ensureMean();

		return sampleToFeatureSpaceNoMean(sample.copy().assign(mean, Functions.minus));
	}

	/** Internal sampleToFeatureSpace, for data that are already mean subtracted.
	 *  @param sample the sample to convert.
	 */
	private DoubleMatrix1D sampleToFeatureSpaceNoMean(DoubleMatrix1D sample) {
		ensureReducedData();

		if(sample.size() != data.rows())
			throw new IllegalArgumentException("Unexpected sample length.");

		return new Algebra().mult(basis, sample);
	}

	/** @return an arbitrary sample expressed in the sample space.
	 *  @param sample the sample data. Length should be the same as the feature space dimension.
	 */
	public DoubleMatrix1D sampleToSampleSpace(DoubleMatrix1D sample) {
		if(sample.size() != numComponents)
			throw new IllegalArgumentException("Unexpected sample length.");

		ensureMean();

		DoubleMatrix1D s_copy = sample.copy();

		s_copy.assign(eigenValues, Functions.mult);

		DoubleMatrix1D result = new Algebra().mult(reducedData, s_copy);

		return result.assign(mean, Functions.plus);
	}

	/** Compute the error resulting for a projection to feature space and back for a sample.
	 *  This could be used to test the membership of a sample to the feature space.
	 */
	public double errorMembership(DoubleMatrix1D sample) {
		DoubleMatrix1D feat = sampleToFeatureSpace(sample);
		DoubleMatrix1D back = sampleToSampleSpace(feat);

		sample.assign(back, Functions.minus);
		sample.assign(Functions.square);

		return Math.sqrt(sample.zSum());
	}

	/** @return the mean sample. */
	public DoubleMatrix1D getMean() {
		ensureMean();
		return mean;
	}

	@Override
	public String toString() {
		return "PCA: \n" + basis;
	}

	/** Update the mean sample of the original data. */
	private void computeMean() {
		Log.debug(LogType.MODEL, "PCA: compute mean sample.");

		if(mean == null)
			mean = new DenseDoubleMatrix1D(data.rows());
		else
			mean.assign(0.0);

		for(int i = 0; i < data.rows(); i++) {
			mean.set(i, data.viewRow(i).zSum() / data.columns());
		}

		meanDirty = false;
	}

	/** Subtract the mean from all samples. */
	private void centerData() {
		Log.debug(LogType.MODEL, "PCA: lock data.");
		this.dataLock = true;
		ensureMean();

		for(int i = 0; i < data.columns(); i++)
			data.viewColumn(i).assign(mean, Functions.minus);
	}

	/** If no explicit computeBasis call have been made with a numComponents,
	 *  we compute the PCA with the same dimension as the original data. */
	private void ensureReducedData() {
		if(reducedData == null)
			computePCA(data.rows());
	}

	/** Ensure that the mean is properly computed. */
	private void ensureMean() {
		if(meanDirty)
			computeMean();
	}
}
