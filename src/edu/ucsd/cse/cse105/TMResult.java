package edu.ucsd.cse.cse105;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import automata.Configuration;
import automata.turing.TMConfiguration;
import automata.turing.TuringMachine;

public class TMResult extends DecisionAutomatonResult {

	public TMResult(TMGrader problem_grader, File file) {
		super(problem_grader, file);
	}
	

	@SuppressWarnings("unchecked") // This is JFLAP's fault.
	@Override
	public Boolean test(TestCase<Boolean> tc) {
		String test_string = tc.getTestString();
		LinkedList<TMConfiguration> queue = new LinkedList<TMConfiguration>();
		for (Configuration c : sim.getInitialConfigurations(test_string)) {
			queue.add((TMConfiguration) c);
		}
		int configs_so_far = queue.size();
		
		TMConfiguration config;
		ArrayList<TMConfiguration> next_configs = new ArrayList<TMConfiguration>();
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
		if (automaton instanceof TuringMachine)
			wrong_type = false;
		else {
			wrong_type = true;
			feedback_prelude += "\\n\t not a TM";
		}
	}
}
