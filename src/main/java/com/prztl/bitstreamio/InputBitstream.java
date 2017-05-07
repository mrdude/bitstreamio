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

public class InputBitstream extends BaseInputBitstream
{
	public InputBitstream(ByteArrayInputStream in) { super(in); }
	public InputBitstream(byte[] b, int off, int len) { this( new ByteArrayInputStream( b, off, len ) ); }
	public InputBitstream(byte[] b) { this( new ByteArrayInputStream(b) ); }

	public byte readByte() { return readByte(8); }
	public short readShort() { return readShort(16); }
	public int readInt() { return readInt(32); }
	public long readLong() { return readLong(64); }
	public float readFloat() { return readFloat(true, FLOAT_MAX_EXPONENT_BITS, FLOAT_MAX_MANTISSA_BITS); }
	public double readDouble() { return readDouble(true, DOUBLE_MAX_EXPONENT_BITS, DOUBLE_MAX_MANTISSA_BITS); }
	
	public double readAngle()
	{
		return (double)readFloat(false, FLOAT_MAX_EXPONENT_BITS, FLOAT_MAX_MANTISSA_BITS);
	}

	public <E extends Enum<?>> E readEnum(E[] values)
	{
		int ord = readInt( Bits.bitsNeeded( values.length ) );
		return values[ord];
	}
}
