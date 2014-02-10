package edu.ucsd.cse.cse105;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public abstract class AssignmentGrader {
	
	/**
	 * This needs to be filled in for each assignment. 
	 * @param maps filename -> test string -> correctness
	 * @return score of assignment
	 */
	public abstract int[] score(HashMap<String, ProblemResult> results_map);
	public abstract int numScores();
	
	public static int[] array(int... ints) {
		return ints;
	}
	
	public String gradeAssignment(File dir, ArrayList<ProblemGrader> problem_graders) {
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
		HashMap<String, ProblemResult> results_map = new HashMap<String, ProblemResult>();
		for (ProblemGrader pg : problem_graders) {
			String file_name = pg.getFileName();
			File file = files_map.get(file_name);
			ProblemResult pr = pg.grade(file);
			results_map.put(file_name, pr);
		}
		
		String feedback = getAllFeedback(results_map);
		int scores[] = score(results_map);
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
	
	public static String getAllFeedback(HashMap<String, ProblemResult> results_map) {
		String feedback = "";
		String[] keys = new String[0];
		keys = results_map.keySet().toArray(keys);
		Arrays.sort(keys);
		for (String file_name : keys) {
			feedback += file_name + ":" + results_map.get(file_name).getFeedback() + "\\n";
		}
		return feedback.substring(0, feedback.length()-2);
	}
	
	
	public int numberOfCorrectProblems(HashMap<String, ProblemResult> results) {
		int count = 0;
		for (ProblemResult pr : results.values()) {
			if (pr.isCorrect())
				++count;
		}
		return count;
	}
	
	public boolean allProblemsCorrect(HashMap<String, ProblemResult> results) {
		for (ProblemResult pr : results.values()) {
			if (! pr.isCorrect())
				return false;
		}
		return true;
	}
	
	public boolean allProblemsCorrectExceptFor(HashMap<String, ProblemResult> results, String... file_names) {
		HashSet<String> file_name_set = new HashSet<String>();
		for (String file_name : file_names)
			file_name_set.add(file_name);
		for (ProblemResult pr : results.values())
			if (! (file_name_set.contains(pr.getFileName()) || pr.isCorrect()) )
				return false;
		return true;
	}
	
	protected void mainMethod(String[] args) {
		if (args.length < 2 || args.length > 3){
			System.err.println("Incorrect number of arguments");
			System.err.println("Syntax: java AssignmentGrader " +
					"<submission_dir> <test_cases_file> [readable=1]");
			return;
		}
		File submission_dir = new File(args[0]);
		File tests_file = new File(args[1]);
		
		if (!tests_file.canRead()) {
			System.err.println("can't read test_cases_file");
			return;
		}
		if (!submission_dir.canRead()) {
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
		
		ArrayList<ProblemGrader> testSuites = ProblemGrader.createProblemGraders(tests_file);
		String result = gradeAssignment(submission_dir, testSuites);
		
		if (readable)
			System.out.println(result.replace("\\n", "\n"));
		else
			System.out.println(result);
	}
}
