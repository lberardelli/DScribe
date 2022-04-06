package ca.mcgill.cs.swevo.dscribe.model;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;

import ca.mcgill.cs.swevo.dscribe.Context;
import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.*;
import ca.mcgill.cs.swevo.dscribe.setup.Setup;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestFocalClass {

    private FocalClass focalClass;

    private Context context;

    private Path getPathToClass(String className, String targetFolder) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	return Setup.getPathToClass(context, className, targetFolder);
    }

    @BeforeAll
    public void setupContext() {
        context = Setup.setupContext();
    }

    @BeforeEach
    public void setupFocalClass() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        focalClass = new FocalClass(getPathToClass("top.HasNestedClass", context.sourceFolder()));
        focalClass.parseCompilationUnit(new JavaParser(new ParserConfiguration()));
    }

    @Test
    @AssertThrows(exType = java.lang.IllegalStateException.class, factory = "focalClass", state = "MethodDNE", params = "new FocalMethod(\"foo\", null)", uut = "getMethodDeclaration(FocalMethod)")
    public void getMethodDeclaration_WhenMethodDNE_ThrowIllegalStateException() {
    	assertThrows(java.lang.IllegalStateException.class, () -> focalClass.getMethodDeclaration(new FocalMethod("foo", null)));
    }
}
