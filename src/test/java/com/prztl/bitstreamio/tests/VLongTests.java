package com.prztl.bitstreamio.tests;

import com.prztl.bitstreamio.BaseInputBitstream;
import com.prztl.bitstreamio.BaseOutputBitstream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

public class VLongTests
{
	private static final int maxBits = 64;
	
	@ParameterizedTest
	@MethodSource("bitsSource")
	public void testBits(int groupSize)
	{
		for( long value = Long.MIN_VALUE; value < Long.MAX_VALUE; value++ )
			testValue(value, groupSize);
	}
	
	private void testValue(long value, int groupSize)
	{
		System.out.println("value = " +value+ ", groupSize = " +groupSize);
		final BaseOutputBitstream out = new BaseOutputBitstream();
		out.writeVLong(value, groupSize);
		final byte[] array = out.toByteArray();
		
		final BaseInputBitstream in = new BaseInputBitstream(array);
		final long actualValue = in.readVLong(groupSize);
		
		Assertions.assertEquals(value, actualValue, "expected: " +value+ ", actualValue: " +actualValue+ ", groupSize: " +groupSize);
	}
	
	static IntStream bitsSource()
	{
		return IntStream.range(1, maxBits);
	}
}
