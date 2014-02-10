package edu.ucsd.cse.cse105;
public class TestCase<R> {
	private String test_string;
	private R expected_result;
	
	public TestCase(String test_string, R expected_result) {
		this.test_string = test_string;
		this.expected_result = expected_result;
	}
	
	public String getTestString() {
		return new String(test_string);
	}
	
	public R getExpectedResult() {
		return expected_result;
	}
}
