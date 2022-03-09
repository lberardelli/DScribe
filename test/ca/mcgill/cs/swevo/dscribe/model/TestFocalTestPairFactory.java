package ca.mcgill.cs.swevo.dscribe.model;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.*;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ca.mcgill.cs.swevo.dscribe.Context;
import ca.mcgill.cs.swevo.dscribe.setup.*;
import static org.junit.jupiter.api.Assertions.*;
import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.*;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestFocalTestPairFactory {

    private FocalTestPairFactory factory;

    private Context context;

    @BeforeEach
    public void setupFactory() {
        factory = new FocalTestPairFactory(context);
    }

    @BeforeAll
    public void setupTestData() {
        context = Setup.setupContext();
    }

    @Test
    public void Test_getPathToClass() throws SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = factory.getClass().getDeclaredMethod("getPathToClass", String.class, String.class);
        method.setAccessible(true);
        Path path = (Path) method.invoke(factory, "top.Foo", context.sourceFolder());
        assertEquals(path.toString(), System.getProperty("user.dir") + "/" + context.sourceFolder() + "/top/Foo.java");
    }

    @Test
    @ReturnNull(factory = "new FocalTestPairFactory(context)", params = { "\"foo\"" }, state = "ClassDNE", uut = "get(String)")
    public void get_returnsNullWhenClassDNE() {
        assertSame(new FocalTestPairFactory(context).get("foo"), null);
    }
}
