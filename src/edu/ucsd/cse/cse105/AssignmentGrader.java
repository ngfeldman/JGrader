package edu.ucsd.cse.cse105;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class AssignmentGrader {
	protected AllTestSuitesResults results;
	
	/**
	 * This needs to be filled in for each assignment. 
	 * @param maps filename -> test string -> correctness
	 * @return score of assignment
	 */
	public abstract int[] score();
	public abstract int numScores();
	
	public static int[] array(int... ints) {
		return ints;
	}
	
	public String gradeAssignment(File dir, ArrayList<TestSuite> test_suites) {
		File[] files = dir.listFiles();
		if (files.length == 2 && files[0].isDirectory() && files[1].isDirectory()) {
			if (files[0].getName().contains("MACOS")) {
				files = files[1].listFiles();
			}
			if (files[1].getName().contains("MACOS")) {
				files = files[0].listFiles();
			}
		}
		while ((files.length == 1 && files[0].isDirectory()))
			files = files[0].listFiles();
		HashMap<String, File> files_map = new HashMap<String, File>();
		for (File file : files)
			files_map.put(file.getName(), file);
		HashMap<String, TestSuiteResult> results_map = new HashMap<String, TestSuiteResult>();
		for (TestSuite suite : test_suites) {
			String file_name = suite.getFileName();
			File file = files_map.get(file_name);
			TestSuiteResult tsr = new TestSuiteResult(file, suite);
			results_map.put(file_name, tsr);
		}
		results = new AllTestSuitesResults(results_map);
		
		String feedback = results.getAllFeedback();
		int scores[] = score();
		int expected_num_scores = numScores();
		if (expected_num_scores != scores.length)
			throw new RuntimeException("Method score() should return "
					+ expected_num_scores + " scores, not " + scores.length + ".");
		String scores_str = "";
		for (int score : scores) {
			if (score >= 0)
				scores_str += score + ";";
			else
				scores_str += ";";
		}
		return scores_str + feedback;
	}
	
	public void mainMethod(String[] args) {
		if (args.length < 2 || args.length > 3){
			System.err.println("Incorrect number of arguments");
			System.err.println("Syntax: java AssignmentGrader " +
					"<submission_dir> <test_cases_file> [readable=1]");
			return;
		}
		File submissionDir = new File(args[0]);
		File testsFile = new File(args[1]);
		
		if (!testsFile.canRead()) {
			System.err.println("can't read test_cases_file");
			return;
		}
		if (!submissionDir.canRead()) {
			System.err.println("can't read submission_dir");
			return;
		}
		
		boolean readable = true;
		if (args.length >= 3) {
			if (args[2].equals("0") || args[2].equalsIgnoreCase("false"))
				readable = false;
			else if (! (args[2].equals("1") || args[2].equalsIgnoreCase("true")))
				System.err.println("third argument should be 0 or 1");
		}
		
		ArrayList<TestSuite> testSuites = TestSuite.createTestSuites(testsFile);
		String result = gradeAssignment(submissionDir, testSuites);
		
		if (readable)
			System.out.println(result.replace("\\n", "\n"));
		else
			System.out.println(result);
	}
}
