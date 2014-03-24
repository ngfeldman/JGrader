package edu.ucsd.cse.cse105;

import java.util.HashMap;

public class HW6Grader extends AssignmentGrader {

	@Override
	public int[] score(HashMap<String, ProblemResult> results_map) {
		TMResult a = (TMResult) results_map.get("HW6.jff");
		int[] p = new int[1];
		p[0] = 0;
		
		if (a.isCorrect())
			p[0] = 2;
		else if (a.size() - a.numberOfCorrectCases() <= 6)
			p[0] = 1;
		
		
		return p;
	}

	@Override
	public int numScores() {
		return 1;
	}
	
	public static void main(String[] args) {
		new HW6Grader().mainMethod(args);
	}

}