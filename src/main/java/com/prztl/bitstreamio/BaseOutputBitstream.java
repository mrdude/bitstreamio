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

abstract class BaseOutputBitstream extends Bitstream
{
	private int compressedBitsWritten; //the number of bits written
	
	public int getCompressedBitsWritten() { return compressedBitsWritten; }
	
	/**
	 * Packs the bits into a byte array and returns it.
	 */
	public abstract byte[] toByteArray();
	
	protected abstract void writeBit(boolean bit);
	
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
	
	public void writeByte(byte b) { writeByte( b, 8 ); }
	public void writeShort(short s) { writeShort( s, 16 ); }
	public void writeInt(int i) { writeInt( i, 32 ); }
	public void writeLong(long l) { writeLong( l, 64 ); }
	public void writeFloat(float f) { writeFloat( f, true, FLOAT_MAX_EXPONENT_BITS, FLOAT_MAX_MANTISSA_BITS ); }
	public void writeDouble(double d) { writeDouble( d, true, DOUBLE_MAX_EXPONENT_BITS, DOUBLE_MAX_MANTISSA_BITS ); }
	
	private double normalizeRadianAngle(double ang)
	{
		ang %= 2 * Math.PI;
		if( ang < 0 )
			ang += 2 * Math.PI;
		
		return ang;
	}
	
	public void writeAngle(double ang)
	{
		ang = normalizeRadianAngle(ang);
		writeFloat( (float)ang, true, FLOAT_MAX_EXPONENT_BITS, FLOAT_MAX_MANTISSA_BITS);
	}
	
	public <E extends Enum<?>> void writeEnum(E e, E[] values)
	{
		writeInt( e.ordinal(), Bits.bitsNeeded( values.length ) );
	}
	
}
