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
	private ByteArrayInputStream in;
	private byte b;
	private int pos = 8;
	
	public InputBitstream(ByteArrayInputStream in)
	{
		this.in = in;
	}
	public InputBitstream(byte[] b, int off, int len) { this( new ByteArrayInputStream( b, off, len ) ); }
	public InputBitstream(byte[] b) { this( new ByteArrayInputStream(b) ); }
	
	@Override
	protected boolean readBit()
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
}
