package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class DScribeAnnotations {
	
	// AssertThrows Template
	@Repeatable(AssertThrowsList.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface AssertThrows {
		public String state();

		Class<?> exType();

		public String factory();

		public String[] params() default {};

		public String uut() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface AssertThrowsList {
		AssertThrows[] value();
	}
	
	// StatefulReturnsDouble Template
	@Repeatable(StatefulReturnsDoubleList.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface StatefulReturnsDouble {
		String factory();
		
		String[] params();
		
		String target();
		
		String state();
		
		public String uut() default "";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface StatefulReturnsDoubleList {
		StatefulReturnsDouble[] value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface AsFactory {
		String factory();
		
		String state() default "";
		
		String oracle();
		
		String verification_method();
		
		String[] params() default {};
		
		String uut() default "";
	}
	
	// ReturnsDouble Template
	@Repeatable(ReturnsDoubleList.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface ReturnsDouble {
		String factory();
		
		String[] params();
		
		String target();
		
		public String uut() default "";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface ReturnsDoubleList {
		ReturnsDouble[] value();
	}

	// AssertThrowsMessage Template
	@Repeatable(AssertThrowsMessageList.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface AssertThrowsMessage {
		public String state();

		public Class<?> exType();

		public String factory();

		public String[] params() default {};

		public String message();

		public String uut() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface AssertThrowsMessageList {
		AssertThrowsMessage[] value();
	}

	// ShallowClone Template
	@Repeatable(ShallowCloneList.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface ShallowClone {
		public String factory();

		public String uut() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface ShallowCloneList {
		ShallowClone[] value();
	}

	// AssertBool Template
	@Repeatable(AssertBoolList.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface AssertBool {
		public String state();

		public String bool();

		public String factory();

		public String[] params() default {};

		public String uut() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface AssertBoolList {
		AssertBool[] value();
	}

	// EqualsContract Template
	@Repeatable(EqualsContractList.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface EqualsContract {
		public String factory1();

		public String factory2();

		public String factory3();

		public String uut() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	public @interface EqualsContractList {
		EqualsContract[] value();
	}
	
	//ToString Template
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface ToString {
		public String factory();
		
		public String target();
		
		public String uut() default "";
	}
	
	//AssertBools Template
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface AssertBools {
		public String factory();
		
		public String trueState();
		
		public String falseState();
		
		public String[] trueParams();
		
		public String[] falseParams();
		
		public String uut() default "";
	}
	
	//NullParam Template
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface NullParam {
		public String factory();
		
		public String expected();
		
		public String returnClass();

		public String uut() default "";
	}
	
	//TestClonePrivateHelper Template
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface TestClonePrivateHelper {
		public String factory();
		
		public String statement();
		
		public String target();
		
		public String uut() default "";
	}
	
	//AssertBoolWithMods template
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface AssertBoolWithMods {
		public String factory();
		
		public String statement();
		
		public String bool();
		
		public String state();
		
		public String[] params();
		
		public String uut() default "";
	}
	
	//AssertBoolFieldManip template
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.TYPE, ElementType.METHOD})
		public @interface AssertBoolFieldManip {
			public String factory();
			
			public String statement();
			
			public String bool();
			
			public String state();
			
			public String[] params();
			
			public String fieldName();
			
			public String uut() default "";
		}
}
