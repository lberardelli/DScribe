package ca.mcgill.cs.swevo.dscribe.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.AssertThrows;

public class TestTypeNameResolver {

    @Test
    @AssertThrows(exType = java.lang.ClassNotFoundException.class, factory = "TypeNameResolver", state = "DNE", params = "\"foo\"", uut = "resolve(String)")
    public void resolve_WhenDNE_ThrowClassNotFoundException() {
        assertThrows(java.lang.ClassNotFoundException.class, () -> TypeNameResolver.resolve("foo"));
    }
}
