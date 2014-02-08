package edu.ucsd.cse.cse105;


public class HW2Grader extends AssignmentGrader {
	// from superclass: protected AllTestSuitesResults results;

	public int numScores() {
		return 5;
	}
	
	public int[] score() {
		int scores[] = new int[5];
		
		int p1 = 0;
		for (char c = 'a'; c <= 'd'; c++) {
			if (results.getTestSuiteResult("hw1"+c+".jff").allCasesCorrect())
				++p1;
		}
		if (p1==0)
			scores[0]=0;
		else if (p1<3)
			scores[0]=1;
		else
			scores[0]=2;
		
		int p2 = 0;
		for (char c = 'a'; c <= 'b'; c++) {
			TestSuiteResult tsr = results.getTestSuiteResult("hw2" + c +".jff");
			if (tsr.getSpecificType().equals("DFA")
					&& tsr.getActualStates() == tsr.getExpectedStates()
					&& tsr.allCasesCorrect())
				++p2; 
		}
		scores[1]=p2;
		
		int p3 = 0;
		for (char c = 'a'; c <= 'c'; c++) {
			TestSuiteResult tsr = results.getTestSuiteResult("hw3" + c +".jff");
			if (tsr.allCasesCorrect())
				++p3;
		}
		if (p3==0)
			scores[2]=0;
		else if (p3==1)
			scores[2]=1;
		else
			scores[2]=2;
		
		int p4 = 0;
		boolean all_p4_missing = true;
		for (char c = 'a'; c <= 'b'; c++) {
			if (results.getTestSuiteResult("hw4" + c + "1.out.jff").allCasesCorrect() && results.getTestSuiteResult("hw4" + c + "2.out.jff").allCasesCorrect())
				++p4;
			if (!(results.getTestSuiteResult("hw4" + c + "1.out.jff").isMissing() && results.getTestSuiteResult("hw4" + c + "2.out.jff").isMissing()))
				all_p4_missing = false;
		}
		if (all_p4_missing)
			scores[3] = -1;
		else
			scores[3] = p4;
		
		if (results.getTestSuiteResult("hw5a1.out.jff").isMissing()
				&& results.getTestSuiteResult("hw5a2.out.jff").isMissing()
				&& results.getTestSuiteResult("hw5b.out.jff").isMissing())
			scores[4] = -1;
		else if (results.getTestSuiteResult("hw5a1.out.jff").allCasesCorrect() && results.getTestSuiteResult("hw5a2.out.jff").allCasesCorrect())
			scores[4] = 2;
		else if (results.getTestSuiteResult("hw5b.out.jff").allCasesCorrect())
			scores[4] = 2;
		else
			scores[4] = 0;
		
		return scores;
	}

	public HW2Grader() {
		super();
	}

	public static void main(String args[]) {
		(new HW2Grader()).mainMethod(args);
	}
}
