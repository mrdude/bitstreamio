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

import java.io.ByteArrayOutputStream;

class BaseOutputBitstream extends Bitstream
{
	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	private byte b; //the byte we are writing to
	private int pos; //the position in the byte
	
	private int compressedBitsWritten; //the number of bits written
	
	public int getCompressedBitsWritten() { return compressedBitsWritten; }
	
	/**
	 * Flushes the current byte.
	 */
	private void flush()
	{
		out.write(b);
		b = 0;
		pos = 0;
	}
	
	/**
	 * Packs the bits into a byte array and returns it.
	 */
	public byte[] toByteArray()
	{
		if( pos != 0 ) { flush(); }
		return out.toByteArray();
	}
	
	private void writeBit(boolean bit)
	{
		b = Bits.set(b, pos, bit);
		pos++;
		
		//flush the byte
		if( pos == 8 ) { flush(); }
	}
	
	public void writeBoolean(boolean b)
	{
		compressedBitsWritten += 1;
		writeBit(b);
	}
	
	/**
	 * Writes the given byte using the given number of bits.
	 */
	public void writeByte(byte b, int bits)
	{
		compressedBitsWritten += bits;
		
		for( int x=0; x<Math.min(8,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	public void writeShort(short b, int bits)
	{
		compressedBitsWritten += bits;
		
		for( int x=0; x<Math.min(16,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	public void writeInt(int b, int bits)
	{
		compressedBitsWritten += bits;
		
		for( int x=0; x<Math.min(32,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	public void writeInt(int b, int bits, boolean signBit)
	{
		compressedBitsWritten += bits;
		if( signBit ) { bits++; }
		
		for( int x=0; x<Math.min(32,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
		
		//write the sign bit last
		if( signBit ) { writeBit( Bits.get(b, 31) ); }
	}
	
	public void writeLong(long b, int bits)
	{
		compressedBitsWritten += bits;
		
		for( int x=0; x<Math.min(64,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	//Untested
	public void writeFloat(float f, boolean signBit, int exponentBits, int mantissaBits)
	{
		compressedBitsWritten += (signBit ? 1 : 0) + exponentBits + mantissaBits;
		
		//convert to an int
		int i = Float.floatToIntBits(f);
		
		//write the sign bit
		if( signBit )
		{
			writeBit( Bits.get(i, 31) );
		}
		
		//write the exponent bits
		for( int x=0; x<exponentBits; x++ )
		{
			writeBit( Bits.get(i, 23 + x) );
		}
		
		//write the mantissa bits
		for( int x=0; x<mantissaBits; x++ )
		{
			writeBit( Bits.get(i, x) );
		}
	}
	
	public void writeDouble(double d, boolean signBit, int exponentBits, int mantissaBits)
	{
		compressedBitsWritten += (signBit ? 1 : 0) + exponentBits + mantissaBits;
		
		//convert to a long
		long l = Double.doubleToLongBits( d );
		
		//write the sign bit
		if( signBit )
		{
			writeBit( Bits.get(l, 63) );
		}
		
		//write the exponent bits
		for( int x=0; x<exponentBits; x++ )
		{
			writeBit( Bits.get(l, 52 + x) );
		}
		
		//write the mantissa bits
		for( int x=0; x<mantissaBits; x++ )
		{
			writeBit( Bits.get(l, x) );
		}
	}
	
}
