package edu.ucsd.cse.cse105;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public abstract class ProblemGrader {
	protected File tests_file;
	private String file_name;
	private String type;
	
	public ProblemGrader(String file_name, File tests_file, String[] test_info, int line_number) {
		this.file_name = file_name;
		this.tests_file = tests_file;
		this.type = this.getClass().getSimpleName();
		this.type = this.type.substring(0, this.type.lastIndexOf("Grader"));
		onInstantiate(test_info, line_number);
	}

	protected abstract void onInstantiate(String[] test_info, int line_number);
	
	public String getFileName() {
		return new String(file_name);
	}
	
	public String getType() {
		return new String(type);
	}
	
	public abstract ProblemResult grade(File file);
	
	@SuppressWarnings("unchecked")
	public static ProblemGrader createProblemGrader(String file_name, File tests_file, String type, String[] package_names, String[] test_info, int line_number){
		Class<? extends ProblemGrader> c = null;
		try {
			c = (Class<? extends ProblemGrader>) Class.forName(type+"Grader");
		} catch (ClassNotFoundException e) {
		}
		if (c == null) {
			for (int i = package_names.length-1; i >= 0; i--) {
				String package_name = package_names[i];
				try {
					c = (Class<? extends ProblemGrader>) Class.forName(package_name+"."+type+"Grader");
					break;
				} catch (ClassNotFoundException e) {
				}
			}
		}
		if (c == null) {
			throw new RuntimeException("line " + line_number
					+ " of tests file: No ProblemGrader subclass for problem type " + type);
		}
		if (!ProblemGrader.class.isAssignableFrom(c)) {
			throw new RuntimeException("line " + line_number
					+ " of tests file: grader class for problem type "
					+ type + " does not extend ProblemGrader");
		}
		Constructor<? extends ProblemGrader> cons;
		try {
			cons = (Constructor<? extends ProblemGrader>) c.getConstructor(String.class, File.class, String[].class, int.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(c.getName() + " does not implement the required"
					+ "constructor, taking (String file_name, String[] test_info, int line_number)");
		}
		ProblemGrader pg;
		try {
			pg = cons.newInstance(file_name, tests_file, test_info, line_number);
		} catch (Exception e) {
			if (e.getCause() instanceof RuntimeException)
				throw (RuntimeException) e.getCause();
			else
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
			int test_info_starting_line_number = -1;
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
					continue;
				}
				if (line.charAt(line.length()-1) == ':') {
					if (test_info != null) {
						String[] strs = new String[0];
						list.add(createProblemGrader(file_name, tests_file, type, package_names.toArray(strs),
								test_info.toArray(strs), test_info_starting_line_number));
					}
					file_name = line.substring(0,line.length()-1);
					line = br.readLine();
					if (line == null) {
						br.close();
						throw new RuntimeException("Last file listed has no type, after line " + line_number + ".");
					}
					type = line;
					test_info = new ArrayList<String>();
					test_info_starting_line_number = line_number+1;
					continue;
				}
				if (test_info == null) {
					br.close();
					throw new RuntimeException("Expected file name at line "
							+ line_number + " of " + tests_file.getName() +". Got: " + line);
				}
				test_info.add(line.trim());
			}
			if (test_info != null) {
				String[] strs = new String[0];
				list.add(createProblemGrader(file_name, tests_file, type, package_names.toArray(strs),
						test_info.toArray(strs), test_info_starting_line_number));
			}
			br.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}

		return list;
	}
}
