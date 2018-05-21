package com.btscl.mobius;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.math3.complex.Complex;

public class SchottkySequenceGenerator implements Generator
{
	private static double TOL = 0.001;
	private double maxTweakInc = Math.toRadians(5);
	private double minTweakInc = Math.toRadians(0.7);
	private double tweakAngle = Math.toRadians(0);
	private double ONE_OVER_ROOT2 = 1/Math.sqrt(2.0);
	private RiemannCircle c1 = new RiemannCircle(Complex.ONE.negate(), ONE_OVER_ROOT2);
	private RiemannCircle c2 = new RiemannCircle(Complex.ONE, ONE_OVER_ROOT2);
	private RiemannCircle c3 = new RiemannCircle(new Complex(0.0, 1.0), ONE_OVER_ROOT2);
	private RiemannCircle c4 = new RiemannCircle(new Complex(0.0, -1.0), ONE_OVER_ROOT2);
	
	private Mobius transform;
	
	
	public SchottkySequenceGenerator()
	{
		transform = Mobius.IDENTITY;
		reset();
	}
	
	/**
	 * When tweak angle is small (or near 2PI) we want it to vary slowly.
	 * This smoothly adjusts the tweak increment depending on the current
	 * tweak angle. 
	 */
	private void nextTweakAngle()
	{
		double cost = Math.cos(tweakAngle);
		double inc = minTweakInc + ((1.0 - cost)/2.0) * (maxTweakInc - minTweakInc);
		tweakAngle += inc;
	}
	
	@Override
	public void iterate(ComplexGraphics g)
	{
		g.reset();
		Mobius transA = Mobius.pairCircles(c2, c1, Math.PI + tweakAngle);
		Mobius transB = Mobius.pairCircles(c4, c3, tweakAngle);
		Mobius transAInv = transA.inv();
		Mobius transBInv = transB.inv();

		List<Mobius> schottkyList = new ArrayList<>();
		schottkyList.add(transA);
		schottkyList.add(transB);
		schottkyList.add(transAInv);
		schottkyList.add(transBInv);
		
		List<RiemannCircle> plotList = new ArrayList<>();
		plotList.add(c1);
		plotList.add(c2);
		plotList.add(c3);
		plotList.add(c4);

		int iteration = 0;
		
		while(!plotList.isEmpty())
		{
			Color col = iteration%2==0 ? Color.BLACK : Color.RED;
			List<RiemannCircle> nextPlotList = new ArrayList<>();

			for ( RiemannCircle circle : plotList)
			{
				double pixRadius = g.getPixelRadius(circle);
				if(pixRadius > 2.0)
				{
					g.fillRiemannCircle(circle, col);
					double currentRadius = circle.getRadius();
			
					for(Mobius trans : schottkyList)
					{
						RiemannCircle mappedCircle = trans.mapCircle(circle);
						if(mappedCircle.getRadius() < currentRadius - TOL)
						{
							nextPlotList.add(mappedCircle);
						}
					}
				}
			}
			plotList = nextPlotList;
			iteration++;
		}
		nextTweakAngle();
	}

	@Override
	public Mobius getTransform()
	{
		return transform;
	}

	@Override
	public void reset()
	{
		tweakAngle = 0;
	}
	
	// The entry main() method
	public static void main(String[] args)
	{
		// Run GUI codes on the Event-Dispatcher Thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				Generator gen = new SchottkySequenceGenerator();
				new ComplexImagePlot(gen); // Let the constructor do the job
			}
		});
	}

	@Override
	public boolean isFinished()
	{
		return tweakAngle > (4.0 * Math.PI + minTweakInc);
	}
	
	@Override
	public long getSleepMillis()
	{
		return 30;
	}

}
