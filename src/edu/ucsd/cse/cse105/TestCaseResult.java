package edu.ucsd.cse.cse105;

public class TestCaseResult {
	private TestCase tc;
	boolean actual;
	
	public TestCaseResult(TestCase tc, boolean actual) {
		this.tc = tc;
		this.actual = actual;
	}
	
	public TestCase getTestCase() {
		return tc;
	}
	
	public String getTestString() {
		return tc.getTestString();
	}
	
	public boolean getExpectedResult() {
		return tc.accept();
	}
	
	public boolean getActualResult() {
		return actual;
	}
	
	public boolean isCorrect() {
		return (tc.accept() == actual);
	}
	
	public String getFeedback() {
		String test_string = getTestString();
		String feedback = test_string.equals("") ? "empty string" : test_string;
		feedback += isCorrect() ? " correct " : " incorrect ";
		feedback += "expected ";
		feedback += tc.accept() ? "accept" : "reject";
		feedback += " got ";
		feedback += actual ? "accept" : "reject";
		return feedback;
	}
}
