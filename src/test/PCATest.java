package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

import pca.PCA;
import pca.PCA_SVD;


public class PCATest {

	private PCA pca;
	private double[] d1 = {0,1,2,3,4,5,6,7,8,9};
	private double[] d2 = {65,1,244,6,5,6,456,8,4,415};
	private double[] d3 = {12,478,62,478,63,57,296,787,145,32587};
	private DoubleMatrix1D m1 = new DenseDoubleMatrix1D(d1);
	private DoubleMatrix1D m2 = new DenseDoubleMatrix1D(d2);
	private DoubleMatrix1D m3 = new DenseDoubleMatrix1D(d3);

	@Before
	public void setUp(){
		pca = new PCA_SVD();

		pca.addSample(m1);
		pca.addSample(m2);
		pca.addSample(m3);
	}

	/**
	 * Test method for {@link pca.PCA#addSample(org.ejml.data.DenseMatrix64F)}.
	 */
	@Test
	public void testGetSample() {
		assertTrue(m1.equals(pca.getSample(0)));
		assertTrue(m2.equals(pca.getSample(1)));
		assertTrue(m3.equals(pca.getSample(2)));
	}

	@Test
	public void testGetSampleSize() {
		assertEquals(10, pca.getSampleSize());
	}

	/**
	 * Test method for {@link pca.PCA#getMean()}.
	 */
	@Test
	public void testGetMean() {
		double[] mean = {77./3., 160, 308./3., 487./3., 24, 68./3., 758./3., 802./3., 157./3., 33011./3.};
		DoubleMatrix1D meand = new DenseDoubleMatrix1D(mean);
		assertTrue(meand.equals(pca.getMean()));
	}

}
