import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NoNameTemplate {
	
	@Template
	@Types($state$=EXPR, $exType$=EXCEPTION, $factory$=EXPR, $params$=EXPR_LIST)
	@Test
	public void $method$_When$state$_Throw$exType$() {
	    assertThrows($exType$, () -> $factory$.$method$($params$)); 
	}

}
