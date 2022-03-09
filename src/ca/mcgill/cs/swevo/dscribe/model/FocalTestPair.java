package ca.mcgill.cs.swevo.dscribe.model;

import java.util.Iterator;
import java.util.List;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import ca.mcgill.cs.swevo.dscribe.template.TemplateRepository;
import ca.mcgill.cs.swevo.dscribe.template.invocation.FocalTemplateInvocationExtractor;
import ca.mcgill.cs.swevo.dscribe.template.invocation.InstanceContext;
import ca.mcgill.cs.swevo.dscribe.template.invocation.TemplateInvocation;
import ca.mcgill.cs.swevo.dscribe.template.invocation.TestTemplateInvocationExtractor;
import ca.mcgill.cs.swevo.dscribe.utils.exceptions.GenerationException;

/**
 * A FocalTestPair represents links a focal class to its corresponding test class.
 *
 * @author Alexa
 */
public class FocalTestPair implements Parseable {

    private final FocalClass focalClass;

    private final TestClass testClass;

    public FocalTestPair(FocalClass focalClass, TestClass testClass) {
        assert focalClass != null && testClass != null;
        this.focalClass = focalClass;
        this.testClass = testClass;
    }

    /**
     * Parse both the focal and test classes and store the resulting compilation units
     *
     * @param parser
     *            the JavaParser instance to use for parsing
     * @throws GenerationException
     *             if there is a parsing error or the either path cannot be accessed
     */
    @Override
    public void parseCompilationUnit(JavaParser parser) {
        focalClass.parseCompilationUnit(parser);
        testClass.parseCompilationUnit(parser);
    }

    @Override
    public boolean writeToFile(List<Exception> exceptions) {
        boolean success = testClass.writeToFile(exceptions);
        if (success) {
            success = focalClass.writeToFile(exceptions);
            return success;
        }
        return false;
    }

    /**
     * Extract all template invocations (i.e., DScribe Annotations) from the focal and test classes
     */
    public void extractTemplateInvocations(TemplateRepository templateRepo) {
        assert focalClass.compilationUnit() != null && testClass.compilationUnit() != null;
        var srcInstantiator = new FocalTemplateInvocationExtractor(templateRepo);
        var testInstantiator = new TestTemplateInvocationExtractor(templateRepo);
        srcInstantiator.visit(focalClass.compilationUnit(), focalClass);
        testInstantiator.visit(testClass.compilationUnit(), focalClass);
    }

    public void validateTemplateInvocations(TemplateRepository templateRepository) {
        for (FocalMethod testMethod : focalClass) {
            Iterator<TemplateInvocation> iterator = testMethod.iterator();
            CompilationUnit cu = focalClass.compilationUnit();
            String packageName = cu.getPackageDeclaration().get().getNameAsString();
            InstanceContext context = new InstanceContext(packageName);
            while (iterator.hasNext()) {
                TemplateInvocation next = iterator.next();
                next.setContext(context);
                if (!next.validate(templateRepository)) {
                    iterator.remove();
                }
            }
        }
    }

    public FocalClass focalClass() {
        return focalClass;
    }

    public TestClass testClass() {
        return testClass;
    }
}
