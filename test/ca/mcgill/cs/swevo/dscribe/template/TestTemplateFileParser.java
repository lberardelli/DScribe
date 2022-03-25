package ca.mcgill.cs.swevo.dscribe.template;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.github.javaparser.ast.CompilationUnit;

import ca.mcgill.cs.swevo.dscribe.setup.Setup;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestTemplateFileParser {
	//LBERAR: TODO test this out with bad template files.

	private void addTemplate(Template template) {
	}
	
	private TemplateFileParser fileParser;
	private CompilationUnit cu;
	
	@BeforeAll
	public void parse() throws FileNotFoundException {
		cu = Setup.parse(System.getProperty("user.dir") + "/dscribe-test-data/templates/TestTemplate.java");
	}
	
	@BeforeEach
	public void setup() {
		fileParser = new TemplateFileParser(this::addTemplate);
	}
	
	@Test
	public void visitClassOrInterface_FindsClassName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		fileParser.visit(cu.getClassByName("OverridenTemplate").get(), cu.getImports());
		
		Class<?> clazz = fileParser.getClass();
		Field className = clazz.getDeclaredField("className");
		className.setAccessible(true);
		String name = (String) className.get(fileParser);
		
		assertEquals(name, "OverridenTemplate");
	}
	
	@Test
	public void visitPackageDeclaration_FindsPackageName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		fileParser.visit(cu.getPackageDeclaration().get(), cu.getImports());
		
		Class<?> clazz = fileParser.getClass();
		Field packageName = clazz.getDeclaredField("packageName");
		packageName.setAccessible(true);
		String name = (String) packageName.get(fileParser);
		
		assertEquals(name, "$package$");
	}

}
