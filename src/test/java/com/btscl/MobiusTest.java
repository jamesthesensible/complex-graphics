package com.btscl;

import static org.junit.Assert.*;

import org.apache.commons.math3.complex.Complex;
import org.junit.Test;

import com.btscl.mobius.Mobius;

public class MobiusTest
{
	private double TOL = 0.001;
	@Test
	public void testMobius()
	{
		Mobius m = Mobius.IDENTITY;
//		Complex a = Complex.ONE;
//		Complex b = a.multiply(2.0);
//		Complex c = a.multiply(3.0);
//		Complex d = a.multiply(4.0);
		Complex a = Complex.ZERO;
		Complex b = Complex.ONE;
		Complex c = Complex.ONE.negate();
		Complex d = Complex.ZERO;

		Mobius m2 = new Mobius(true, a, b, c, d);
		Mobius m2Inv = m2.inv();
		Mobius id = m2.multiply(m2Inv);
		assertTrue(Complex.equals(Complex.ONE, id.getA(), TOL));
	}
}
