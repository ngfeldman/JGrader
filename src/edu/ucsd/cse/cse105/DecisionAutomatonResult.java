package edu.ucsd.cse.cse105;

import java.io.File;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.fsa.FSAStepWithClosureSimulator;
import automata.fsa.FiniteStateAutomaton;
import automata.pda.PDAStepWithClosureSimulator;
import automata.pda.PushdownAutomaton;
import automata.turing.TMSimulator;
import automata.turing.TuringMachine;
import file.ParseException;
import file.XMLCodec;

public class DecisionAutomatonResult extends DecisionProblemResult {
	private boolean missing = true;
	private boolean testable = false;
	protected boolean wrong_type;
	
	protected int actual_states = 0;
	
	protected String feedback_prelude = "";
	protected Automaton automaton;
	protected AutomatonSimulator sim;
	
	public DecisionAutomatonResult(DecisionAutomatonGrader problem_grader, File file) {
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
		
		if (jff_obj instanceof FiniteStateAutomaton
				|| jff_obj instanceof PushdownAutomaton
				|| jff_obj instanceof TuringMachine) {
			testable = true;
			automaton = (Automaton) jff_obj;
			actual_states = automaton.getStates().length;
		}
		
		typeCheck();
		if (testable)
			setSimulator();
		loadTestCaseResults();
	}

	protected void typeCheck() {
		if (isTestable())
			wrong_type = false;
		else {
			wrong_type = true;
			feedback_prelude += "\\n\t not a DecisionAutomaton";
		}
	}
	
	protected void setSimulator() {
		if (automaton instanceof FiniteStateAutomaton) {
			sim = new FSAStepWithClosureSimulator(automaton);
		}
		else if (automaton instanceof PushdownAutomaton) {
			sim = new PDAStepWithClosureSimulator(automaton);
			((PDAStepWithClosureSimulator) sim).setAcceptByFinalState();
		}
		else if (automaton instanceof TuringMachine) {
			sim = new TMSimulator(automaton);
		}
		try {
			test(new TestCase<Boolean>("00110", true));
		} catch (NullPointerException e) {
			if (sim == null)
				throw e;
			testable = false;
			feedback_prelude += "\\n\t Something wrong with file. Perhaps there's no start state?";
		}
	}
	
	@Override
	public Boolean test(TestCase<Boolean> tc) {
		return sim.simulateInput(tc.getTestString());
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
	public boolean isCorrect() {
		return (! isWrongType()) && statesCorrect() && allCasesCorrect();
	}
	
	public boolean statesCorrect() {
		//TODO: implement functionality to grade based on number of states
		return true;
	}
	
	public boolean isWrongType() {
		return wrong_type;
	}

	public int getExpectedStates() {
		return getProblemGrader().getExpectedStates();
	}
	
	public int getActualStates() {
		return actual_states;
	}
	
	@Override
	public String getFeedback() {
		return feedback_prelude + super.getFeedback();
	}
	
	public DecisionAutomatonGrader getProblemGrader() {
		return (DecisionAutomatonGrader) problem_grader;
	}
}
