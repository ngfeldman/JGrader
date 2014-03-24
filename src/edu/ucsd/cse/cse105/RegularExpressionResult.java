package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import file.ParseException;
import file.XMLCodec;

import regular.RegularExpression;

public class RegularExpressionResult extends DecisionProblemResult {
	private boolean missing = true;
	private boolean testable = false;

	protected String feedback_prelude = "";

	protected RegularExpression regex;
	protected Pattern pattern;
	
	public RegularExpressionResult(RegularExpressionGrader problem_grader, File file) {
		super(problem_grader, file);
		if (file == null) {
			return;
		}
		missing = false;
		XMLCodec codec = new XMLCodec();
		Object jff_obj = null;
		try {
			jff_obj = codec.decode(file, null);
		} catch (ParseException e) {
			feedback_prelude += "\\n\t not generated properly. "
					+ "This is likely due to a problem in your Haskell code.";
			return;
		}
		
		if (jff_obj instanceof RegularExpression) {
			regex = (RegularExpression) jff_obj;
		}
		else {
			feedback_prelude += "\\n\t Not a RegularExpression";
			return;
		}
		try {
			pattern = Pattern.compile(regex.asCheckedString()
					.replace("+", "|").replaceAll("!\\*","").replace("!", ""));
		} catch (UnsupportedOperationException e) {
			feedback_prelude += "\\n\t malformed RegularExpression";
			
			return;
		} catch (PatternSyntaxException e) {
			feedback_prelude += "\\n\t malformed RegularExpression";
			return;
		}
		testable = true;
		loadTestCaseResults();
	}
	
	@Override
	public Boolean test(TestCase<Boolean> tc) {
		String test_string = tc.getTestString();
		return new Boolean(pattern.matcher(test_string).matches());
	}

	@Override
	public boolean isMissing() {
		return missing;
	}

	@Override
	public boolean isTestable() {
		return testable;
	}
	
	@Override
	public String getFeedback() {
		return feedback_prelude + super.getFeedback();
	}

}
