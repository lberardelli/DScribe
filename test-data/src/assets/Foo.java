/*
 * How to compile: navigate to DScribe/test-data/src/assets and run myjavac -cp ../ Foo.java -d ../../bin
 * How to run: navigate to DScibr/test-data/bin and run myjava assets.Foo
 * Remaining issues: I need to get these binary assets on the DScribe classpath so that I can load them into the JVM at test time.
 * Unfortunately Eclipse is none too cooperative wrt putting random binaries on the classpath so that's the next step.
 */

package assets;

import annotations.DScribeAnnotations.AssertBool;

public class Foo {
	
	@AssertBool(state = "isEven", factory = "Foo", bool = "False", params = "22")
	public static boolean isOdd(int n) { 
		return n % 2 != 0;
	}

	public static void main(String[] args) {
		System.out.println(isOdd(23));
	}
}
