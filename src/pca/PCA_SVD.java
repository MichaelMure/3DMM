package pca;

import org.ejml.alg.dense.decomposition.DecompositionFactory;
import org.ejml.alg.dense.decomposition.SingularValueDecomposition;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.SingularOps;

public class PCA_SVD extends PCA {

	public PCA_SVD(DenseMatrix64F data) {
		super(data);
	}

	@Override
	protected void doComputeBasis() {
		/* Compute SVD and save time by not computing U */
		SingularValueDecomposition<DenseMatrix64F> svd = DecompositionFactory.svd(
				data.numRows, data.numCols, false, true, false);
		if (!svd.decompose(data))
			throw new RuntimeException("SVD failed");

		basis = svd.getV(true);
		DenseMatrix64F W = svd.getW(null);

		/* Singular values are in an arbitrary order initially. */
		SingularOps.descendingOrder(null, false, W, basis, true);

		/* strip off unneeded components and find the basis. */
		basis.reshape(numComponents, data.numCols, true);
	}

}
