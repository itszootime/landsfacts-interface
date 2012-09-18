package org.uncertweb.landsfacts.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Ignore;

@Ignore
public class TestHelper {

	public static void assertFileEquals(File expected, File actual) {
		try {
			String s1 = readFile(expected);
			String s2 = readFile(actual);
			Assert.assertEquals(s1, s2);
		}
		catch (IOException e) {
			Assert.fail();
		}
	}
	
	private static String readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
}
