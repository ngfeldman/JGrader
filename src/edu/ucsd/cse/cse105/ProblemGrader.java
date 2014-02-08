package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.HashMap;

public abstract class ProblemGrader {

	File file;
	String feedback;
	HashMap<String, TestCaseResult> testcase_results; //is this implementation dependent?
	
	public ProblemGrader(File file) {
		this.file = file;
		test();
	}
	
	public abstract void test();
	
	public String getFeedback() {
		return new String(feedback);
	}
	
	public HashMap<String, TestCaseResult> getTestCaseResults() {
		return testcase_results;
	}
}
