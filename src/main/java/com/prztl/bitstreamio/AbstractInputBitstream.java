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

abstract class AbstractInputBitstream extends Bitstream
{
	private long compressedBitsRead; //the number of bits read via compressed methods
	
	public final long getCompressedBitsRead() { return compressedBitsRead; }
	
	protected final void incrementCompressedBitsCounter(int amount)
	{
		compressedBitsRead += amount;
	}
	
	protected abstract boolean readBit();
	
	public boolean readBoolean()
	{
		incrementCompressedBitsCounter(1);
		return readBit();
	}
	
	/**
	 * Reads a byte using the given number of bits
	 */
	public byte readByte(int bits)
	{
		checkBits(bits, 8);
		incrementCompressedBitsCounter(bits);
		
		byte b = 0;
		for( int x = 0; x < bits; x++ )
			b = Bits.set(b, x, readBit());
		return b;
	}
	
	/**
	 * Reads a short using the given number of bits
	 */
	public short readShort(int bits)
	{
		checkBits(bits, 16);
		incrementCompressedBitsCounter(bits);
		
		short b = 0;
		for( int x = 0; x < bits; x++ )
			b = Bits.set(b, x, readBit());
		return b;
	}
	
	/**
	 * Reads a int using the given number of bits
	 */
	public int readInt(int bits)
	{
		checkBits(bits, 32);
		incrementCompressedBitsCounter(bits);
		
		int b = 0;
		for( int x = 0; x < bits; x++ )
			b = Bits.set(b, x, readBit());
		return b;
	}
	
	public int readPositiveInt(int bits)
	{
		checkBits(bits, 31);
		incrementCompressedBitsCounter(bits);
		
		int b = 0;
		for( int x = 0; x < bits; x++ )
			b = Bits.set(b, x, readBit());
		return b;
	}
	
	/**
	 * Reads a long using the given number of bits
	 */
	public long readLong(int bits)
	{
		checkBits(bits, 64);
		incrementCompressedBitsCounter(bits);
		
		long b = 0;
		for( int x = 0; x < bits; x++ )
			b = Bits.set(b, x, readBit());
		return b;
	}
	
	public long readVLong(int groupSize)
	{
		if(groupSize <= 0 || groupSize > 64)
			throw new RuntimeException("groupSize out of range -- must be 0 <= groupSize < 64, was " +groupSize);
		
		long value = 0;
		int pos = 0;
		while(true)
		{
			for(int group = 0; group < groupSize; group++)
				value = Bits.set(value, pos++, readBit());
			
			boolean hasMoreBits = readBit();
			if(!hasMoreBits)
				break;
		}
		
		return value;
	}
	
	public double readDouble(boolean signBit, int exponentBits, int mantissaBits)
	{
		checkBits(exponentBits, DOUBLE_MAX_EXPONENT_BITS);
		checkBits(mantissaBits, DOUBLE_MAX_MANTISSA_BITS);
		incrementCompressedBitsCounter((signBit ? 1 : 0) + exponentBits + mantissaBits);
		
		//create a long
		long l = 0;
		
		//read the sign bit
		if( signBit )
			l = Bits.set(l, 63, readBit());
		
		//read the exponent bits
		for( int x = 0; x < exponentBits; x++ )
			l = Bits.set(l, 52 + x, readBit());
		
		//read the mantissa bits
		for( int x = 0; x < mantissaBits; x++ )
			l = Bits.set(l, x, readBit());
		
		//convert to a double and return
		return Double.longBitsToDouble(l);
	}
	
	/**
	 * Reads a "split" double -- one with the integral and decimal parts written separately as integers.
	 * @param maxAbsInteger The integral (a.k.a. the non-decimal part) of the value is expected to be between [-maxAbsInteger, maxAbsInteger]
	 * @param decimalPlaces The number of decimal places to preserve
	 * @return the value
	 */
	public double readSplitDouble(int maxAbsInteger, int decimalPlaces)
	{
		assert maxAbsInteger > 0;
		
		//write the sign bit
		final boolean signBit = readBit();
		
		//write the integral part
		final int integral = readInt(Bits.bitsNeeded(maxAbsInteger));
		
		//write the decimal part
		final long decimalMultiplier = (long)Math.pow(10, decimalPlaces);
		final long scaledDecimal = readLong(Bits.bitsNeeded(decimalMultiplier));
		final double decimal = scaledDecimal / (double)decimalMultiplier;
		
		return (signBit ? -1.0 : 1.0) * ((double)integral+decimal);
	}
}
