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

public class OutputBitstream extends BaseOutputBitstream
{
	public OutputBitstream() {}

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
