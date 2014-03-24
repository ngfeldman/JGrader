package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.ArrayList;

import automata.mealy.MealyConfiguration;
import automata.mealy.MealyMachine;
import automata.mealy.MealyNondeterminismDetector;
import automata.mealy.MealyStepByStateSimulator;
import file.ParseException;
import file.XMLCodec;

public class FSTResult extends TestSuiteResult<String> {
	private boolean missing = true;
	private boolean testable = false;
	protected boolean wrong_type;
	
	protected int actual_states = 0;
	
	protected String feedback_prelude = "";
	protected MealyMachine automaton;
	protected MealyStepByStateSimulator sim;
	
	public FSTResult(FSTGrader problem_grader, File file) {
		super(problem_grader, file);
		if (file == null) {
			return;
		}
		missing = false;
		XMLCodec codec = new XMLCodec();
		Object jff_obj = null;
		try {
			jff_obj = codec.decode(file, null);
		} catch (ParseException e) {
			feedback_prelude += "\\n\t not generated properly. "
					+ "This is likely due to a problem in your Haskell code.";
			return;
		}
		
		if (jff_obj instanceof MealyMachine) {
			testable = true;
			automaton = (MealyMachine) jff_obj;
			actual_states = automaton.getStates().length;
			MealyNondeterminismDetector mnd = new MealyNondeterminismDetector();
			if (mnd.getNondeterministicStates(automaton).length > 0) {
				testable = false;
				feedback_prelude += "Not an FST: transducer is non-deterministic.";
			}
		}
		
		
		if (testable) {
			sim = new MealyStepByStateSimulator(automaton);
			try {
				sim.simulateInput("00110");
			} catch (NullPointerException e) {
				if (sim == null)
					throw e;
				testable = false;
				feedback_prelude += "\\n\t Something wrong with file. Perhaps there's no start state?";
			}
		}
		loadTestCaseResults();
		sim = null;
		automaton = null;
	}

	@Override
	public String test(TestCase<String> tc) {
		MealyConfiguration config = (MealyConfiguration) 
				sim.getInitialConfigurations(tc.getTestString())[0];
		
		@SuppressWarnings("rawtypes") // This is JFLAP's fault.
		ArrayList next = sim.stepConfiguration(config);
		while (! next.isEmpty()) {
			config = (MealyConfiguration) next.get(0);
			next = sim.stepConfiguration(config);
		}
		return config.getOutput();
	}

	@Override
	public boolean isMissing() {
		return missing;
	}

	@Override
	public boolean isTestable() {
		return testable;
	}

	@Override
	public FSTGrader getProblemGrader() {
		return (FSTGrader) problem_grader;
	}
	
	public int getExpectedStates() {
		return getProblemGrader().getExpectedStates();
	}
	
	public int getActualStates() {
		return actual_states;
	}
	
	@Override
	public String getFeedback() {
		return feedback_prelude + super.getFeedback();
	}
}
