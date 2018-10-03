package com.prztl.bitstreamio.tests;

import com.prztl.bitstreamio.BaseInputBitstream;
import com.prztl.bitstreamio.BaseOutputBitstream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

public class ByteTests
{
	private static final int maxBits = 8;
	
	@ParameterizedTest
	@MethodSource("bitsSource")
	public void testBits(int bits)
	{
		if(bits < maxBits)
		{
			final long maxValue = Utils.pow2(bits);
			for( byte value = 0; value >= 0 && value < maxValue; value++ )
				testValue(value, bits);
		}
		else
		{
			for( byte value = Byte.MIN_VALUE; value < Byte.MAX_VALUE; value++ )
				testValue(value, 8);
		}
	}
	
	@Test
	public void testNegativeBits()
	{
		Assertions.assertThrows(RuntimeException.class, new Executable() {
			@Override
			public void execute() throws Throwable
			{
				testValue((byte)0, -1);
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
				testValue((byte)5, maxBits+1);
			}
		});
	}
	
	private void testValue(byte value, int bits)
	{
		final BaseOutputBitstream out = new BaseOutputBitstream();
		out.writeByte(value, bits);
		final byte[] array = out.toByteArray();
		
		final BaseInputBitstream in = new BaseInputBitstream(array);
		final byte actualValue = in.readByte(bits);
		
		Assertions.assertEquals(value, actualValue, "expected: " +value+ ", actualValue: " +actualValue+ ", bits: " +bits);
	}
	
	static IntStream bitsSource()
	{
		return IntStream.range(1, maxBits);
	}
}
