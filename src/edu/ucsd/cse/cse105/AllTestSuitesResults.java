package edu.ucsd.cse.cse105;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class AllTestSuitesResults {
	private HashMap<String, TestSuiteResult> results;
	
	public AllTestSuitesResults(HashMap<String, TestSuiteResult> results) {
		this.results = results;
	}

	public HashMap<String, TestSuiteResult> getResults() {
		return results;
	}
	
	public TestSuiteResult getTestSuiteResult(String file_name) {
		return results.get(file_name);
	}
	
	public int numberOfCorrectProblems() {
		int count = 0;
		for (TestSuiteResult tsr : results.values()) {
			if (tsr.allCasesCorrect())
				++count;
		}
		return count;
	}
	
	public boolean allProblemsCorrect(
			HashMap<String, TestSuiteResult> results) {
		return results.size() == numberOfCorrectProblems();
	}
	
	public int numberOfCorrectCases() {
		int count = 0;
		for (TestSuiteResult tsr : results.values())
			for (TestCaseResult tcr : tsr)
				if (tcr.isCorrect())
					++count;
		return count;
	}
	
	public int numberOfCases() {
		int count = 0;
		for (TestSuiteResult tsr : results.values())
			count += tsr.size();
		return count;
	}
	
	public boolean allProblemsCorrectExceptFor(String... file_names) {
		HashSet<String> file_name_set = new HashSet<String>();
		for (String file_name : file_names)
			file_name_set.add(file_name);
		for (TestSuiteResult tsr : results.values())
			if (! (file_name_set.contains(tsr.getFileName()) || tsr.allCasesCorrect()))
				return false;
		return true;
	}
	
	public String getAllFeedback() {
		String feedback = "";
		ArrayList<String> keys = new ArrayList<String>(results.keySet());
		Collections.sort(keys);
		for (String file_name : keys) {
			TestSuiteResult testsuite_result = results.get(file_name);
			feedback += testsuite_result.getFeedback() + "\\n";
		}
		if (feedback.length() >= 2)
			feedback = feedback.substring(0, feedback.length()-2);
		return feedback;
	}
}
