package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.HashMap;

public abstract class DecisionProblemResult extends TestSuiteResult<Boolean> {

	public DecisionProblemResult(DecisionProblemGrader problem_grader, File file) {
		super(problem_grader, file);
	}
	
	@Override
	protected void loadTestCaseResults() {
		HashMap<String, DecisionResult> test_case_results = new HashMap<String, DecisionResult>();
		for (TestCase<Boolean> tc : getProblemGrader())
			test_case_results.put(tc.getTestString(), new DecisionResult(tc,test(tc)));
		this.test_case_results = test_case_results;
	}
	
	@Override
	public DecisionResult getTestCaseResult(String test_string) {
		if (! isTestable())
			throwMissingFileException();
		return (DecisionResult) test_case_results.get(test_string);
	}
	
	@Override
	public DecisionResult getTestCaseResult(TestCase<Boolean> tc) {
		if (! isTestable())
			throwMissingFileException();
		return (DecisionResult) test_case_results.get(tc.getTestString());
	}
	
	@Override
	public DecisionResult getTestCaseResult(int i) {
		if (! isTestable())
			throwMissingFileException();
		return (DecisionResult) test_case_results.get(getProblemGrader().getTestCase(i).getTestString());
	}

}