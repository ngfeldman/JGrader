package edu.ucsd.cse.cse105;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EGrepGrader extends DecisionProblemGrader {

	File egrep_test_file;
	
	public EGrepGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		super(file_name, tests_file, test_info, line_number);
	}
	
	@Override
	public EGrepResult grade(File file) {
		return new EGrepResult(this, file);
	}

	@Override
	protected void onInstantiate(String[] test_info, int line_number) {
		super.onInstantiate(test_info, line_number);
	}
	
	@Override
	protected ArrayList<TestCase<Boolean>> loadTestCases(String[] test_info, int line_number) {
		if (test_info.length == 0) {
			throw new RuntimeException("Line " + line_number + " of tests file: EGrepGrader needs egrep test file name");
		}
		File test_dir = tests_file.getParentFile();
		egrep_test_file = new File(test_dir, test_info[0]);
		BufferedReader br = null;
		ArrayList<TestCase<Boolean>> test_cases = null;
		try {
			br = new BufferedReader(new FileReader(egrep_test_file));
			test_cases = new ArrayList<TestCase<Boolean>>();
			String line = null;
			int egrep_line_number = 0;
			while((line = br.readLine()) != null) {
				++egrep_line_number;
				++line_number;
				if (test_info.length <= egrep_line_number) {
					br.close();
					throw new RuntimeException("Line " + line_number
							+ " of tests file. More lines in egrep test file"
							+ " than accept/reject lines in this tests file.");
				}
				Boolean expected_result = stringToR(test_info[egrep_line_number]);
				if (expected_result == null) {
					br.close();
					throw new RuntimeException("Line " + line_number
							+ " of tests file. Expected \"accept\" or \"reject\"." +
							"Got: " +line);
				}
				test_cases.add(new TestCase<Boolean>(line, expected_result));
			}
			br.close();
			if (test_info.length > egrep_line_number + 1) {
				throw new RuntimeException("Line " + line_number
				+ " of tests file. More accept/reject lines in tests file"
				+ " than lines in egrep test file");
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Line " + line_number
					+ " of tests file. Cannot find egrep test file "
					+ egrep_test_file.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("Line " + line_number
					+ " of tests file. Problem closing egrep test file"
					+ egrep_test_file.getAbsolutePath());
		}
		return test_cases;
	}

}
