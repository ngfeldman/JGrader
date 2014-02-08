package edu.ucsd.cse.cse105;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public abstract class ProblemGrader {
	private String file_name;
	private String type;
	
	public ProblemGrader(String file_name, String type) {
		this.file_name = file_name;
		this.type = type;
	}
	
	public String getFileName() {
		return new String(file_name);
	}
	
	public String getType() {
		return new String(type);
	}
	
	@SuppressWarnings("unchecked")
	public static ProblemGrader createProblemGrader(String file_name, String type, String[] package_names, String[] test_info, int line_number){
		Class<? extends ProblemGrader> c = null;
		try {
			System.out.println(type+"Grader");
			c = (Class<? extends ProblemGrader>) Class.forName(type+"Grader");
		} catch (ClassNotFoundException e) {
		}
		if (c == null) {
			for (int i = package_names.length-1; i >= 0; i--) {
				String package_name = package_names[i];
				System.out.println(package_name+"."+type+"Grader");
				try {
					c = (Class<? extends ProblemGrader>) Class.forName(package_name+"."+type+"Grader");
					break;
				} catch (ClassNotFoundException e) {
				}
			}
		}
		if (c == null) {
			throw new RuntimeException("line " + line_number + " of "
					+ file_name +": No ProblemGrader subclass for problem type " + type);
		}
		if (!c.isAssignableFrom(ProblemGrader.class)) {
			throw new RuntimeException("line " + line_number + " of "
					+ file_name +": grader class for problem type " + type + " does not extend ProblemGrader");
		}
		Constructor<? extends ProblemGrader> cons;
		try {
			cons = (Constructor<? extends ProblemGrader>) c.getConstructor(String.class, String[].class, int.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(c.getName() + " does not implement the required"
					+ "constructor, taking (String file_name, String[] test_info, int line_number)");
		}
		ProblemGrader pg;
		try {
			pg = cons.newInstance(file_name, test_info, line_number);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return pg;
	}
	
	public static ArrayList<ProblemGrader> createProblemGraders(File tests_file) {
		ArrayList<ProblemGrader> list = new ArrayList<ProblemGrader>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(tests_file));
			ArrayList<String> package_names = new ArrayList<String>();
			package_names.add(ProblemGrader.class.getPackage().getName());
			ArrayList<String> test_info = null;
			String type = null;
			String line;
			String file_name = null;
			int line_number = 0;
			while((line = br.readLine()) != null) {
				line_number++;
				line = line.trim();
				if (line.equals("") || line.startsWith("#"))
					continue;
				if (line.startsWith("package")) {
					String[] fields = line.split("\\s+");
					if (fields.length == 2) {
						package_names.add(fields[1]);
					}
				}
				if (line.charAt(line.length()-1) == ':') {
					if (test_info != null) {
						list.add(createProblemGrader(file_name, type,
								(String[]) package_names.toArray(),
								(String[]) test_info.toArray(), line_number));
					}
					file_name = line.substring(0,line.length()-1);
					line = br.readLine();
					if (line == null) {
						br.close();
						throw new RuntimeException("Last file listed has no type, after line " + line_number + ".");
					}
					type = line;
					test_info = new ArrayList<String>();
					continue;
				}
				if (test_info == null) {
					br.close();
					throw new RuntimeException("Expected file name at line "
							+ line_number + " of " + tests_file.getName() +". Got: " + line);
				}
				test_info.add(line.trim());
				/*String[] fields = line.split("\\s+");
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
						System.err.println("Line " + line_number + " bad: "+ line);
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
						System.err.println("Line " + line_number + " bad: "+ line);
						continue;
					}
					testString = fields[0];
				}
				else {
					System.err.println("Line " + line_number + " bad: "+ line);
					continue;
				}
				currentSuite.addTestCase(new TestCase(accept, testString));*/
			}
			if (test_info != null) {
				list.add(createProblemGrader(file_name, type,
						(String[]) package_names.toArray(),
						(String[]) test_info.toArray(), line_number));
			}
			br.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
		return list;
	}
}
