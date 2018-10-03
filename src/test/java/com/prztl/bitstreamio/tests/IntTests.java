package com.prztl.bitstreamio.tests;

import com.prztl.bitstreamio.BaseInputBitstream;
import com.prztl.bitstreamio.BaseOutputBitstream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

public class IntTests
{
	private static final int maxBits = 32;
	
	@ParameterizedTest
	@MethodSource("bitsSource")
	public void testBits(int bits)
	{
		if(bits < maxBits)
		{
			final long maxValue = Utils.pow2(bits);
			for( int value = 0; value >= 0 && value < maxValue; value++ )
				testValue(value, bits);
		}
		else
		{
			for( int value = Integer.MIN_VALUE; value < Integer.MAX_VALUE; value++ )
				testValue(value, 32);
		}
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
				testValue(5, maxBits+1);
			}
		});
	}
	
	private void testValue(int value, int bits)
	{
		final BaseOutputBitstream out = new BaseOutputBitstream();
		out.writeInt(value, bits);
		final byte[] array = out.toByteArray();
		
		final BaseInputBitstream in = new BaseInputBitstream(array);
		final int actualValue = in.readInt(bits);
		
		Assertions.assertEquals(value, actualValue, "expected: " +value+ ", actualValue: " +actualValue+ ", bits: " +bits);
	}
	
	static IntStream bitsSource()
	{
		return IntStream.range(1, maxBits);
	}
}
