package com.btscl.mobius;

import org.apache.commons.math3.complex.Complex;

/**
 * Represents a circle on the riemann sphere.
 * Equivalently a circle or a line on the complex plane.
 * @author James
 *
 */
public class RiemannCircle
{
	private boolean isCircle;
	private Complex centre;
	private double radius;
	private double direction;
	private double offset; // Interior is same side of the line as direction if positive or zero
	
	public RiemannCircle(Complex centre, double radius)
	{
		isCircle = true;
		this.centre = centre;
		this.radius = radius;
		this.direction = 0;
		this.offset = 0;
	}
	
	public RiemannCircle(double direction, double offset)
	{
		isCircle = false;
		this.centre = Complex.INF;
		this.radius = Double.POSITIVE_INFINITY;
		this.direction = direction;
		this.offset = offset;
	}

	public boolean isCircle()
	{
		return isCircle;
	}

	public double getRadius()
	{
		return radius;
	}
	
	public Complex getCentre()
	{
		return centre;
	}
}
