package ca.mcgill.cs.swevo.dscribe.template;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestTemplateFileParser {

	private void addTemplate(Template template) {
		if (templateMethods.containsKey(template.getName())) {
			List<Template> newList = templateMethods.get(template.getName());
			newList.add(template);
			templateMethods.put(template.getName(), newList);
		} else {
			List<Template> newList = new ArrayList<>();
			newList.add(template);
			templateMethods.put(template.getName(), newList);
		}
	}
	
	private final Map<String, List<Template>> templateMethods = new HashMap<>();
	
	TemplateFileParser fileParser;
	
	CompilationUnit cu;
	
	@BeforeAll
	public void parse() throws FileNotFoundException {
		JavaParser parser = new JavaParser(new ParserConfiguration());
		File file = new File(System.getProperty("user.dir") + "/dscribe-test-data/templates/TestTemplate.java");
		ParseResult<CompilationUnit> result = parser.parse(file);
		cu = result.getResult().get();
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
	public void visitClassOrInterface_FindsImports() {
		List<String> imports = TestInMemoryTemplateRepository.Target.importNames;
	}
	
	@Test
	public void visitPackageDecleration_FindsPackageName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		fileParser.visit(cu.getPackageDeclaration().get(), cu.getImports());
		
		Class<?> clazz = fileParser.getClass();
		Field packageName = clazz.getDeclaredField("packageName");
		packageName.setAccessible(true);
		String name = (String) packageName.get(fileParser);
		
		assertEquals(name, "$package$");
	}

}
