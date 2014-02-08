package edu.ucsd.cse.cse105;


public class HW1Grader extends AssignmentGrader {
	// from superclass: protected AllTestSuitesResults results;
	
	public int numScores() {
		return 1;
	}
	
	public int[] score() {
		int correctProblems = results.numberOfCorrectProblems();
		if (results.getTestSuiteResult("hw1c.jff")==null) {
			System.err.println("what?!");
		}
		boolean partCCorrectExceptEmptyString = 
				!results.getTestSuiteResult("hw1c.jff").allCasesCorrect()
				&& results.getTestSuiteResult("hw1c.jff").allCasesCorrectExceptFor("");
		
		if (correctProblems >= 4)
			return array(2);
		
		if (correctProblems == 3
				&& (partCCorrectExceptEmptyString
						|| results.getTestSuiteResult("hw1e.jff").allCasesCorrect()))
			return array(2);
		
		if (correctProblems >= 2)
			return array(1);
		
		return array(0);
	}
	
	public HW1Grader() {
		super();
	}
	
	public static void main(String args[]) {
		(new HW1Grader()).mainMethod(args);
	}
}
