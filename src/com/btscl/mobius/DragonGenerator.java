package com.btscl.mobius;

import java.awt.Color;
import java.util.Random;

import org.apache.commons.math3.complex.Complex;

public class DragonGenerator implements Generator
{
	private static Color LINE_COLOR = Color.BLACK;
	private Random rand = new Random();
	
	Mobius transA;
	Mobius transB;
	
	private Mobius transform;
	
	public DragonGenerator()
	{
		Mobius rotA = Mobius.getRotation(Math.toRadians(45));
		Mobius scale = Mobius.getScale(Complex.ONE.divide(Math.sqrt(2.0)));
		transA = rotA.multiply(scale);
		Mobius rotB = Mobius.getRotation(Math.toRadians(135), new Complex(2.0, 0));
		Mobius shiftB = Mobius.getTranslation(new Complex(2.0, 0));
		transB = shiftB.multiply(rotB).multiply(scale);
		
		transform = Mobius.getScale(new Complex(0.3, 0));
		//		Complex centre = new Complex(1.0, 0.5);
//		transform = Mobius.getTranslation(centre.negate());
//		transform = Mobius.getCayley().multiply(transform);
//		Mobius shrink = Mobius.getScale(new Complex(0.1, 0));
//		transform = shrink.multiply(transform);
	}
	
	@Override
	public void iterate(ComplexGraphics g)
	{
		g.getGraphics().setColor(LINE_COLOR);
		Complex z = Complex.ZERO;
		Color col;
		for (int i = 1; i < 50000; i++)
		{
			if (rand.nextDouble() < 0.5)
			{
				z = transA.map(z);
				col = Color.BLUE;
			}
			else
			{
				z = transB.map(z);
				col = Color.RED;
			}
			g.plot(z, col);
		}

	}

	@Override
	public Mobius getTransform()
	{
		return transform;
	}

	@Override
	public void reset()
	{
		// No action required
	}

	@Override
	public boolean isFinished()
	{
		// Go forever!
		return false;
	}
	
	@Override
	public long getSleepMillis()
	{
		return 10;
	}

}
