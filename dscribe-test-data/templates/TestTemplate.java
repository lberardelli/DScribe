package $package$;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.*;

public class OverridenTemplate {
	
	@Template("AssertThrows")
	@Types($state$=EXPR, $exType$=EXCEPTION, $factory$=EXPR, $params$=EXPR_LIST)
	@Test
	public void $method$_When$state$_Throw$exType$() {
	    assertThrows($exType$, () -> $factory$.$method$($params$)); 
	}
	
	@Template("AssertThrows")
	@Types($state$=EXPR, $exType$=EXCEPTION, $factory$=EXPR, $statement$=EXPR, $params$=EXPR_LIST)
	@Test
	public void $method$_When$state$_Throw$exType$WithStatement() {
		$statement$;
		assertThrows($exType$, () -> $factory$.$method$($params$)); 
	}
}
