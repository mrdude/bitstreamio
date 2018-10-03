package com.prztl.bitstreamio.tests;

class Utils
{
	/** @return 2**b */
	static long pow2(int b)
	{
		if(b < 0)
			return 1;
		
		if(b == 0)
			return 1;
		
		if(b == 1)
			return 2;
		
		if(b == 2)
			return 2*2;
		
		if(b % 2 == 0)
		{
			long tmp = pow2(b/2);
			return tmp * tmp;
		}
		else
		{
			long tmp = pow2(b/2);
			return tmp * tmp * 2;
		}
	}
}
