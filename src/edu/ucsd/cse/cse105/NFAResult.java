package edu.ucsd.cse.cse105;

import java.io.File;

import automata.fsa.FiniteStateAutomaton;

public class NFAResult extends DecisionAutomatonResult {

	public NFAResult(NFAGrader problem_grader, File file) {
		super(problem_grader, file);
	}
	
	@Override
	protected void typeCheck() {
		if (automaton instanceof FiniteStateAutomaton)
			wrong_type = false;
		else {
			wrong_type = true;
			feedback_prelude += "\\n\t not an NFA";
		}
	}
}