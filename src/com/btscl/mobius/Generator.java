package com.btscl.mobius;

public interface Generator
{
	void iterate(ComplexGraphics g);

	Mobius getTransform();

	void reset();
	
}
