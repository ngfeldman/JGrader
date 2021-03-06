package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.Arrays;

public class DecisionAutomatonGrader extends DecisionProblemGrader {

	protected int expected_states;
	
	public DecisionAutomatonGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		super(file_name, tests_file, test_info, line_number);
	}

	@Override
	public DecisionAutomatonResult grade(File file) {
		return new DecisionAutomatonResult(this, file);
	}

	@Override
	protected void onInstantiate(String[] test_info, int line_number) {
		if (test_info.length > 0 && test_info[0].matches("[0-9]+\\s+states")) {
			expected_states = Integer.parseInt(test_info[0].split("\\s+")[0]);
			test_info = Arrays.copyOfRange(test_info, 1, test_info.length);
			++line_number;
		}
		super.onInstantiate(test_info, line_number);
	}
	
	public int getExpectedStates() {
		return expected_states;
	}
	
}
