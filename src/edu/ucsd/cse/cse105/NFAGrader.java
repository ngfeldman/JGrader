package edu.ucsd.cse.cse105;

import java.io.File;

public class NFAGrader extends DecisionAutomatonGrader {
	
	public NFAGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		super(file_name, tests_file, test_info, line_number);
	}
	
	@Override
	public DFAResult grade(File file) {
		return new DFAResult(this, file);
	}
}
