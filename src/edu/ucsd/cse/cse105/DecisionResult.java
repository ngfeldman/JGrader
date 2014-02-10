package edu.ucsd.cse.cse105;

public class DecisionResult extends TestCaseResult<Boolean> {
	public DecisionResult(TestCase<Boolean> tc, Boolean actual_result) {
		super(tc, actual_result);
	}
	
	public String getFeedback() {
		String test_string = getTestString();
		String feedback = test_string.equals("") ? "empty string" : test_string;
		feedback += isCorrect() ? " correct " : " incorrect ";
		feedback += "expected ";
		feedback += getExpectedResult() ? "accept" : "reject";
		feedback += " got ";
		feedback += getActualResult() ? "accept" : "reject";
		return feedback;
	}
}
