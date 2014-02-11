package edu.ucsd.cse.cse105;

import java.io.File;

import automata.AutomatonChecker;
import automata.fsa.FiniteStateAutomaton;

public class DFAResult extends NFAResult {
	public DFAResult(NFAGrader problem_grader, File file) {
		super(problem_grader, file);
	}
	
	@Override
	protected void typeCheck() {
		AutomatonChecker ac = new AutomatonChecker();
		if (! ac.isNFA((FiniteStateAutomaton) automaton))
			wrong_type = false;
		else {
			wrong_type = true;
			feedback_prelude += "\\n\t not a DFA";
		}
	}
}
