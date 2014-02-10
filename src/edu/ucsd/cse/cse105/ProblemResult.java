package edu.ucsd.cse.cse105;

import java.io.File;

public abstract class ProblemResult {

	protected ProblemGrader problem_grader;
	protected File file;
	
	public abstract boolean isCorrect();
	public abstract boolean isMissing();
	public abstract boolean isTestable();
	
	public ProblemResult(ProblemGrader problem_grader, File file) {
		this.problem_grader = problem_grader;
		this.file = file;
	}
	
	public ProblemGrader getProblemGrader() {
		return problem_grader;
	}
	
	public String getFileName() {
		return problem_grader.getFileName();
	}
	
	public abstract String getFeedback();
	
	protected void throwMissingFileException() {
		if (isMissing())
			throw new NullPointerException("Cannot return value because "
					+ problem_grader.getFileName() + " was missing.");
		if (! isTestable())
			throw new NullPointerException("Cannot return value because "
					+ problem_grader.getFileName() + " was not testable.");
	}
}
