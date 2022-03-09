package ca.mcgill.cs.swevo.dscribe.model;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import ca.mcgill.cs.swevo.dscribe.Context;
import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.AssertBools;
import ca.mcgill.cs.swevo.dscribe.setup.Setup;
import ca.mcgill.cs.swevo.dscribe.template.InMemoryTemplateRepository;
import ca.mcgill.cs.swevo.dscribe.template.TemplateRepository;
import ca.mcgill.cs.swevo.dscribe.template.invocation.TemplateInvocation;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestFocalTestPair {

    private Context context;
    
    private TemplateRepository templateRepo;
    
    private FocalTestPair focalTestPair;
    
    private static class Util {
    
	    private static List<String> getTargetMethodStrings() {
	    	return Arrays.asList("isOdd(int)", "isEven(int)", "main(String[])");
	    }
	    
	    private static List<String> getTargetAnnotationStrings() {
	    	return Arrays.asList("@AssertBools(factory = \"Foo\", falseParams = { \"22\" }, "
	    			+ "falseState = \"isEven\", trueParams = { \"23\" }, trueState = \"isOdd\", uut = \"isOdd(int)\")",
	    			
	    			"@AssertBools(factory = \"Foo.Bar\", falseParams = { \"23\" }, falseState = \"Odd\", "
	    			+ "trueParams = { \"22\" }, trueState = \"isEven\", uut = \"isEven(int)\")");
	    }
	    
	    private static List<String> collectExtractedAnnotations(TestFocalTestPair outer) {
	    	List<String> ret = new ArrayList<String>();
	    	for (FocalMethod method : outer.focalTestPair.focalClass()) {
	    		for (TemplateInvocation invocation : method) {
	    			ret.add(invocation.getAnnotationExpr().toString());
	    		}
	    	}
	    	return ret;
	    }
	    
	    private static boolean assertContains(List<String> generatedNames, List<String> targetNames) {
	    	for (String methodName : generatedNames) {
	    		if (!targetNames.contains(methodName)) {
	    			return false;
	    		}
	    	}
	    	for (String methodName : targetNames) {
	    		if (!generatedNames.contains(methodName)) {
	    			return false;
	    		}
	    	}
	    	return true;
	    }
    }

    @BeforeAll
    public void setupContext() {
        context = Setup.setupContext();
        templateRepo = new InMemoryTemplateRepository(context.templateRepositoryPath());
    }

    @BeforeEach
    public void setupFocalTestPair() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        focalTestPair = new FocalTestPair(new FocalClass(Setup.getPathToClass(context, "top.Foo", context.sourceFolder())),
        		new TestClass(Setup.getPathToClass(context, "top.TestFoo", context.testFolder())));
        focalTestPair.parseCompilationUnit(new JavaParser(new ParserConfiguration()));
    }
    
    @Test
    public void extractTemplateInvocations_FindsFocalMethods() {
    	focalTestPair.extractTemplateInvocations(templateRepo);
    	List<String> focalMethods = focalTestPair.focalClass().getMethods()
    			.stream()
    			.map(fm -> fm.getSignature()).toList();
    	assertTrue(Util.assertContains(focalMethods, Util.getTargetMethodStrings()));
    }
    
    @Test
    public void extractTemplateInvocations_FindsDScribeAnnotations() {
    	focalTestPair.extractTemplateInvocations(templateRepo);
    	assertTrue(Util.assertContains(Util.collectExtractedAnnotations(this), Util.getTargetAnnotationStrings()));
    }
    
    /*
     * TODO: should be a similar and doable with the testVisitor aswell. 
     */
}
