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

public class OutputBitstream extends AbstractOutputBitstream
{
	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	private byte b; //the byte we are writing to
	private int pos; //the position in the byte
	
	public OutputBitstream() {}
	
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
	
	protected void writeBit(boolean bit)
	{
		b = Bits.set(b, pos, bit);
		pos++;
		
		//flush the byte
		if( pos == 8 ) { flush(); }
	}
}
