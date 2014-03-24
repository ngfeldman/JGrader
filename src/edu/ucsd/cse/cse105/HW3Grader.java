package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.HashMap;

public class HW3Grader extends AssignmentGrader {

	@Override
	public int[] score(HashMap<String, ProblemResult> results_map) {
		int p[] = new int[12];
		for (int i=0; i<p.length; i++)
			p[i] = -1;
		EGrepResult p1 = (EGrepResult) results_map.get("HW3p1.rex");
		FSTReductionResult p2a = (FSTReductionResult) results_map.get("HW3p2a.jff");
		FSTReductionResult p2b = (FSTReductionResult) results_map.get("HW3p2b.jff");
		FSTReductionResult p2c = (FSTReductionResult) results_map.get("HW3p2c.jff");
		FSTResult p3a = (FSTResult) results_map.get("encode.jff");
		FSTResult p3b = (FSTResult) results_map.get("decode.jff");
		FSTResult p4a = (FSTResult) results_map.get("HW3p4a.out.jff");
		FSTResult p4b = (FSTResult) results_map.get("HW3p4b.out.jff");
		DFAResult p5a = (DFAResult) results_map.get("HW3p5a.out.jff");
		DFAResult p5b = (DFAResult) results_map.get("HW3p5b.out.jff");
		RegularExpressionResult p6a = (RegularExpressionResult) results_map.get("HW3p6a.jff");
		RegularExpressionResult p6b = (RegularExpressionResult) results_map.get("HW3p6b.jff");
		if (p1.isCorrect())
			p[1] = 2;
		else if (p1.numberOfCorrectCases() > 17)
			p[1] = 1;
		else
			p[1] = 0;
		
		int p2 = 0;
		if (p2a.isCorrect())
			++p2;
		if (p2b.isCorrect())
			++p2;
		if (p2c.isCorrect())
			++p2;
		if (p2 > 2)
			p2 = 2;
		p[3] = p2;
	
		p[5] = 0;
		if (p3a.isCorrect())
			++p[5];
		if (p3b.isCorrect())
			++p[5];
		
		if (p4a.isCorrect() && p4b.isCorrect())
			p[7] = 2;
		else
			p[7] = 0;
		
		if (p5a.isCorrect() && p5b.isCorrect())
			p[9] = 2;
		else
			p[9] = 0;
		
		p[11] = 0;
		if (p6a.isCorrect())
			++p[11];
		if (p6b.isCorrect())
			++p[11];
		
		return p;
	}

	@Override
	public int numScores() {
		return 12;
	}

	public static class P2aGrader extends FSTReductionGrader {
		public P2aGrader(String file_name, File tests_file, String[] test_info, int line_number){
			super(file_name, tests_file, test_info, line_number);
		}

		@Override
		public boolean inHardLanguage(String reduction_output) {
			int ones = 0;
			int zeros = 0;
			for (char c: reduction_output.toCharArray()) {
				if (c == '0')
					++zeros;
				else if (c == '1')
					++ones;
			}
			return ones == zeros;
		}
	}
	
	public static class P2bGrader extends FSTReductionGrader {
		public P2bGrader(String file_name, File tests_file, String[] test_info, int line_number){
			super(file_name, tests_file, test_info, line_number);
		}

		@Override
		public boolean inHardLanguage(String reduction_output) {
			int l = reduction_output.length();
			if (l % 2 != 0)
				return false;
			String w1 = reduction_output.substring(0, l/2);
			String w2 = reduction_output.substring(l/2, l);
			return w1.equals(w2);
		}
	}
	
	public static class P2cGrader extends FSTReductionGrader {
		public P2cGrader(String file_name, File tests_file, String[] test_info, int line_number){
			super(file_name, tests_file, test_info, line_number);
		}

		@Override
		public boolean inHardLanguage(String reduction_output) {
			try {
				for (char c : reduction_output.toCharArray()) {
					if (! Character.isDigit(c))
						return false;
				}
				int num = Integer.parseInt(reduction_output, 2);
				return num % 3 == 0;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}
	public static void main(String args[]) {
		(new HW3Grader()).mainMethod(args);
	}
}
