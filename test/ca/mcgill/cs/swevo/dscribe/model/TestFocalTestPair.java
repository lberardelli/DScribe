package ca.mcgill.cs.swevo.dscribe.model;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import ca.mcgill.cs.swevo.dscribe.Context;
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
    	
    	assertEquals(new HashSet<String>(focalMethods), new HashSet<String>(Util.getTargetMethodStrings()));
    	
    }
    
    @Test
    public void extractTemplateInvocations_FindsDScribeAnnotations() {
    	focalTestPair.extractTemplateInvocations(templateRepo);
    	assertEquals(new HashSet<String>(Util.collectExtractedAnnotations(this)), new HashSet<String>(Util.getTargetAnnotationStrings()));
    }
    
    /*
     * TODO: should be a similar and doable with the testVisitor aswell. 
     */
}
