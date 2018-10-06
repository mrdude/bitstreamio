package com.prztl.bitstreamio.tests;

import com.prztl.bitstreamio.AbstractInputBitstream;
import com.prztl.bitstreamio.AbstractOutputBitstream;
import com.prztl.bitstreamio.Bits;

import java.util.Arrays;

public class TestBitstream
{
	private final IBitstream in = new IBitstream();
	private final OBitstream out = new OBitstream();
	
	private byte[] data;
	private int bitLength = 0;
	
	public TestBitstream(int initialLength)
	{
		data = new byte[initialLength];
	}
	
	public TestBitstream()
	{
		this(1024);
	}
	
	private void extendDataArray()
	{
		data = Arrays.copyOf(data, data.length*2);
	}
	
	public void prepareForWrites()
	{
		out.pos = 0;
		bitLength = 0;
	}
	
	public void prepareForReads()
	{
		in.pos = 0;
	}
	
	public AbstractInputBitstream getInputBitstream() { return in; }
	public AbstractOutputBitstream getOutputBitstream() { return out; }
	
	private class IBitstream extends AbstractInputBitstream
	{
		private int pos = 0;
		
		@Override
		protected boolean readBit()
		{
			if(pos >= bitLength)
				return false;
			
			final int bytePos = pos / 8;
			final int bitPos = pos % 8;
			pos++;
			
			if(bytePos >= data.length)
				return false;
			
			return Bits.get(data[bytePos], bitPos);
		}
	}
	
	private class OBitstream extends AbstractOutputBitstream
	{
		private int pos = 0;
		
		@Override
		protected void writeBit(boolean bit)
		{
			final int bytePos = pos / 8;
			final int bitPos = pos % 8;
			
			if(bytePos >= data.length)
				extendDataArray();
			
			data[bytePos] = Bits.set(data[bytePos], bitPos, bit);
			pos++;
			bitLength++;
		}
	}
}
