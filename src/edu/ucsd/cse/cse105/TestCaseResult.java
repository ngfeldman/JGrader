package edu.ucsd.cse.cse105;

public class TestCaseResult<R> {
	private TestCase<R> tc;
	R actual_result;
	
	public TestCaseResult(TestCase<R> tc, R actual_result) {
		this.tc = tc;
		this.actual_result = actual_result;
	}
	
	public TestCase<R> getTestCase() {
		return tc;
	}
	
	public String getTestString() {
		return tc.getTestString();
	}
	
	public R getExpectedResult() {
		return tc.getExpectedResult();
	}
	
	public R getActualResult() {
		return actual_result;
	}
	
	public boolean isCorrect() {
		return (tc.getExpectedResult().equals(actual_result));
	}
	
	public String getFeedback() {
		String test_string = getTestString();
		String feedback = test_string.equals("") ? "empty string" : test_string;
		feedback += isCorrect() ? " correct " : " incorrect ";
		feedback += "expected ";
		feedback += getExpectedResult();
		feedback += " got ";
		feedback += getActualResult();
		return feedback;
	}
}
