package edu.ucsd.cse.cse105;
public class TestCase{
	private boolean accept;
	private String testString;
	
	public TestCase(boolean accept, String testString) {
		this.accept = accept;
		this.testString = testString;
	}
	
	public boolean accept() {
		return accept;
	}
	
	public String getTestString() {
		return new String(testString);
	}
}
