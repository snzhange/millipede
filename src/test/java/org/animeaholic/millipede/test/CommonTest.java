package org.animeaholic.millipede.test;

public class CommonTest {

	public static void main(String[] args) {
		System.out.println(CommonTest.class.getClassLoader().getResource("log4j.properties").getPath());
	}

}
