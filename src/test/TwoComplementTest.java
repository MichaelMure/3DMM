package test;

import static org.junit.Assert.*;

import org.junit.Test;

import util.TwoComplement;

public class TwoComplementTest {

	@Test
	public void testFrom2complement() {
		assertEquals((byte) 0, TwoComplement.from2complement(TwoComplement.to2complement((byte) 0)));
		assertEquals((byte) 1, TwoComplement.from2complement(TwoComplement.to2complement((byte) 1)));
		assertEquals((byte) 120, TwoComplement.from2complement(TwoComplement.to2complement((byte) 120)));
		assertEquals((byte) 127, TwoComplement.from2complement(TwoComplement.to2complement((byte) 127)));
		assertEquals((byte) -1, TwoComplement.from2complement(TwoComplement.to2complement((byte) -1)));
		assertEquals((byte) -128, TwoComplement.from2complement(TwoComplement.to2complement((byte) -128)));
		assertEquals((byte) -30, TwoComplement.from2complement(TwoComplement.to2complement((byte) -30)));
	}

	@Test
	public void testTo2complement() {
		assertEquals(0.0, TwoComplement.to2complement(TwoComplement.from2complement(0.0)), 0.01);
		assertEquals(1.0, TwoComplement.to2complement(TwoComplement.from2complement(1.0)), 0.01);
		assertEquals(120.0, TwoComplement.to2complement(TwoComplement.from2complement(120.0)), 0.01);
		assertEquals(127.0, TwoComplement.to2complement(TwoComplement.from2complement(127.0)), 0.01);
		assertEquals(128.0, TwoComplement.to2complement(TwoComplement.from2complement(128.0)), 0.01);
		assertEquals(190.0, TwoComplement.to2complement(TwoComplement.from2complement(190.0)), 0.01);
		assertEquals(250.0, TwoComplement.to2complement(TwoComplement.from2complement(250.0)), 0.01);
		assertEquals(255.0, TwoComplement.to2complement(TwoComplement.from2complement(255.0)), 0.01);
	}

}
