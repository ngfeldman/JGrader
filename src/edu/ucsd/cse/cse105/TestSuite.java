package edu.ucsd.cse.cse105;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class TestSuite implements Iterable<TestCase>{
	private String fileName;
	
	private String type;
	private int states;
	
	public TestSuite(String fileName, ArrayList<TestCase> cases, String type, int states) 
			throws IllegalArgumentException {
		if (states < 0)
			throw new IllegalArgumentException("\"states\" must be 0 or positive");
		this.fileName = fileName;
		this.cases = cases==null ? new ArrayList<TestCase>() : cases;
		this.type = new String(type);
		this.states = states;
	}
	
	public TestSuite(String fileName, ArrayList<TestCase> cases, String type) {
		this(fileName, cases, type, 0);
	}
	
	public TestSuite(String fileName, String type) {
		this(fileName, new ArrayList<TestCase>(), type);
	}
	
	public TestSuite(String fileName, ArrayList<TestCase> cases) {
		this(fileName, cases, "Automaton");
	}
	
	public TestSuite(String fileName) {
		this(fileName, new ArrayList<TestCase>());
	}
	
	public String getFileName() {
		return fileName;
	}
	
	private ArrayList<TestCase> cases;
	
	public TestCase getTestCase(int i) {
		return cases.get(i);
	}
	
	/*
	 * Warning: linear time for lookup
	 */
	public TestCase getTestCase(String test_string) {
		for (TestCase tc : cases)
			if (tc.getTestString().equals(test_string))
				return tc;
		return null;
	}
	
	public void addTestCase(TestCase c){
		cases.add(c);
	}
	
	public Iterator<TestCase> iterator() {
		return cases.iterator();
	}
	
	public int size() {
		return cases.size();
	}
	
	public String getType() {
		return new String(type);
	}

	public int getStates() {
		return states;
	}
	
	public void setType(String type) {
		this.type = new String(type);
	}
	
	public void setStates(int states) {
		this.states = states;
	}
	
	
	//TODO: build into AllTestSuitesResults class? move method into one of those classes?
	public static ArrayList<TestSuite> createTestSuites(File file) {
		ArrayList<TestSuite> list = new ArrayList<TestSuite>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
		
			String line;
			TestSuite currentSuite = null;
			String currentJffFileName;
			int lineNumber = 0;
			while((line = br.readLine()) != null) {
				lineNumber++;
				line = line.trim();
				if (line.equals("") || line.startsWith("#"))
					continue;
				if (line.charAt(line.length()-1) == ':') {
					currentJffFileName = line.substring(0,line.length()-1);
					currentSuite = new TestSuite(currentJffFileName);
					list.add(currentSuite);
					continue;
				}
				String[] fields = line.split("\\s+");
				if (fields.length == 0)
					continue;
				if (fields.length == 1  && !fields[0].equalsIgnoreCase("reject")
						&& !fields[0].equalsIgnoreCase("accept")) {
					String type = fields[0];
					currentSuite.setType(type);
					continue;
				}
				if (fields.length == 2 && line.matches("[0-9]+\\s+states")) {
					int states = Integer.parseInt(fields[0]);
					currentSuite.setStates(states);
					continue;
				}
				boolean accept;
				String testString;
				if (fields.length == 1) {
					if (fields[0].equalsIgnoreCase("accept")) {
						accept = true;
					}
					else if (fields[0].equalsIgnoreCase("reject")) {
						accept = false;
					}
					else {
						System.err.println("Line " + lineNumber + " bad: "+ line);
						continue;
					}
					testString = "";
				}
				else if (fields[0].equalsIgnoreCase("accept")) {
					accept = true;
					testString = fields[1];
				}
				else if (fields[0].equalsIgnoreCase("reject")) {
					accept = false;
					testString = fields[1];
				}
				else if (fields.length == 2){
					if (fields[1].equalsIgnoreCase("accept"))
						accept = true;
					else if (fields[1].equalsIgnoreCase("reject"))
						accept = false;
					else {
						System.err.println("Line " + lineNumber + " bad: "+ line);
						continue;
					}
					testString = fields[0];
				}
				else {
					System.err.println("Line " + lineNumber + " bad: "+ line);
					continue;
				}
				currentSuite.addTestCase(new TestCase(accept, testString));
			}
			br.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
		return list;
	}
}
