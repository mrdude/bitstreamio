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

abstract class AbstractOutputBitstream extends Bitstream
{
	private int compressedBitsWritten; //the number of bits written
	
	public final int getCompressedBitsWritten() { return compressedBitsWritten; }
	
	protected final void incrementCompressedBitsCounter(int amount)
	{
		compressedBitsWritten += amount;
	}
	
	/**
	 * Packs the bits into a byte array and returns it.
	 */
	public abstract byte[] toByteArray();
	
	protected abstract void writeBit(boolean bit);
	
	public void writeBoolean(boolean b)
	{
		incrementCompressedBitsCounter(1);
		writeBit(b);
	}
	
	/**
	 * Writes the given byte using the given number of bits.
	 */
	public void writeByte(byte b, int bits)
	{
		incrementCompressedBitsCounter(bits);
		
		for( int x=0; x<Math.min(8,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	public void writeShort(short b, int bits)
	{
		incrementCompressedBitsCounter(bits);
		
		for( int x=0; x<Math.min(16,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	public void writeInt(int b, int bits)
	{
		incrementCompressedBitsCounter(bits);
		
		for( int x=0; x<Math.min(32,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	public void writeInt(int b, int bits, boolean signBit)
	{
		incrementCompressedBitsCounter(bits);
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
		incrementCompressedBitsCounter(bits);
		
		for( int x=0; x<Math.min(64,bits); x++ )
		{
			writeBit( Bits.get(b, x) );
		}
	}
	
	//Untested
	@Deprecated
	public void writeFloat(float f, boolean signBit, int exponentBits, int mantissaBits)
	{
		incrementCompressedBitsCounter((signBit ? 1 : 0) + exponentBits + mantissaBits);
		
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
		incrementCompressedBitsCounter((signBit ? 1 : 0) + exponentBits + mantissaBits);
		
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
	
	/**
	 * Writes a "split" double -- one with the integral and decimal parts written separately as integers.
	 * @param value the value to write
	 * @param maxAbsInteger The integral (a.k.a. the non-decimal part) of the value is expected to be between [-maxAbsInteger, maxAbsInteger]
	 * @param decimalPlaces The number of decimal places to preserve
	 */
	public void writeSplitDouble(double value, int maxAbsInteger, int decimalPlaces)
	{
		assert maxAbsInteger > 0;
		
		final int integral = (int)Math.abs(value);
		final double decimal = Math.abs(value) - (double)integral;
		
		//write the sign bit
		writeBit(value < 0);
		
		//write the integral part
		writeInt(integral, Bits.bitsNeeded(maxAbsInteger));
		
		//write the decimal part
		final long decimalMultiplier = (long)Math.pow(10, decimalPlaces);
		writeLong((long)(decimal * decimalMultiplier), Bits.bitsNeeded(decimalMultiplier));
	}
}
