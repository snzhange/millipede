package org.animeaholic.millipede.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class JDKTimeTest {

	public JDKTimeTest() {
	}
	
	@Test
	public void testCase() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = f.format(new Date());
		System.out.println(now);
	}

}
