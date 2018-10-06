package com.prztl.bitstreamio.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

public class IntTests
{
	private static final int maxBits = 32;
	private final int runLength = (int)Utils.pow2(20);
	private final TestBitstream stream = Utils.testBitstream(runLength * maxBits);
	
	@ParameterizedTest
	@MethodSource("bitsSource")
	public void testGroupedWrites(int bits)
	{
		//final int runLength = (int)Utils.pow2(20);
		//final TestBitstream stream = new TestBitstream((runLength * bits)/8 + 1);
		
		if(bits < maxBits)
		{
			final long maxValue = Utils.pow2(bits);
			for( int startValue = 0; startValue >= 0 && startValue < maxValue; startValue += runLength )
			{
				final int endValue = (int)Math.min(maxValue, startValue + runLength);
				testRangeOfValues(stream, startValue, endValue, bits);
			}
		}
		else
		{
			for( int startValue = Integer.MIN_VALUE; startValue < Integer.MAX_VALUE; startValue += runLength )
			{
				final int endValue;
				if(Integer.MAX_VALUE - startValue < runLength)
					endValue = Integer.MAX_VALUE;
				else
					endValue = startValue + runLength;
				testRangeOfValues(stream, startValue, endValue,32);
			}
		}
	}
	
	@ParameterizedTest
	@MethodSource("bitsSource")
	public void testIndividualWrites(int bits)
	{
		final TestBitstream stream = new TestBitstream();
		
		if(bits < maxBits)
		{
			final long maxValue = Utils.pow2(bits);
			for( int value = 0; value >= 0 && value < maxValue; value++ )
				testValue(stream, value, bits);
		}
		else
		{
			for( int value = Integer.MIN_VALUE; value < Integer.MAX_VALUE; value++ )
				testValue(stream, value, 32);
		}
	}
	
	@Test
	public void testNegativeBits()
	{
		final TestBitstream stream = new TestBitstream();
		
		Assertions.assertThrows(RuntimeException.class, new Executable() {
			@Override
			public void execute() throws Throwable
			{
				testValue(stream,0,-1);
			}
		});
	}
	
	@Test
	public void testInvalidBits()
	{
		final TestBitstream stream = new TestBitstream();
		
		Assertions.assertThrows(RuntimeException.class, new Executable() {
			@Override
			public void execute() throws Throwable
			{
				testValue(stream,5, maxBits+1);
			}
		});
	}
	
	private void testRangeOfValues(final TestBitstream stream, int startValue, int endValue, int bits)
	{
		//writes
		stream.prepareForWrites();
		for(int value = startValue; value < endValue; value++)
			stream.getOutputBitstream().writeInt(value, bits);
		if(endValue == Integer.MAX_VALUE)
			stream.getOutputBitstream().writeInt(endValue, bits);
		
		stream.prepareForReads();
		for(int expectedValue = startValue; expectedValue < endValue; expectedValue++)
		{
			final int actualValue = stream.getInputBitstream().readInt(bits);
			Assertions.assertEquals(expectedValue, actualValue, "expected: " + expectedValue + ", actualValue: " + actualValue + ", bits: " + bits);
		}
		
		if(endValue == Integer.MAX_VALUE)
		{
			final int actualValue = stream.getInputBitstream().readInt(bits);
			Assertions.assertEquals(Integer.MAX_VALUE, actualValue, "expected: " + Integer.MAX_VALUE + ", actualValue: " + actualValue + ", bits: " + bits);
		}
	}
	
	private void testValue(final TestBitstream stream, int value, int bits)
	{
		stream.prepareForWrites();
		stream.getOutputBitstream().writeInt(value, bits);
		
		stream.prepareForReads();
		final int actualValue = stream.getInputBitstream().readInt(bits);
		Assertions.assertEquals(value, actualValue, "expected: " + value + ", actualValue: " + actualValue + ", bits: " + bits);
	}
	
	static IntStream bitsSource()
	{
		return IntStream.range(1, maxBits);
	}
}
