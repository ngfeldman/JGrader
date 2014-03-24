package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class TestSuiteGrader<R> extends ProblemGrader implements Iterable<TestCase<R>>{

	protected ArrayList<TestCase<R>> test_cases;
	
	public TestSuiteGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		super(file_name, tests_file, test_info, line_number);
	}
	
	@Override
	protected void onInstantiate(String[] test_info, int line_number) {
		test_cases = loadTestCases(test_info, line_number);
	}

	protected ArrayList<TestCase<R>> loadTestCases(String[] test_info, int line_number) {
		ArrayList<TestCase<R>> test_cases = new ArrayList<TestCase<R>>();
		for (String line : test_info) {
			String[] between_quotes = line.split("\"");
			LinkedList<String> fields_list = new LinkedList<String>();
			int start = (between_quotes.length > 0 && between_quotes[0].equals("")) ? 1 : 0;
			for (int i=start; i<between_quotes.length; i++) {
				if (i%2 == 0) {
					String[] fields = between_quotes[i].trim().split("\\s+");
					for (int j=0; j<fields.length; j++)
						fields_list.add(fields[j]);
				}
				else {
					fields_list.add(between_quotes[i].trim());
				}
			}
			String[] fields = fields_list.toArray(new String[0]);
			//String[] fields = line.split("\\s+");
			for (int i=0; i<fields.length; i++)
				if (fields[i].equals("!"))
					fields[i] = "";
			test_cases.add(createTestCaseFromLine(fields, line_number));
			++line_number;
		}
		return test_cases;
	}
	
	public static String join(String[] strings, String glue) {
		if (strings.length == 0)
			return "";
		String result = strings[0];
		for (int i = 1; i<strings.length; i++)
			result += glue + strings[i];
		return result;
	}
	
	protected TestCase<R> createTestCaseFromLine(String[] fields, int line_number) {
		if (fields.length != 2)
			throw new RuntimeException("Line " + line_number
					+ " bad in tests file: " + join(fields, " "));
		String test_string = fields[0];
		R expected_result = stringToR(fields[1]);
		if (expected_result == null)
			throw new RuntimeException("Line" + line_number +
					" bad in tests file. Could not interpret expected result \""
					+ fields[1] + "\".");
		return new TestCase<R>(test_string, expected_result);
	}
	
	public TestCase<R> getTestCase(int i) {
		return test_cases.get(i);
	}
	
	public Iterator<TestCase<R>> iterator() {
		return test_cases.iterator();
	}
	
	public ArrayList<TestCase<R>> getTestCases() {
		return test_cases;
	}
	
	public int size() {
		return test_cases.size();
	}
	
	/*
	 * Warning: linear time for lookup
	 */
	public TestCase<R> getTestCase(String test_string) {
		for (TestCase<R> tc : test_cases)
			if (tc.getTestString().equals(test_string))
				return tc;
		return null;
	}
	
	@Override
	public abstract TestSuiteResult<R> grade(File file);
	
	public abstract R stringToR (String expected_result_str);
}
