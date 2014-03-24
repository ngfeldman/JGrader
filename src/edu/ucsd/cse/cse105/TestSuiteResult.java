package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public abstract class TestSuiteResult<R> extends ProblemResult implements Iterable<TestCaseResult<R>>{
	protected HashMap<String, ? extends TestCaseResult<R>> test_case_results;

	public TestSuiteResult(TestSuiteGrader<R> test_suite_grader, File file) {
		super(test_suite_grader, file);
	}
	
	protected void loadTestCaseResults() {
		if (! isTestable())
			return;
		HashMap<String, TestCaseResult<R>> test_case_results = new HashMap<String, TestCaseResult<R>>();
		for (TestCase<R> tc : getProblemGrader()) {
			test_case_results.put(tc.getTestString(), new TestCaseResult<R>(tc,test(tc)));
		}
		this.test_case_results = test_case_results;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TestSuiteGrader<R> getProblemGrader() {
		return (TestSuiteGrader<R>) super.getProblemGrader();
	}
	
	@Override
	public String getFeedback() {
		String feedback = super.getFeedback();
		if (isTestable()) {
			for (TestCase<R> tc : getProblemGrader()) {
				String test_string = tc.getTestString();
				TestCaseResult<R> tcr = test_case_results.get(test_string);
				feedback += "\\n\t" + tcr.getFeedback();
			}
		}
		return feedback;
	}
	
	public TestCaseResult<R> getTestCaseResult(String test_string) {
		if (! isTestable())
			throwMissingFileException();
		return test_case_results.get(test_string);
	}
	
	public TestCaseResult<R> getTestCaseResult(TestCase<R> tc) {
		if (! isTestable())
			throwMissingFileException();
		return test_case_results.get(tc.getTestString());
	}
	
	public TestCaseResult<R> getTestCaseResult(int i) {
		if (! isTestable())
			throwMissingFileException();
		return test_case_results.get(getProblemGrader().getTestCase(i).getTestString());
	}
	
	public R getActualTestCaseResult(TestCase<R> tc) {
		if (! isTestable())
			throwMissingFileException();
		return getTestCaseResult(tc).getActualResult();
	}
	
	public R getActualTestCaseResult(String test_string) {
		if (! isTestable())
			throwMissingFileException();
		return getTestCaseResult(test_string).getActualResult();
	}
	
	public R getActualTestCaseResult(int i) {
		if (! isTestable())
			throwMissingFileException();
		return getTestCaseResult(i).getActualResult();
	}
	
	public R getExpectedTestCaseResult(TestCase<R> tc) {
		return tc.getExpectedResult();
	}
	
	/**
	 * Warning: linear time lookup if file was missing.
	 * @param test_string
	 * @return
	 */
	public R getExpectedTestCaseResult(String test_string) {
		if (! isTestable())
			return getProblemGrader().getTestCase(test_string).getExpectedResult();
		return getTestCaseResult(test_string).getExpectedResult();
	}
	
	public R getExpectedTestCaseResult(int i) {
		return getProblemGrader().getTestCase(i).getExpectedResult();
	}
	
	public boolean isCorrectFor(TestCase<R> tc) {
		if (! isTestable())
			return false;
		return getTestCaseResult(tc).isCorrect();
	}
	
	public boolean isCorrectFor(String test_string) {
		if (! isTestable())
			return false;
		return getTestCaseResult(test_string).isCorrect();
	}
	
	public boolean isCorrectFor(int i) {
		if (! isTestable())
			return false;
		return getTestCaseResult(i).isCorrect();
	}
	
	public boolean isCorrect() {
		return allCasesCorrect();
	}
	
	public boolean allCasesCorrect() {
		if (! isTestable())
			return false;
		for (TestCaseResult<R> tcr : test_case_results.values()) {
			if (! tcr.isCorrect())
				return false;
		}
		return true;
	}
	
	public int numberOfCorrectCases() {
		if (! isTestable())
			return 0;
		int count = 0;
		for (TestCaseResult<R> tcr : test_case_results.values())
			if (tcr.isCorrect())
				++count;
		return count;
	}
	
	public boolean allCasesCorrectExceptFor(String... test_strings) {
		if (! isTestable())
			return false;
		HashSet<String> test_strings_set = new HashSet<String>();
		for (String test_string : test_strings)
			test_strings_set.add(test_string);
		for (TestCaseResult<R> tcr : test_case_results.values())
			if (! (tcr.isCorrect() || test_strings_set.contains(tcr.getTestString())))
					return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public int size() {
		return ((TestSuiteGrader<R>) problem_grader).size();
	}
	
	/**
	 * This is a hack. Does not do anything on remove();
	 */
	public Iterator<TestCaseResult<R>> iterator() {
		ArrayList<TestCaseResult<R>> list = new ArrayList<TestCaseResult<R>>();
		for (TestCaseResult<R> tcr : test_case_results.values())
			list.add(tcr);
		return list.iterator();
	}
	
	public abstract R test(TestCase<R> tc);
	
}
