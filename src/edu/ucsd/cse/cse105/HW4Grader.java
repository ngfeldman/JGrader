package edu.ucsd.cse.cse105;

import java.util.HashMap;

public class HW4Grader extends AssignmentGrader {

	@Override
	public int[] score(HashMap<String, ProblemResult> results_map) {
		RegularExpressionResult r = (RegularExpressionResult) results_map.get("HW4p3.jff");
		int[] p = new int[1];
		if (r.isMissing())
			p[0] = 0;
		else if (r.isCorrect())
			p[0] = 2;
		else if (r.size() - r.numberOfCorrectCases() <= 2)
			p[0] = 1;
		else
			p[0] = 0;
		return p;
	}

	@Override
	public int numScores() {
		return 1;
	}
	
	public static void main(String[] args) {
		new HW4Grader().mainMethod(args);
	}

}
