package com.prztl.bitstreamio.tests;

import com.prztl.bitstreamio.BaseInputBitstream;
import com.prztl.bitstreamio.BaseOutputBitstream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class LongTests
{
	@Test
	public void testBits()
	{
		for(int bits = 1; bits < 63; bits++)
		{
			final long maxValue = Utils.pow2(bits);
			for( long value = 0; value < maxValue; value++ )
				testValue(value, bits);
		}
		
		for(long value = Long.MIN_VALUE; value < Long.MAX_VALUE; value++)
			testValue(value, 64);
	}
	
	@Test
	public void testNegativeBits()
	{
		Assertions.assertThrows(RuntimeException.class, new Executable() {
			@Override
			public void execute() throws Throwable
			{
				testValue(0, -1);
			}
		});
	}
	
	@Test
	public void testInvalidBits()
	{
		Assertions.assertThrows(RuntimeException.class, new Executable() {
			@Override
			public void execute() throws Throwable
			{
				testValue(5, 65);
			}
		});
	}
	
	private void testValue(long value, int bits)
	{
		final BaseOutputBitstream out = new BaseOutputBitstream();
		out.writeLong(value, bits);
		final byte[] array = out.toByteArray();
		
		final BaseInputBitstream in = new BaseInputBitstream(array);
		final long actualValue = in.readLong(bits);
		
		Assertions.assertEquals(value, actualValue, "expected: " +value+ ", actualValue: " +actualValue+ ", bits: " +bits);
	}
}
