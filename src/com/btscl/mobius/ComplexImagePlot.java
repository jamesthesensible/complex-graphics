package com.btscl.mobius;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ComplexImagePlot extends JFrame
{
	// Define constants for the various dimensions
	public static final double SCALE = 200.0;
	public static final Color LINE_COLOR = Color.BLACK;

	private ImagePanel imagePanel;
	
	private Generator generator;
	
	public ComplexImagePlot(Generator generator)
	{
		this.generator = generator;
		// Set up a panel for the buttons
		JPanel btnPanel = new JPanel(new FlowLayout());
		JButton btnReset = new JButton("Reset");
		btnPanel.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				generator.reset();
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
	
	// The entry main() method
	public static void main(String[] args)
	{
		// Run GUI codes on the Event-Dispatcher Thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				Generator gen = new DragonGenerator();
				new ComplexImagePlot(gen); // Let the constructor do the job
			}
		});
	}
}


