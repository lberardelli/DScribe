package ca.mcgill.cs.swevo.dscribe.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.github.javaparser.ast.ImportDeclaration;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class TestInMemoryTemplateRepository {
	
	private InMemoryTemplateRepository repository;
	
	void addTemplate(Template template) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		Class<?> repoClass = repository.getClass();
		Method addTemplateMethod = repoClass.getDeclaredMethod("addTemplate", Template.class);
		addTemplateMethod.setAccessible(true);
		addTemplateMethod.invoke(repository, template);
	}
	
	class Target {
	
		static List<String> templateNames = Arrays.asList(new String[] { "AssertThrows" });
		
		/*
		 * Oracles for found placeholders test
		 */
		static List<ArrayList<Placeholder>> getPlaceholderPairs() {
			List<ArrayList<Placeholder>> ret = new ArrayList<ArrayList<Placeholder>>();
			ArrayList<Placeholder> templatePlaceholders = new ArrayList<Placeholder>();
			
			templatePlaceholders.add(new Placeholder("$state$", PlaceholderType.EXPR));
			templatePlaceholders.add(new Placeholder("$exType$", PlaceholderType.EXCEPTION));
			templatePlaceholders.add(new Placeholder("$factory$", PlaceholderType.EXPR));
			templatePlaceholders.add(new Placeholder("$params$", PlaceholderType.EXPR_LIST));
			ret.add(templatePlaceholders);
			
			templatePlaceholders = new ArrayList<Placeholder>();
			templatePlaceholders.add(new Placeholder("$state$", PlaceholderType.EXPR));
			templatePlaceholders.add(new Placeholder("$exType$", PlaceholderType.EXCEPTION));
			templatePlaceholders.add(new Placeholder("$factory$", PlaceholderType.EXPR));
			templatePlaceholders.add(new Placeholder("$params$", PlaceholderType.EXPR_LIST));
			templatePlaceholders.add(new Placeholder("$statement$", PlaceholderType.EXPR));
			ret.add(templatePlaceholders);
			
			return ret;
		}
		
		/*
		 * oracles for found imports test
		 */
		static List<String> importNames = Arrays.asList(new String[] {
				"import static org.junit.jupiter.api.Assertions.*;\n",
				"import org.junit.jupiter.api.Test;\n", 
				"import ca.mcgill.cs.swevo.dscribe.annotations.DScribeAnnotations.*;\n"
				});
	}
	
	@Nested
	public class OverridenTemplateDefinitions {
		
		@BeforeEach
		public void setup() {
			repository = new InMemoryTemplateRepository(System.getProperty("user.dir") + "/dscribe-test-data/templates");
		}
		
		@Test
		public void InMemoryTemplateRepository_findsAllTemplateSkeletons() {
			Target.templateNames.stream().forEach(name -> assertTrue(repository.contains(name)));
			assertEquals(2,repository.get(Target.templateNames.get(0)).size());
		}
		
		@Test
		public void InMemoryTemplateRepository_findsPlaceholders() {
			List<ArrayList<Placeholder>> targetPlaceholders = new ArrayList<ArrayList<Placeholder>>(Target.getPlaceholderPairs());
			
			//Aggregate found placeholders.
			List<Template> foundTemplates = repository.get(Target.templateNames.get(0));
			List<ArrayList<Placeholder>> foundPlaceholders = new ArrayList<ArrayList<Placeholder>>();
			for (int i = 0; i < foundTemplates.size(); ++i) {
				ArrayList<Placeholder> totient = new ArrayList<Placeholder>();
				for (Placeholder ph : foundTemplates.get(i)) {
					if (ph != null)
						totient.add(ph);
				}
				foundPlaceholders.add(totient);
			}
			
			assertEquals(targetPlaceholders.size(), foundPlaceholders.size());
			
			//assert whether the parsed placeholders match the name and type of what was expected
			for (int i = 0; i < targetPlaceholders.size(); ++i) {
				Set<Placeholder> target = new HashSet<Placeholder>(targetPlaceholders.get(i));
				Set<Placeholder> found = new HashSet<Placeholder>(foundPlaceholders.get(i));
				Set<String> tgtname = new HashSet<String>(target.stream().map(ph -> ph.getName()).toList());
				Set<String> fndName = new HashSet<String>(found.stream().map(ph -> ph.getName()).toList());
				Set<PlaceholderType> tgtType = new HashSet<PlaceholderType>(target.stream().map(ph -> ph.getType()).toList());
				Set<PlaceholderType> fndType = tgtType = new HashSet<PlaceholderType>(found.stream().map(ph -> ph.getType()).toList());
				assertEquals(tgtname, fndName);
				assertEquals(tgtType, fndType);
			}
		}
		
		@Test
		public void InMemoryTemplateRepository_findsAllImportStatements() {
			List<ImportDeclaration> foundDecls = repository.get(null).get(0).getNecessaryImports();
			List<String> foundImports =  foundDecls.stream().map(im -> im.toString()).toList();
			assertEquals(new HashSet<String>(Target.importNames), new HashSet<String>(foundImports));
		}
		
		@Test
		public void InMemortTemplateRepository_findsPackageDecleration() {
			assertEquals(repository.get(null).get(0).getPackageName(), "$package$");
		}
	}
	
	@Nested
	public class AttachedJavaDocs {
		
		@BeforeEach
		public void setup() {
			repository = new InMemoryTemplateRepository(System.getProperty("user.dir") + "/dscribe-test-data/javadocs_template");
		}
		
		@Test
		public void InMemoryTemplateRepository_findsJavaDocs() {
			List<Template> templates = repository.get(Target.templateNames.get(0));
			Template hasDocs = templates.get(0);
			Template noDocs = templates.get(1);
			assertTrue(hasDocs.getDocFactory().isPresent());
			assertFalse(noDocs.getDocFactory().isPresent());
		}
	}

}
