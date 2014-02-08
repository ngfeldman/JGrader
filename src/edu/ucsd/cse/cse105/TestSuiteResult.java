package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import regular.RegularExpression;
import automata.Automaton;
import automata.AutomatonChecker;
import automata.AutomatonSimulator;
import automata.fsa.FSAStepWithClosureSimulator;
import automata.fsa.FiniteStateAutomaton;
import automata.mealy.MealyMachine;
import automata.mealy.MooreMachine;
import automata.pda.PDAStepWithClosureSimulator;
import automata.pda.PushdownAutomaton;
import automata.turing.TMSimulator;
import automata.turing.TuringMachine;
import file.ParseException;
import file.XMLCodec;
import grammar.Grammar;
import grammar.GrammarChecker;

public class TestSuiteResult implements Iterable<TestCaseResult>{
	private TestSuite suite;
	private HashMap<String, TestCaseResult> testcase_results;
	private String specific_type;
	private Object jff_obj;
	private String feedback;
	private int actual_states;
	boolean missing;
	
	public TestSuiteResult(File file, TestSuite suite) {
		this.suite = suite;
		if (file == null) {
			missing = true;
			feedback = suite.getFileName() + " missing.";
			return;
		}
		missing = false;
		XMLCodec codec = new XMLCodec();
		try {
			jff_obj = codec.decode(file, null);
		} catch (ParseException e) {
			missing = true;
			feedback = suite.getFileName() +" was not generated properly. "
					+ "This is likely due to a problem in your Haskell code.";
			return;
		}
		
		feedback = suite.getFileName() + ":";
		specific_type = getJffObjectType(jff_obj);
		testcase_results = new HashMap<String, TestCaseResult>();
		
		String type = suite.getType();
		//TODO: write tester classes to plug in here, to be loaded by Grader
		if (type.equalsIgnoreCase("Automaton") || type == null) {
			if (!isDecisionAutomaton(specific_type)) {
				feedback += "\\n\tnot an Automaton";
				return;
			}
			testDecisionAutomaton((Automaton) jff_obj);
			return;
		}
		if (type.equalsIgnoreCase("DFA")) {
			if (!isDecisionAutomaton(specific_type)) {
				feedback += "\\n\tnot an Automaton";
				return;
			}
			if (!specific_type.equals("DFA")) {
				feedback += "\\n\tnot a DFA";
				// still testable
			}
			testDecisionAutomaton((Automaton) jff_obj);
			return;
		}
		if (type.equalsIgnoreCase("NFA")) {
			if (!isDecisionAutomaton(specific_type)) {
				feedback += "\\n\tnot an Automaton";
				return;
			}
			if (!(jff_obj instanceof FiniteStateAutomaton)) {
				feedback += "\\n\tnot an NFA";
				// still testable
			}
			testDecisionAutomaton((Automaton) jff_obj);
			return;
		}
		if (type.equalsIgnoreCase("PushdownAutomaton")) {
			if (!isDecisionAutomaton(specific_type)) {
				feedback += "\\n\tnot an Automaton";
				return;
			}
			if (!specific_type.equals("PushdownAutomaton")) {
				feedback += "\\n\tnot a PushdownAutomaton";
				// still testable
			}
			testDecisionAutomaton((Automaton) jff_obj);
			return;
		}
		if (type.equalsIgnoreCase("TuringMachine")) {
			if (!isDecisionAutomaton(specific_type)) {
				feedback += "\\n\tnot an Automaton";
				return;
			}
			if (!specific_type.equals("TuringMachine")) {
				feedback += "\\n\tnot a TuringMachine";
				// still testable
			}
			testDecisionAutomaton((Automaton) jff_obj);
			return;
		}
		if (type.equalsIgnoreCase("ContextFreeGrammar")) {
			if (!(jff_obj instanceof Grammar)) {
				feedback += "\\n\tnot a Grammar";
				return;
			}
			if (!specific_type.equals("ContextFreeGrammar")) {
				feedback += "\\n\tnot a ContextFreeGrammar";
				// still testable
			}
			//TODO: testGrammar((Grammar) jff_obj);
			return;
		}
		if (type.equalsIgnoreCase("Grammar")) {
			if (!(jff_obj instanceof Grammar)) {
				feedback += "\\n\tnot a Grammar";
				return;
			}
			//TODO: testGrammar((Grammar) jff_obj);
			return;
		}
		if (type.equalsIgnoreCase("RegularExpression")) {
			if (!specific_type.equals("RegularExpression")) {
				feedback += "\\n\tnot a RegularExpression";
				return;
			}
			testRegularExpression((RegularExpression) jff_obj);
			return;
		}
		throw new RuntimeException("Unknown test object type "
				+ type + " in test suites text file.");
	}

	public TestCaseResult getTestCaseResult(String test_string) {
		if (missing)
			throwMissingFileException();
		return testcase_results.get(test_string);
	}
	
	public TestCaseResult getTestCaseResult(TestCase tc) {
		if (missing)
			throwMissingFileException();
		return testcase_results.get(tc.getTestString());
	}
	
	public TestCaseResult getTestCaseResult(int i) {
		if (missing)
			throwMissingFileException();
		return testcase_results.get(suite.getTestCase(i).getTestString());
	}
	
	public Boolean getActualTestCaseResult(TestCase tc) {
		if (missing)
			throwMissingFileException();
		return getTestCaseResult(tc).getActualResult();
	}
	
	public Boolean getActualTestCaseResult(String test_string) {
		if (missing)
			throwMissingFileException();
		return getTestCaseResult(test_string).getActualResult();
	}
	
	public Boolean getActualTestCaseResult(int i) {
		if (missing)
			throwMissingFileException();
		return getTestCaseResult(i).getActualResult();
	}
	
	public boolean getExpectedTestCaseResult(TestCase tc) {
		return tc.accept();
	}
	
	/**
	 * Warning: linear time lookup if file was missing.
	 * @param test_string
	 * @return
	 */
	public boolean getExpectedTestCaseResult(String test_string) {
		if (missing)
			return suite.getTestCase(test_string).accept();
		return getTestCaseResult(test_string).getExpectedResult();
	}
	
	public boolean getExpectedTestCaseResult(int i) {
		return suite.getTestCase(i).accept();
	}
	
	public boolean isCorrectFor(TestCase tc) {
		if (missing)
			return false;
		return getTestCaseResult(tc).isCorrect();
	}
	
	public boolean isCorrectFor(String test_string) {
		if (missing)
			return false;
		return getTestCaseResult(test_string).isCorrect();
	}
	
	public boolean isCorrectFor(int i) {
		if (missing)
			return false;
		return getTestCaseResult(i).isCorrect();
	}
	
	public boolean allCasesCorrect() {
		if (missing)
			return false;
		for (TestCaseResult tcr : testcase_results.values()) {
			if (! tcr.isCorrect())
				return false;
		}
		return true;
	}
	
	public int numberOfCorrectCases() {
		if (missing)
			return 0;
		int count = 0;
		for (TestCaseResult tcr : testcase_results.values())
			if (tcr.isCorrect())
				++count;
		return count;
	}
	
	public boolean allCasesCorrectExceptFor(String... test_strings) {
		if (missing)
			return false;
		HashSet<String> test_strings_set = new HashSet<String>();
		for (String test_string : test_strings)
			test_strings_set.add(test_string);
		for (TestCaseResult tcr : testcase_results.values())
			if (! (tcr.isCorrect() || test_strings_set.contains(tcr.getTestString())))
					return false;
		return true;
	}
	
	public Iterator<TestCaseResult> iterator() {
		if (missing)
			throwMissingFileException();
		return testcase_results.values().iterator();
	}
	
	public int getExpectedStates() {
		return suite.getStates();
	}
	
	public int getActualStates() {
		if (missing)
			throwMissingFileException();
		return actual_states;
	}
	
	public String getFeedback() {
		return new String(feedback);
	}
	
	public String toString() {
		return getFeedback();
	}
	
	public Object getJffObject() {
		return jff_obj;
	}
	
	public String getFileName() {
		return suite.getFileName();
	}
	
	public int size() {
		return suite.size();
	}
	
	public String getSpecificType() {
		if (missing)
			return "Missing";
		return new String(specific_type);
	}
	
	public static String getJffObjectType(Object jff_obj) {
		if (jff_obj instanceof Automaton) {
			if (jff_obj instanceof FiniteStateAutomaton) {
				AutomatonChecker ac = new AutomatonChecker();
				if (ac.isNFA((FiniteStateAutomaton)jff_obj))
					return "NFA";
				else
					return "DFA";
			}
			if (jff_obj instanceof MealyMachine)
				return "MealyMachine";
			if (jff_obj instanceof MooreMachine)
				return "MooreMachine";
			if (jff_obj instanceof PushdownAutomaton)
				return "PushdownAutomaton";
			//TODO: find a way to detect nondeterminism in turing machines
			if (jff_obj instanceof TuringMachine)
				return "TuringMachine";
			return "Automaton";
		}
		if (jff_obj instanceof Grammar) {
			if (GrammarChecker.isContextFreeGrammar((Grammar) jff_obj))
				return "ContextFreeGrammar";
			else
				return "Grammar";
		}
		if (jff_obj instanceof RegularExpression) {
			return "RegularExpression";
		}
		return null;
	}
	
	public static boolean isDecisionAutomaton(String type) {
		return (type.equals("DFA")
				|| type.equals("NFA")
				|| type.equals("FiniteStateAutomaton")
				|| type.equals("PushDownAutomaton")
				|| type.equals("TuringMachine"));
	}
	
	private void testDecisionAutomaton(Automaton a) {
		actual_states = a.getStates().length;
		if (getExpectedStates() > 0) {
			feedback += "\\n\texpected " + getExpectedStates()
					+ " states, got " + getActualStates() + " states";
		}
		AutomatonSimulator sim = null;
		if (a instanceof FiniteStateAutomaton) {
			sim = new FSAStepWithClosureSimulator(a);
		}
		else if (a instanceof PushdownAutomaton) {
			sim = new PDAStepWithClosureSimulator(a);
		}
		else if (a instanceof TuringMachine) {
			sim = new TMSimulator(a);
		} else {
			feedback += "\\n\t Incorrect type of Automaton";
			return;
		}
		
		for (TestCase tc : suite) {
			String test_string = tc.getTestString();
			boolean actual;
			try {
				actual = sim.simulateInput(test_string);
			} catch (NullPointerException e) {
				if (test_string == null || sim == null) {
					throw e;
				}
				feedback += "\\n\t Something wrong with file";
				break;
			}
			TestCaseResult testcase_result = new TestCaseResult(tc, actual);
			testcase_results.put(test_string, testcase_result);
			feedback += "\\n\t" + testcase_result.getFeedback();
		}
	}

/*	
	private void testGrammar(Grammar g) {
		for (TestCase tc : suite) {
			String test_string = tc.getTestString();
			//TODO: figure out how to check if a grammar accepts a string
			boolean actual = false;
			testcase_results.put(tc, new Boolean(actual));
			boolean expected = tc.accept();
			feedback += "\\n\t" + generateFeedbackForTestCase(
					test_string, expected, actual);
		}
	}*/
	
	private void testRegularExpression(RegularExpression regex) {
		Pattern pattern;
		try {
			pattern = Pattern.compile(regex.asCheckedString()
					.replace("+", "|").replaceAll("!\\*","").replace("!", ""));
		} catch (UnsupportedOperationException e) {
			feedback += "\\n\t malformed RegularExpression";
			return;
		} catch (PatternSyntaxException e) {
			feedback += "\\n\t malformed RegularExpression";
			return;
		}
		for (TestCase tc : suite) {
			String test_string = tc.getTestString();
			boolean actual = pattern.matcher(test_string).matches();
			TestCaseResult testcase_result = new TestCaseResult(tc, actual);
			testcase_results.put(test_string, testcase_result);
			feedback += "\\n\t" + testcase_result.getFeedback();
		}
	}
	
	public boolean isMissing() {
		return missing;
	}
	
	private void throwMissingFileException() {
		throw new NullPointerException("Cannot return value because " + suite.getFileName() + " was missing.");
	}
}
