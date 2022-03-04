package ca.mcgill.cs.swevo.dscribe.model;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.*;
import java.nio.file.Path;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import ca.mcgill.cs.swevo.dscribe.Context;
import ca.mcgill.cs.swevo.dscribe.TestDataLoader;

public class TestFocalTestPairFactory
{
	@BeforeAll
	public void setupTestData() {
		TestDataLoader.load();
	}
	
	private FocalTestPairFactory factory;
	
	@BeforeEach
	public void setupFactor() {
		factory = new FocalTestPairFactory(Context.instance());
	}
	
	@Test
	public void Test_getPathToClass() throws SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		factory = new FocalTestPairFactory(Context.instance());
		Method method = factory.getClass().getDeclaredMethod("getPathToClass", String.class, String.class);
		method.setAccessible(true);
		Path path = (Path) method.invoke(factory, "dummy.Foo", "test");
		assertEquals(path.toString(), "/Users/lawrenceberardelli/Documents/courses/COMP303/eclipse-workspace/DScribeTestData/test/dummy/Foo.java");
	}
}
