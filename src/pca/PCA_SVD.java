package pca;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.doublealgo.Statistic;
import cern.colt.matrix.linalg.EigenvalueDecomposition;

public class PCA_SVD extends PCA {

	public PCA_SVD() {
	}

	public PCA_SVD(DoubleMatrix2D data) {
		super(data);
	}

	@Override
	protected void doComputeBasis() {
		DoubleMatrix2D variance = Statistic.covariance(data);
		variance = Statistic.correlation(variance);

		EigenvalueDecomposition eg = new EigenvalueDecomposition(variance);
		basis = eg.getV().viewColumnFlip().viewPart(0, 0, numComponents, data.columns()).copy();
		eigenValues = eg.getRealEigenvalues().viewFlip().viewPart(0, numComponents).copy();
	}

}
