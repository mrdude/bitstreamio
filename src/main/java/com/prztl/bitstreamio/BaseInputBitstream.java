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

import java.io.ByteArrayInputStream;

class BaseInputBitstream extends Bitstream
{
	private ByteArrayInputStream in;
	private byte b;
	private int pos = 8;
	
	private int compressedBitsRead; //the number of bits written
	
	protected BaseInputBitstream(ByteArrayInputStream in)
	{
		this.in = in;
	}
	
	public int getCompressedBitsRead() { return compressedBitsRead; }
	
	private boolean readBit()
	{
		//do we need to read the next byte?
		if( pos == 8 )
		{
			int data = in.read();
			if( data == -1 )
			{
				b = 0;
			}
			else
			{
				b = (byte)data;
			}
			
			pos = 0;
		}
		
		//get the byte
		return Bits.get(b, pos++);
	}
	
	public boolean readBoolean()
	{
		compressedBitsRead += 1;
		return readBit();
	}
	
	/**
	 * Reads a byte using the given number of bits
	 */
	public byte readByte(int bits)
	{
		compressedBitsRead += bits;
		
		byte b = 0;
		
		for( int x=0; x<Math.min(8, bits); x++ )
		{
			b = Bits.set(b, x, readBit());
		}
		
		return b;
	}
	
	/**
	 * Reads a short using the given number of bits
	 */
	public short readShort(int bits)
	{
		compressedBitsRead += bits;
		
		short b = 0;
		
		for( int x=0; x<Math.min(16, bits); x++ )
		{
			b = Bits.set(b, x, readBit());
		}
		
		return b;
	}
	
	/**
	 * Reads a int using the given number of bits
	 */
	public int readInt(int bits)
	{
		compressedBitsRead += bits;
		
		int b = 0;
		
		for( int x=0; x<Math.min(32, bits); x++ )
		{
			b = Bits.set(b, x, readBit());
		}
		
		return b;
	}
	
	public int readInt(int bits, boolean signBit)
	{
		compressedBitsRead += bits;
		if( signBit ) { bits++; }
		
		int b = 0;
		
		for( int x=0; x<Math.min(32, bits); x++ )
		{
			b = Bits.set(b, x, readBit());
		}
		
		//read the sign bit
		if( signBit ) { b = Bits.set(b, 31, readBit()); }
		
		return b;
	}
	
	/**
	 * Reads a long using the given number of bits
	 */
	public long readLong(int bits)
	{
		compressedBitsRead += bits;
		
		long b = 0;
		
		for( int x=0; x<Math.min(64, bits); x++ )
		{
			b = Bits.set(b, x, readBit());
		}
		
		return b;
	}
	
	//Untested
	public float readFloat(boolean signBit, int exponentBits, int mantissaBits)
	{
		compressedBitsRead += (signBit ? 1 : 0) + exponentBits + mantissaBits;
		
		//create an int
		int i = 0;
		
		//read the sign bit
		if( signBit )
		{
			i = Bits.set(i, 31, readBit());
		}
		
		//read the exponent bits
		for( int x=0; x<exponentBits; x++ )
		{
			i = Bits.set(i, 23 + x, readBit());
		}
		
		//read the mantissa bits
		for( int x=0; x<mantissaBits; x++ )
		{
			i = Bits.set(i, x, readBit());
		}
		
		//convert to a float and return
		return Float.intBitsToFloat(i);
	}
	
	public double readDouble(boolean signBit, int exponentBits, int mantissaBits)
	{
		compressedBitsRead += (signBit ? 1 : 0) + exponentBits + mantissaBits;
		
		//create a long
		long l = 0;
		
		//read the sign bit
		if( signBit )
		{
			l = Bits.set(l, 63, readBit());
		}
		
		//read the exponent bits
		for( int x=0; x<exponentBits; x++ )
		{
			l = Bits.set(l, 52 + x, readBit());
		}
		
		//read the mantissa bits
		for( int x=0; x<mantissaBits; x++ )
		{
			l = Bits.set(l, x, readBit());
		}
		
		//convert to a double and return
		return Double.longBitsToDouble(l);
	}
}
