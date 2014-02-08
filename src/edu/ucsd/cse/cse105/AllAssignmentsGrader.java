package edu.ucsd.cse.cse105;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * AllAssignmentsGrader is a main class that grades all students assignments
 * and outputs a CSV file mapping each student (supposed to be from Gradesource) 
 * to their score on these problems.
 * 
 * Arguments are: path to student list file, path to ACMS student account list file,
 * path to file listing problems (by file name in student submission, eg. p2a.jff)
 * and test cases, path to directory containing submission subdirectories (each 
 * subdirectory having the same name as the student's account)
 * 
 * @author Nathan Feldman
 *
 */
public class AllAssignmentsGrader {
	public static void main(String[] args) {
		if (args.length != 5){
			System.err.println("Incorrect number of arguments");
			System.err.println("Syntax: java AllAssignmentsGrader " +
			        "<hw1|hw2|...> " +
					"<gradesource_students_file> <ACMS_accounts_file> " +
					"<test_cases_file> <directory_of_submissions>");
			return;
		}
		String assignment = args[0];
		File studentsFile = new File(args[1]);
		File accountsFile = new File(args[2]);
		File testsFile = new File(args[3]);
		File submissionsDir = new File(args[4]);
		if (!studentsFile.canRead()) {
			System.err.println("can't read gradesource_students_file");
			return;
		}
		if (!accountsFile.canRead()) {
			System.err.println("can't read ACMS_accounts_file");
			return;
		}
		if (!testsFile.canRead()) {
			System.err.println("can't read test_cases_file");
			return;
		}
		if (!submissionsDir.canRead()) {
			System.err.println("can't read directory_of_submissions");
			return;
		}
		
		AssignmentGrader grader = getGrader(assignment);
		
		HashMap<String, String> ar[] = createStudentsToIds(studentsFile);
		HashMap<String, String> studentsToIds = ar[0];
		HashMap<String, String> idsToRealNames = ar[1];
		HashMap<String, String> accountsToStudents = createAccountsToStudents(accountsFile);;
		ArrayList<TestSuite> testSuites = TestSuite.createTestSuites(testsFile);
		HashMap<String, String> namesToScoresAndFeedback = gradeAllAssignments(
				grader, submissionsDir, studentsToIds, accountsToStudents, testSuites);
		ArrayList<String> rows = new ArrayList<String>();
		for (Entry<String, String> entry : studentsToIds.entrySet()) {
			String name = entry.getKey();
			String emailId = entry.getValue();
			String scoresAndFeedback = namesToScoresAndFeedback.remove(name);
			if (scoresAndFeedback != null)
				rows.add(idsToRealNames.get(emailId) + ";" + emailId + ";" + scoresAndFeedback);
			else
				rows.add(idsToRealNames.get(emailId) + ";" + emailId + ";");
		}
		Collections.sort(rows);
		for (Entry<String, String> entry : namesToScoresAndFeedback.entrySet()) {
			String nameOrAccount = entry.getKey();
			rows.add(nameOrAccount + ";?;?;" + entry.getValue());
		}
		for (String row : rows)
			System.out.println(row);
	}
	
	public static HashMap<String, String>[] createStudentsToIds(File file) {
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
		
			String line;
			while((line = br.readLine()) != null) {
				String[] fields = line.replace("\"","").split(",");
				String last = fields[0].trim();
				String[] firstMiddle = fields[1].trim().split("\\s+");
				String name = last + ",";
				//for (int i=0; i<firstMiddle.length;i++)
					//name += " " + firstMiddle[i];
				name += " " + firstMiddle[0];
				/*if (firstMiddle.length >= 2)
					name += " " + firstMiddle[1].charAt(0);*/
				//TODO: take special case mappings and put them in a separate file, or get a better record of accounts from ieng6
				if (name.equalsIgnoreCase("Cheung Wu, Rolando"))
					name = "Cheung-Wu, Rolando";
				if (name.equalsIgnoreCase("Ching Hau, Juan"))
					name = "CHING, JUAN";
				if (name.equalsIgnoreCase("Mo Song, Edwin"))
					name = "MO-SONG, EDWIN";
				if (name.equalsIgnoreCase("Gustavsson Falt, Mattias"))
					name = "Gustavsson-falt, Mattias";
				String email = fields[2];
				String id = fields[3];
				map.put(name.toUpperCase(), email+";"+id);
				map2.put(email+";"+id, fields[0].trim()+", "+fields[1].trim());
			}
	
			br.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
		@SuppressWarnings("unchecked")
		HashMap<String, String> result[] = new HashMap[2];
		result[0] = map;
		result[1] = map2;
		return result;
	}
	
	public static HashMap<String, String> createAccountsToStudents(File file) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
		
			String line;
			while((line = br.readLine()) != null) {
				String[] fields = line.split(":");
				if (fields.length < 5) {
					System.err.println("bad line: " + line);
					continue;
				}
				String[] groups = fields[4].split(",");
				boolean cs105w = false;
				for (int i=groups.length-1; i>=0; i--) {
					if (groups[i].equals("cs105w")) {
						cs105w = true;
						break;
					}
				}
				/*if (!cs105w)
					continue;*/
				String[] names = fields[2].split(" ");
				String name = null;
				if (names.length == 3) {
					name = names[2] + ", " + names[0];// + " " + names[1];
				}
				else if (names.length == 2){
					name = names[1] + ", " + names[0];
				}
				else {
					if (cs105w){
						System.err.println("This person doesn't match: " + fields[2]);
						System.err.println("Adding name as is");
					}
					name = fields[2];
				}
				String account = fields[0];
				map.put(account, name.toUpperCase());
			}
			br.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
		return map;
	}

	public static HashMap<String, String> gradeAllAssignments(
			AssignmentGrader grader,
			File submissionsDir, 
			HashMap<String, String> studentsToIds,
			HashMap<String, String> accountsToStudents,
			ArrayList<TestSuite> testSuites) {
		File[] submissions = submissionsDir.listFiles();
		Arrays.sort(submissions);
		HashMap<String, String> result = new HashMap<String, String>();
		
		for (File submission : submissions) {
			if (!submission.isDirectory()) {
				System.err.println("Skipping non-directory: " + submission.getName());
				continue;
			}
			String account = submission.getName();
			String name = accountsToStudents.get(account);
			String id = null;
			if (name == null) {
				System.err.println("No name for account " + account);
			}
			else {
				id = studentsToIds.get(name);
				if (id == null) {
					System.err.println("No id for " + name + " with account "+ account);
				}
			}
			String scoreAndFeedback =
					grader.gradeAssignment(submission, testSuites);
			if (id != null){
				result.put(name, scoreAndFeedback);
			}
			else if (name != null) {
				result.put(name, scoreAndFeedback);
			}
			else {
				result.put(account, scoreAndFeedback);
			}
		}
		return result;
	}
	
	public static AssignmentGrader getGrader(String assignment) {
		if (assignment.equalsIgnoreCase("hw1"))
			return new HW1Grader();
		if (assignment.equalsIgnoreCase("hw2"))
			return new HW2Grader();
		return null;
	}
}