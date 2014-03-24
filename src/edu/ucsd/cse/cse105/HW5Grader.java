package edu.ucsd.cse.cse105;

import java.util.HashMap;

public class HW5Grader extends AssignmentGrader {

	@Override
	public int[] score(HashMap<String, ProblemResult> results_map) {
		PDAResult a = (PDAResult) results_map.get("HW5p2a.jff");
		PDAResult b = (PDAResult) results_map.get("HW5p2b.jff");
		int[] p = new int[1];
		p[0] = 0;
		
		if (a.isCorrect())
			++p[0];
		
		if (b.isCorrect())
			++p[0];
		
		return p;
	}

	@Override
	public int numScores() {
		return 1;
	}
	
	public static void main(String[] args) {
		new HW5Grader().mainMethod(args);
	}

}
