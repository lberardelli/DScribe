/*******************************************************************************
 * Copyright 2020 McGill University
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package ca.mcgill.cs.swevo.dscribe.cli;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;
import ca.mcgill.cs.swevo.dscribe.Context;
import ca.mcgill.cs.swevo.dscribe.DScribe;
import ca.mcgill.cs.swevo.dscribe.cli.CommandLine.Command;
import ca.mcgill.cs.swevo.dscribe.cli.CommandLine.Parameters;
import ca.mcgill.cs.swevo.dscribe.cli.CommandLine.ParentCommand;
import ca.mcgill.cs.swevo.dscribe.generation.test.TestGenerator;
import ca.mcgill.cs.swevo.dscribe.instance.FocalTestPair;
import ca.mcgill.cs.swevo.dscribe.utils.UserMessages;

/**
 * The GenerateTests class generates unit tests for each template invocations in the given focal
 * classes. The generated unit tests are inserted in the test class associated with the
 * corresponding focal class.
 * 
 * @author Alexa
 *
 */
@Command(name = "generateTests", mixinStandardHelpOptions = true)
public class GenerateTests implements Callable<Integer> {

  @ParentCommand
  private DScribe codit;

  @Parameters
  List<String> focalClassNames;

  private final Context context = codit.getContext();

  @Override
  public Integer call() throws URISyntaxException, ReflectiveOperationException {
    // Instantiate a FocalTestPair for each focal class name
    List<FocalTestPair> focalTestPairs = Utils.initFocalClasses(focalClassNames,
        context.classLoader(), context.testClassNameConvention());

    // Generate unit tests for each template invocation in the given focal classes
    var generator = new TestGenerator(focalTestPairs);
    generator.prepare(context);
    generator.loadInvocations();
    List<Exception> errors = generator.generate();

    // Inform user that generation is complete and list any errors
    UserMessages.TestGeneration.isComplete(errors.size());
    errors.forEach(
        e -> UserMessages.TestGeneration.errorOccurred(e.getClass().getName(), e.getMessage()));
    return errors.size();
  }
}
