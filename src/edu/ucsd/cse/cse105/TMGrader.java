package edu.ucsd.cse.cse105;

import java.io.File;

public class TMGrader extends DecisionAutomatonGrader {

	public TMGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		super(file_name, tests_file, test_info, line_number);
	}

	@Override
	public TMResult grade(File file) {
		return new TMResult(this, file);
	}
	
}
