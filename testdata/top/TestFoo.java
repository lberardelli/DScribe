package top;

import static org.junit.jupiter.api.Assertions.*;
import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.*;

import org.junit.jupiter.api.Test;

public class TestFoo {

    @Test
    public void isOdd_WhenisOddReturnTrueWhenisEvenReturnFalse() {
        boolean actual = Foo.isOdd(23);
        boolean fOracle = Foo.isOdd(22);
        assertTrue(actual);
        assertFalse(fOracle);
    }

    @Test
    public void isEven_WhenisEvenReturnTrueWhenOddReturnFalse() {
        boolean actual = Foo.Bar.isEven(22);
        boolean fOracle = Foo.Bar.isEven(23);
        assertTrue(actual);
        assertFalse(fOracle);
    }
}
