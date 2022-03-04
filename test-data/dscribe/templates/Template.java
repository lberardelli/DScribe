package $package$;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.cs.jetuml.annotations.DScribeAnnotations.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class Template {	
	
	/**
	 * foo foo foo
	 */
	@Template("ToString")
	@Types($target$=EXPR, $factory$=EXPR)
	@Test
	public void $method$_Test() {
		assertEquals($target$, $factory$.$method$());
	}
	
	/**
	 * Throws $exType$ when $state$
	 */
	@Template("AssertThrows")
	@Types($state$=EXPR, $exType$=EXCEPTION, $factory$=EXPR, $params$=EXPR_LIST)
	@Test
	public void $method$_When$state$_Throw$exType$() {
	    assertThrows($exType$, () -> $factory$.$method$($params$)); 
	}
	
	/**
	 * Throws $exType$ when $state$.
	 */
	@Template("AssertThrowsMessage")
	@Types($state$=EXPR, $exType$=EXCEPTION, $factory$=EXPR, $params$=EXPR_LIST, $message$=EXPR)
	@Test
	public void $method$_When$state$_Throw$exType$() {
		final Exception thrown = assertThrows($exType$, () -> $factory$.$method$($params$));
        assertEquals($message$, thrown.getMessage());
	}
	
	@Template("StatefulReturnsDouble")
	@Types($factory$=EXPR, $params$=EXPR_LIST, $target$=EXPR, $state$=EXPR)
	@Test
	public void test_$method$when$state$() {
		double oracle = Double.valueOf($target$);
		assertEquals(oracle, $factory$.$method$($params$));
	}
	
	@Template("ReturnsDouble")
	@Types($factory$=EXPR, $params$=EXPR_LIST, $target$=EXPR)
	@Test
	public void test_$method$() {
		double oracle = Double.valueOf($target$);
		assertEquals(oracle, $factory$.$method$($params$));
	}
	
	/**
	 * Performs a shallow copy of the object.
	 */
	@Template("ShallowClone")
	@Types($factory$=EXPR)
	@Test
	public void $method$_ReturnShallowCopy() {
		$class$ initial = $factory$;
		$class$ cloned = initial.$method$();
		assertNotSame(initial, cloned);
		assertEquals(initial, cloned);
	}	

	/** Returns $bool$ when $state$. */
	@Template("AssertBool")
	@Types($state$=EXPR, $bool$=EXPR, $factory$=EXPR, $params$=EXPR_LIST)
	@Test
	public void $method$_When$state$_Return$bool$() {
		boolean actual = $factory$.$method$($params$);
		assert$bool$(actual);
	}
	
	/**
	 * Returns true when $trueState$, false when $falseState$
	 */
	@Template("AssertBools")
	@Types($trueState$=EXPR, $falseState$=EXPR, $factory$=EXPR, $trueParams$=EXPR_LIST, $falseParams$=EXPR_LIST)
	@Test
	public void $method$_When$trueState$Then$falseState$() {
		boolean actual = $factory$.$method$($trueParams$);
		boolean fOracle = $factory$.$method$($falseParams$);
		assertTrue(actual);
		assertFalse(fOracle);
	}
	
	/** 
	 * Returns $bool$ when $state$. 
	 */
	@Template("AssertBoolWithMods")
	@Types($state$=EXPR, $bool$=EXPR, $factory$=EXPR, $statement$=EXPR , $params$=EXPR_LIST)
	@Test
	public void $method$_When$state$_Return$bool$() {
		$class$ target = $factory$;
		$statement$;
		boolean oracle = target.$method$($params$);
		assert$bool$(oracle);
	}
	
	/** 
	 * Returns $bool$ when $state$. 
	 */
	@Template("AssertBoolFieldManip")
	@Types($state$=EXPR, $bool$=EXPR, $factory$=EXPR, $fieldName$=EXPR, $statement$=EXPR, $params$=EXPR_LIST)
	@Test
	public void $method$_When$state$_Return$bool$() {
		$statement$;
		boolean actual = $factory$.$method$($params$);
		assert$bool$(actual);
	}
	
	@Template("TestClonePrivateHelper")
	@Types($factory$=EXPR, $statement$=EXPR, $target$ = EXPR)
	@Test
	public void $method$_TestEquals() {
		$class$ oracle = $factory$.$method$();
		$class$ $target$ = oracle.$method$();
		assertTrue($statement$);
		assertNotSame($target$, oracle);
	}
	
	/** 
	 * Returns $expected$ when input is null. 
	 */
	@Template("NullParam")
	@Types($factory$=EXPR, $expected$=EXPR, $returnClass$=EXPR)
	@Test
	public void $method$_WhenInputIsNull_Test() {
		$returnClass$ actual = $factory$.$method$(null);
		assertEquals($expected$, actual);
	}
	
	@Template("AsFactory")
	@Types($factory$=EXPR, $state$=EXPR, $oracle$=EXPR, $verification_method$=EXPR, $params$=EXPR_LIST)
	@Test
	public void $method$_testAsFactory$state$() {
		$class$ res = $factory$.$method$($params$);
		assertEquals($oracle$, res.$verification_method$);
	}
	
	/** 
	 * Asserts equality 
	 */
	@Template("EqualsContract")
	@Types($factory1$=EXPR, $factory2$=EXPR, $factory3$=EXPR)
	@Nested
	public class EqualsContractTest {
		
		@Test	
		public void when$class$sAreSame_ReturnTrue()
		{
			$class$ foo = $factory1$;
			boolean actual = foo.equals(foo);
			assertTrue(actual);
		}	
		
		@Test	
		public void whenAppliedSymmetrically_ReturnSameResult()
		{
			boolean actual1 = $factory1$.equals($factory2$);
			boolean actual2 = $factory2$.equals($factory1$);
			assertEquals(actual1, actual2);
		}
		
		@Test	
		public void when$class$sAreEqual_ReturnTrue() { 
			boolean actual = $factory1$.equals($factory3$);
			assertTrue(actual);
		}

		
		@Test
		public void when$class$sAreDifferent_ReturnFalse() {
			boolean actual = $factory1$.equals($factory2$);
			assertFalse(actual);
		}
		
		@Test	
		public void when$class$IsNull_ReturnFalse ()
		{
			boolean actual = $factory1$.equals(null);
			assertFalse(actual);
		}
	}
}
