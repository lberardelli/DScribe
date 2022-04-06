package ca.mcgill.cs.swevo.dscribe.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;

import ca.mcgill.cs.swevo.dscribe.setup.Setup;
import ca.mcgill.cs.swevo.dscribe.utils.exceptions.RepositoryException;


public class TestTemplateFileParser {
	//LBERAR: TODO test this out with bad template files.
	
	List<Template> templateList;

	private void addTemplate(Template template) {
		templateList.add(template);
	}
	
	private TemplateFileParser fileParser;
	private CompilationUnit cu;
	
	public void parse(String templatePath) throws FileNotFoundException {
		cu = Setup.parse(System.getProperty("user.dir") + templatePath);
	}
	
	@BeforeEach
	void setup() {
		fileParser = new TemplateFileParser(this::addTemplate);
		templateList = new ArrayList<Template>();
	}
	
	@Test
	void test_VisitClassOrInterface_FindsClassName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, FileNotFoundException {
		parse("/dscribe-test-data/valid_templates/TestTemplate.java");
		fileParser.visit(cu.getClassByName("ValidTemplates").get(), cu.getImports());
		
		Class<?> clazz = fileParser.getClass();
		Field className = clazz.getDeclaredField("className");
		className.setAccessible(true);
		String name = (String) className.get(fileParser);
		
		assertEquals(name, "NestedClassWithJavadoc");
	}
	
	@Test
	void test_VisitPackageDeclaration_FindsPackageName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, FileNotFoundException {
		parse("/dscribe-test-data/valid_templates/TestTemplate.java");
		fileParser.visit(cu.getPackageDeclaration().get(), cu.getImports());
		Class<?> clazz = fileParser.getClass();
		Field packageName = clazz.getDeclaredField("packageName");
		packageName.setAccessible(true);
		String name = (String) packageName.get(fileParser);
		
		assertEquals(name, "$package$");
	}
	
	@Test
	void test_TemplateFileParser_IgnoreNamelessTemplate() throws FileNotFoundException {
		parse("/dscribe-test-data/bad_templates/TestTemplate.java");
		fileParser.visit(cu.getClassByName("BadTemplates").get().getMethodsByName("noNameTemplate").get(0), null);
		assertTrue(templateList.size()==0);
	}
	
	@Test
	void test_TemplateFileParser_ThrowsRepositoryExceptionWhenSingleTemplateAnnotation() throws FileNotFoundException {
		parse("/dscribe-test-data/bad_templates/TestTemplate.java");
		assertThrows(RepositoryException.class, 
				() -> fileParser.visit(cu.getClassByName("BadTemplates").get().getMethodsByName("singleAnnotationTemplateDecleration").get(0), null));
	}
	
	@Test
	void test_TemplateFileParser_AggregatesDocFactoryWhenJavadocPresent() throws FileNotFoundException {
		parse("/dscribe-test-data/valid_templates/TestTemplate.java");
		fileParser.visit(cu.getClassByName("ValidTemplates").get().getMethodsByName("hasJavadoc").get(0), new ArrayList<ImportDeclaration>());
		assertTrue(templateList.get(0).getDocFactory().isPresent());
	}
	
	@Test
	void test_TemplateFileParser_AggregatesJavadocOnClassLevelTemplate() throws FileNotFoundException {
		parse("/dscribe-test-data/valid_templates/TestTemplate.java");
		fileParser.visit(cu.getClassByName("ValidTemplates").get(), new ArrayList<ImportDeclaration>());
		for (Template template : templateList) {
			if (template.getName() != null && template.getName().equals("ClassLevelWithJavadoc")) {
				assertTrue(templateList.get(0).getDocFactory().isPresent());
			}
		}
	}

}
