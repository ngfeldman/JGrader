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
	
	public static String replaceEmptyStringWithEpsilon(String str) {
		if (str.equals(""))
			return "ε";
		else
			return str;
	}
	
	public String getFeedback() {
		String test_string = replaceEmptyStringWithEpsilon(getTestString());
		String expected = replaceEmptyStringWithEpsilon(getExpectedResult().toString());
		String actual = replaceEmptyStringWithEpsilon(getActualResult().toString());
		
		String feedback = test_string;
		feedback += isCorrect() ? " correct " : " incorrect ";
		feedback += "expected ";
		feedback += expected;
		feedback += " got ";
		feedback += actual;
		return feedback;
	}
}
