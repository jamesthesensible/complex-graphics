package com.btscl.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.complex.Complex;

import com.btscl.mobius.ComplexGraphics;
import com.btscl.mobius.DragonGenerator;
import com.btscl.mobius.Generator;

public class ImagePlotExample extends JFrame
{
	// Define constants for the various dimensions
	public static final IntCoord ORIGIN = new IntCoord(150, 300);
	public static final double SCALE = 200.0;
	public static final Color LINE_COLOR = Color.BLACK;

	// The moving line from (x1, y1) to (x2, y2), initially position at the center
	private Random rand = new Random();
	
	private ImagePanel imagePanel;
	
	private Generator generator;
	
	public ImagePlotExample(Generator generator)
	{
		this.generator = generator;
		// Set up a panel for the buttons
		JPanel btnPanel = new JPanel(new FlowLayout());
		JButton btnReset = new JButton("Reset");
		btnPanel.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				imagePanel.resetImage();
				imagePanel.repaint();
				requestFocus(); // change the focus to JFrame to receive KeyEvent
			}
		});
		JButton btnIterate = new JButton("Iterate");
		btnPanel.add(btnIterate);
		btnIterate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				iterate();
				imagePanel.repaint();
				requestFocus(); // change the focus to JFrame to receive KeyEvent
			}
		});

		// Set up a custom drawing JPanel
		imagePanel = new ImagePanel();
		imagePanel.setPreferredSize(new Dimension(ImagePanel.WIDTH, ImagePanel.HEIGHT));

		// Add both panels to this JFrame's content-pane
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(imagePanel, BorderLayout.CENTER);
		cp.add(btnPanel, BorderLayout.SOUTH);

		// "super" JFrame fires KeyEvent
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt)
			{
				switch (evt.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					repaint();
					break;
				}
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle the CLOSE button
		setTitle("Image Draw Example");
		pack(); // pack all the components in the JFrame
		setVisible(true); // show it
		requestFocus(); // set the focus to JFrame to receive KeyEvent

	}
	
	
	public void iterate()
	{
		ComplexGraphics g = imagePanel.getComplexGraphics(generator.getTransform());
		generator.iterate(g);
	}

	public void iterate_old()
	{
		Graphics g = imagePanel.getImgGraphics();
		
		g.setColor(LINE_COLOR);
		double xx = 0;
		double yy = 0;
		double newxx;
		double newyy = 0;
		for (int i = 1; i < 50000; i++)
		{
			if (rand.nextDouble() < 0.5)
			{
				newxx = (xx - yy) / 2.0;
				newyy = (xx + yy) / 2.0;
			}
			else
			{
				newxx = (-xx - yy) / 2.0 + 2.0;
				newyy = (xx - yy) / 2.0;
			}
			xx = newxx;
			yy = newyy;
			ICoord coord = getIntCoord(xx, yy);
			g.drawLine(coord.x, coord.y, coord.x, coord.y);
		}
	}

	
	// The entry main() method
	public static void main(String[] args)
	{
		// Run GUI codes on the Event-Dispatcher Thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				Generator gen = new DragonGenerator();
				new ImagePlotExample(gen); // Let the constructor do the job
			}
		});
	}

	private ICoord getIntCoord(double x, double y)
	{
		int ix = (int) (x * SCALE) + ORIGIN.x;
		int iy = (int) (-y * SCALE) + ORIGIN.y;
		return new ICoord(ix, iy);
	}
}

class ICoord
{
	public int x;
	public int y;

	public ICoord(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
