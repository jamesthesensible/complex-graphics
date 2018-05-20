package com.btscl.mobius;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.apache.commons.math3.complex.Complex;

public class ComplexGraphics
{
	private Graphics2D g;
	private int centreX;
	private int centreY;
	private double scale;
	private Mobius transform;
	
	public Graphics getGraphics()
	{
		return g;
	}
	
	public ComplexGraphics(Graphics2D g, int width, int height, double scale, Mobius transform)
	{
		this.g = g;
		this.centreX = width/2;
		this.centreY = height/2;
		this.scale = scale;
		this.transform = transform;
	}

	public ComplexGraphics(Graphics2D g, int width, int height, double scale)
	{
		this(g, width, height, scale, Mobius.IDENTITY);
	}
	
	public void plot(Complex z)
	{
		Complex tz = transform.map(z);
		double x = tz.getReal();
		double y = tz.getImaginary();
		int xx = centreX + (int)(x * scale);
		int yy = centreY - (int)(y * scale);
		g.drawLine(xx,  yy,  xx,  yy);
	}
	
	public void plot(Complex z, Color col)
	{
		g.setColor(col);
		plot(z);
	}

	/**
	 * Fills with a radial gradient from white to col.
	 * @param circle
	 * @param col
	 */
	public void fillRiemannCircle(RiemannCircle circle, Color col)
	{
		RiemannCircle transformedCircle = transform.mapCircle(circle);
		Complex c = transformedCircle.getCentre();
		double cx = c.getReal();
		double cy = c.getImaginary();
		double cxx = centreX + (cx * scale);
		double cyy = centreY - (cy * scale);
		double radius = transformedCircle.getRadius() * scale;
		
		Shape s = new Ellipse2D.Double(cxx - radius, cyy-radius, 2*radius, 2*radius);
		Color[] cols = new Color[] {Color.WHITE, col};
		float[] fracts = new float[] {0, 1};
		RadialGradientPaint paint = new RadialGradientPaint((float)cxx, (float)cyy, (float)radius, fracts, cols);
		g.setPaint(paint);
		g.fill(s);
	}

	/**
	 * 
	 * @param circle
	 * @return The radius of the given circle in pixels after the transform has been applied.
	 */
	public double getPixelRadius(RiemannCircle circle)
	{
		RiemannCircle transformedCircle = transform.mapCircle(circle);
		return transformedCircle.getRadius() * scale;
	}

	
}
