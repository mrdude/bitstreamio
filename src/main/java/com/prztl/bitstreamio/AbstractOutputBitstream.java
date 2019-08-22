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

public abstract class AbstractOutputBitstream extends Bitstream
{
	private long compressedBitsWritten; //the number of bits written
	
	public final long getCompressedBitsWritten() { return compressedBitsWritten; }
	
	protected final void incrementCompressedBitsCounter(int amount)
	{
		compressedBitsWritten += amount;
	}
	
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
		checkBits(bits, 8);
		incrementCompressedBitsCounter(bits);
		
		for( int x = 0; x < bits; x++ )
			writeBit( Bits.get(b, x) );
	}
	
	public void writeShort(short b, int bits)
	{
		checkBits(bits, 16);
		incrementCompressedBitsCounter(bits);
		
		for( int x = 0; x < bits; x++ )
			writeBit( Bits.get(b, x) );
	}
	
	public void writeInt(int b, int bits)
	{
		checkBits(bits, 32);
		incrementCompressedBitsCounter(bits);
		
		for( int x = 0; x < bits; x++ )
			writeBit( Bits.get(b, x) );
	}
	
	public void writePositiveInt(int b, int bits)
	{
		checkBits(bits, 31);
		if(b < 0)
			throw new RuntimeException("expected positive int, actual: " +b);
		
		incrementCompressedBitsCounter(bits);
		
		for( int x = 0; x < bits; x++ )
			writeBit( Bits.get(b, x) );
	}
	
	public void writeLong(long b, int bits)
	{
		checkBits(bits, 64);
		incrementCompressedBitsCounter(bits);
		
		for( int x = 0; x < bits; x++ )
			writeBit( Bits.get(b, x) );
	}
	
	public void writeVLong(long b, int groupSize)
	{
		if(groupSize <= 0 || groupSize > 64)
			throw new RuntimeException("groupSize out of range -- must be 0 <= groupSize < 64, was " +groupSize);
		
		while(true)
		{
			for(int group = 0; group < groupSize; group++ )
			{
				writeBoolean((b & 0x1) == 1);
				b >>>= 1;
			}
			
			boolean hasMoreBits = b != 0;
			writeBit(hasMoreBits);
			
			if(!hasMoreBits)
				break;
		}
	}
	
	public void writeDouble(double d, boolean signBit, int exponentBits, int mantissaBits)
	{
		checkBits(exponentBits, DOUBLE_MAX_EXPONENT_BITS);
		checkBits(mantissaBits, DOUBLE_MAX_MANTISSA_BITS);
		incrementCompressedBitsCounter((signBit ? 1 : 0) + exponentBits + mantissaBits);
		
		//convert to a long
		long l = Double.doubleToLongBits( d );
		
		//write the sign bit
		if( signBit )
			writeBit( Bits.get(l, 63) );
		
		//write the exponent bits
		for( int x = 0; x < exponentBits; x++ )
			writeBit( Bits.get(l, 52 + x) );
		
		//write the mantissa bits
		for( int x = 0; x < mantissaBits; x++ )
			writeBit( Bits.get(l, x) );
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
		writeBoolean(value < 0);
		
		//write the integral part
		writeInt(integral, Bits.bitsNeeded(maxAbsInteger));
		
		//write the decimal part
		final long decimalMultiplier = (long)Math.pow(10, decimalPlaces);
		writeLong((long)(decimal * decimalMultiplier), Bits.bitsNeeded(decimalMultiplier));
	}
}
