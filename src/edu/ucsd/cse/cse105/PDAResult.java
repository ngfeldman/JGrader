package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import automata.Configuration;
import automata.pda.PDAConfiguration;
import automata.pda.PushdownAutomaton;

public class PDAResult extends DecisionAutomatonResult {

	public PDAResult(PDAGrader problem_grader, File file) {
		super(problem_grader, file);
	}
	

	@SuppressWarnings("unchecked") // This is JFLAP's fault.
	@Override
	public Boolean test(TestCase<Boolean> tc) {
		String test_string = tc.getTestString();
		LinkedList<PDAConfiguration> queue = new LinkedList<PDAConfiguration>();
		for (Configuration c : sim.getInitialConfigurations(test_string)) {
			queue.add((PDAConfiguration) c);
		}
		int configs_so_far = queue.size();
		
		PDAConfiguration config;
		ArrayList<PDAConfiguration> next_configs = new ArrayList<PDAConfiguration>();
		while (! queue.isEmpty()) {
			config = queue.remove();
			if (config.isAccept())
				return new Boolean(true);
			next_configs = sim.stepConfiguration(config);
			configs_so_far += next_configs.size();
			if (configs_so_far >= 9999)
				return new Boolean(false);
			queue.addAll(next_configs);
		}
		return new Boolean(false);
	}
	
	@Override
	protected void typeCheck() {
		if (automaton instanceof PushdownAutomaton)
			wrong_type = false;
		else {
			wrong_type = true;
			feedback_prelude += "\\n\t not a PDA";
		}
	}
}
