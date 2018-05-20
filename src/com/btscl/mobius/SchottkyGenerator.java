package com.btscl.mobius;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.math3.complex.Complex;

public class SchottkyGenerator implements Generator
{
	private static double TOL = 0.001;
	private double tweakAngle = Math.toRadians(0);
	private double ONE_OVER_ROOT2 = 1/Math.sqrt(2.0);
	private RiemannCircle c1 = new RiemannCircle(Complex.ONE.negate(), ONE_OVER_ROOT2);
	private RiemannCircle c2 = new RiemannCircle(Complex.ONE, ONE_OVER_ROOT2);
	private RiemannCircle c3 = new RiemannCircle(new Complex(0.0, 1.0), ONE_OVER_ROOT2);
	private RiemannCircle c4 = new RiemannCircle(new Complex(0.0, -1.0), ONE_OVER_ROOT2);
	private Mobius transA = Mobius.pairCircles(c2, c1, Math.PI + tweakAngle);
	private Mobius transB = Mobius.pairCircles(c4, c3, tweakAngle);
	private Mobius transAInv = transA.inv();
	private Mobius transBInv = transB.inv();
	private List<Mobius> schottkyList = new ArrayList<>();
	
	private Mobius transform;
	
	private List<RiemannCircle> plotList;
	private int iteration;
	
	public SchottkyGenerator()
	{
		transform = Mobius.IDENTITY;
		//transform = Mobius.getScale(10).multiply(Mobius.getTranslation(new Complex(-1.1)));
		schottkyList.add(transA);
		schottkyList.add(transB);
		schottkyList.add(transAInv);
		schottkyList.add(transBInv);
		reset();
	}
	
	@Override
	public void iterate(ComplexGraphics g)
	{
		Color col = iteration%2==0 ? Color.BLACK : Color.RED;
		
		g.getGraphics().setColor(col);
		List<RiemannCircle> nextPlotList = new ArrayList<>();
		if(isFinished())
		{
			System.out.println("Finished!");
		}
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

	@Override
	public Mobius getTransform()
	{
		return transform;
	}

	@Override
	public void reset()
	{
		plotList = new ArrayList<>();
		plotList.add(c1);
		plotList.add(c2);
		plotList.add(c3);
		plotList.add(c4);
		iteration = 0;
	}
	
	// The entry main() method
	public static void main(String[] args)
	{
		// Run GUI codes on the Event-Dispatcher Thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				Generator gen = new SchottkyGenerator();
				new ComplexImagePlot(gen); // Let the constructor do the job
			}
		});
	}

	@Override
	public boolean isFinished()
	{
		return plotList.isEmpty();
	}

	@Override
	public long getSleepMillis()
	{
		return 10;
	}
}
