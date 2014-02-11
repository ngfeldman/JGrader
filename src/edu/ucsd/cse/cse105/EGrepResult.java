package edu.ucsd.cse.cse105;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class EGrepResult extends DecisionProblemResult {

	File regex_file;
	String regex;
	boolean missing = true;
	boolean testable = false;
	
	public EGrepResult(EGrepGrader problem_grader, File file) {
		super(problem_grader, file);
		regex_file = file;
		if (file == null || !file.canRead()) {
			return;
		}
		missing = false;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(regex_file));
			regex = "";
			String line = null;
			if ((line = br.readLine()) != null)
				regex += line;
			br.close();
			int len = regex.length();
			if (len > 0) {
				if (regex.charAt(0) != '^')
					regex = "^" + regex;
				if (regex.charAt(len-1) != '$')
					regex = regex + "$";
			}
		} catch (FileNotFoundException e) {
			missing = true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (test(new TestCase<Boolean>("blah", true)) != null)
			testable = true;
		// TODO check for testable
		loadTestCaseResults();
	}
	
	@Override
	public Boolean test(TestCase<Boolean> tc) {
		try {
			String[] command = {"egrep", regex};
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			OutputStream stdin = process.getOutputStream();
			String test_string = tc.getTestString();
			stdin.write(test_string.getBytes());
			stdin.flush();
			stdin.close();
			int status = process.waitFor();
			if (status != 0 && status != 1) //exit status 1 just means no match found
				return null;
			String output = "";
			String line;
			while ((line = br.readLine()) != null) {
			    output += line + "\n";
			}
			if (output.length() > 0)
				output = output.substring(0,output.length()-1);
			return new Boolean(tc.getTestString().equals(output));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isMissing() {
		return missing;
	}

	@Override
	public boolean isTestable() {
		return testable;
	}

}
