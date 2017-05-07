/**
 * The MIT License
 * Copyright © 2017 Warren S
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.prztl.bitstreamio;

/**
 * Various bit twiddling functions.
 */
public class Bits
{
	/** Returns the number of bits needed to encode the given number of integer values */
	public static int bitsNeeded(long range)
	{
		//TODO brute force? There *has* to be a better way...
		int bits = 1;
		for( ; (1L << bits) < range; bits++ );
		return bits;
	}

	public static boolean get(int b, int pos)
	{
		return (b & ( 1 << pos )) == (1 << pos);
	}
	
	public static boolean get(long b, int pos)
	{
		return (b & ( 1L << pos )) == (1L << pos);
	}
	
	public static byte set(byte b, int pos, boolean flag)
	{
		if( flag )
			b |= (1 << pos);
		else
			b &= ~(1 << pos);
		
		return b;
	}
	
	public static short set(short b, int pos, boolean flag)
	{
		if( flag )
			b |= (1 << pos);
		else
			b &= ~(1 << pos);
		
		return b;
	}
	
	public static int set(int b, int pos, boolean flag)
	{
		if( flag )
			b |= (1 << pos);
		else
			b &= ~(1 << pos);
		
		return b;
	}
	
	public static long set(long b, int pos, boolean flag)
	{
		if( flag )
			b |= (1L << pos);
		else
			b &= ~(1L << pos);
		
		return b;
	}
}
