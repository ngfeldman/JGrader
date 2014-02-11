package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.Arrays;

public class FSTGrader extends TestSuiteGrader<String> {

	protected int expected_states;
	
	public FSTGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		super(file_name, tests_file, test_info, line_number);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String stringToR(String expected_result_str) {
		return expected_result_str;
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

	@Override
	public FSTResult grade(File file) {
		return new FSTResult(this, file);
	}

	public int getExpectedStates() {
		return expected_states;
	}
}
