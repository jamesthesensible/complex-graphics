package com.btscl.mobius;

import org.apache.commons.math3.complex.Complex;

public class Mobius
{
	public static Mobius IDENTITY = new Mobius(false, Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE);
	
	private Complex a;
	private Complex b;
	private Complex c;
	private Complex d;
	
	public static Mobius getScale(Complex k)
	{
		Complex a = k.sqrt();
		return new Mobius(false, a, Complex.ZERO, Complex.ZERO, a.reciprocal());
	}
	
	public static Mobius getScale(double k)
	{
		return getScale(new Complex(k, 0));
	}

	public static Mobius getRotation(double angle)
	{
		Complex k = new Complex(Math.cos(angle), Math.sin(angle));
		return getScale(k);
	}
	
	public static Mobius getCayley()
	{
		return new Mobius(false, Complex.ONE, Complex.I.negate(), Complex.ONE, Complex.I);
	}
	
	public static Mobius pairCircles(RiemannCircle a, RiemannCircle b, double rotAngle)
	{
		if(a.isCircle() && b.isCircle())
		{
			double r = a.getRadius();
			double s = b.getRadius();
			Complex p = a.getCentre();
			Complex q = b.getCentre();
			Mobius shiftP = Mobius.getTranslation(p.negate());
			Mobius invert = Mobius.getUnitCircleInvert();
			Mobius rotate = Mobius.getRotation(rotAngle);
			Mobius scale = Mobius.getScale(r * s);
			Mobius shiftQ = Mobius.getTranslation(q);
			return shiftQ.multiply(scale).multiply(invert).multiply(rotate).multiply(shiftP);
		}
		return null;
	}
	
	public static Mobius getUnitCircleInvert()
	{
		return new Mobius(false, Complex.ZERO, Complex.I, Complex.I, Complex.ZERO);
	}

	/**
	 * Produces a simple translation 
	 * @param b Translation to add
	 * @return Translation transform.
	 */
	public static Mobius getTranslation(Complex b)
	{
		return new Mobius(false, Complex.ONE, b, Complex.ZERO, Complex.ONE);
	}
	
	public static Mobius getRotation(double angle, Complex centre)
	{
		Mobius rot = getRotation(angle);
		Mobius trans = getTranslation(centre);
		return rot.conj(trans);
	}
	
	public static Mobius getScale(Complex factor, Complex centre)
	{
		Mobius scale = getScale(factor);
		Mobius trans = getTranslation(centre);
		return scale.conj(trans);
	}
	
	public Mobius(boolean norm, Complex a, Complex b, Complex c, Complex d)
	{
		if(norm)
		{
			Complex det = (a.multiply(d)).subtract(b.multiply(c));
			double detMag = det.abs();
			if((!Double.isFinite(detMag)) || detMag == 0)
			{
				throw new IllegalArgumentException("Zero or bad determinant. Unable to normalise.");
			}
			Complex rootDet = det.sqrt();
			this.a = a.divide(rootDet);
			this.b = b.divide(rootDet);
			this.c = c.divide(rootDet);
			this.d = d.divide(rootDet);
		}
		else
		{
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}
	}
	
	public Mobius inv()
	{
		return new Mobius(false, d, b.negate(), c.negate(), a);
	}
	

	/**
	 * Conjugates a mobius transofrm with another.
	 * @return t.this.t^-1
	 */
	public Mobius conj(Mobius t)
	{
		return t.multiply(this).multiply(t.inv());
	}
	
	public Mobius multiply(Mobius t)
	{
		Complex newa = (a.multiply(t.a)).add(b.multiply(t.c));
		Complex newb = (a.multiply(t.b)).add(b.multiply(t.d));
		Complex newc = (c.multiply(t.a)).add(d.multiply(t.c));
		Complex newd = (c.multiply(t.b)).add(d.multiply(t.d));
		return new Mobius(false, newa, newb, newc, newd);
	}
	
	public Complex map(Complex z)
	{
		return ((z.multiply(a)).add(b)).divide((z.multiply(c)).add(d));
	}

	public Complex getA()
	{
		return a;
	}
	
	public Complex getB()
	{
		return b;
	}

	public Complex getC()
	{
		return c;
	}

	public Complex getD()
	{
		return d;
	}

	public RiemannCircle mapCircle(RiemannCircle circle)
	{
		Complex p = circle.getCentre();
		double r = circle.getRadius();
		Complex z;
		Complex dOverC = d.divide(c);
		if(dOverC.isInfinite() || dOverC.isNaN())
		{
			z = p;
		}
		else
		{
			z = p.subtract( (dOverC.add(p)).conjugate().reciprocal().multiply(r*r) );
		}
		Complex newCentre = map(z);
		
		double newRadius = newCentre.subtract(map(p.add(r))).abs();
		
		return new RiemannCircle(newCentre, newRadius);
	}

}
