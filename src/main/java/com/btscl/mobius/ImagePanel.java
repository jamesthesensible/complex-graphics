package com.btscl.mobius;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	private BufferedImage img;
	public static int WIDTH = 700;
	public static int HEIGHT = 500;
	public static double COMPLEX_SCALE = 200.0;
	
	public ImagePanel()
	{
		resetImage();
	}
	
	public void resetImage()
	{
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = getImgGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	public Graphics getImgGraphics()
	{
		return img.getGraphics();
	}

	public ComplexGraphics getComplexGraphics(Mobius transform)
	{
		return new ComplexGraphics(img.createGraphics(), WIDTH, HEIGHT, COMPLEX_SCALE, transform);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.drawImage(img, 0, 0, null);
	}
}
