package edu.ucsd.cse.cse105;

import java.io.File;


public abstract class DecisionProblemGrader extends TestSuiteGrader<Boolean> {

	public DecisionProblemGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		super(file_name, tests_file, test_info, line_number);
	}
	
	@Override
	protected TestCase<Boolean> createTestCaseFromLine(String[] fields, int line_number) {
        if (fields.length == 1) {
        	Boolean expected_result = stringToR(fields[0]);
        	if (expected_result == null)
        		return super.createTestCaseFromLine(fields, line_number);
        	return new TestCase<Boolean>("", expected_result);
        }
		if (fields.length != 2)
			return super.createTestCaseFromLine(fields, line_number);
		if (fields[0].equalsIgnoreCase("accept")) {
			return new TestCase<Boolean>(fields[1], new Boolean(true));
		}
		if (fields[0].equalsIgnoreCase("reject")) {
			return new TestCase<Boolean>(fields[1], new Boolean(false));
		}
		Boolean expected_result = stringToR(fields[1]);
		if (expected_result == null)
			return super.createTestCaseFromLine(fields, line_number);
		return new TestCase<Boolean>(fields[0], expected_result);
	}
	
	protected static boolean meansTrue(String word) {
		return word.equalsIgnoreCase("true") || word.equalsIgnoreCase("accept");
	}
	
	protected static boolean meansFalse(String word) {
		return word.equalsIgnoreCase("false") || word.equalsIgnoreCase("reject");
	}
	
	@Override
	public Boolean stringToR(String expected_result_str) {
		if (meansTrue(expected_result_str))
			return new Boolean(true);
		if (meansFalse(expected_result_str))
			return new Boolean(false);
		return null;
	}

}
