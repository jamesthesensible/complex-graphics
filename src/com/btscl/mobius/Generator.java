package com.btscl.mobius;

public interface Generator
{
	void iterate(ComplexGraphics g);

	Mobius getTransform();

	void reset();

	/**
	 * Returns true if iterate will not do anything anymore.
	 * @return
	 */
	boolean isFinished();

	/**
	 * How long to sleep between frames
	 * @return
	 */
	long getSleepMillis();
	
}
