package com.prztl.bitstreamio.tests;

import com.prztl.bitstreamio.BaseInputBitstream;
import com.prztl.bitstreamio.BaseOutputBitstream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BooleanTests
{
	@Test
	public void testBits()
	{
		testValue(true);
		testValue(false);
	}
	
	private void testValue(boolean value)
	{
		final BaseOutputBitstream out = new BaseOutputBitstream();
		out.writeBoolean(value);
		final byte[] array = out.toByteArray();
		
		final BaseInputBitstream in = new BaseInputBitstream(array);
		final boolean actualValue = in.readBoolean();
		
		Assertions.assertEquals(value, actualValue);
	}
}
