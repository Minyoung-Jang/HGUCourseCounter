package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVRecord;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	//Options
	boolean help;
	String input;
	String output;
	String analysis;
	String startyear;
	String endyear;
	String coursecode;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		Options options = createOptions();
		
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				System.exit(0);;
			}
		
			String dataPath = input; // file to be analyzed
			String resultPath = output; // the file path where the results are saved.
			
			ArrayList<CSVRecord> lines = Utils.getLines(dataPath, true);
			students = loadStudentCourseRecords(lines);
			Map<String, Student> sortedStudents = new TreeMap<String,Student>(students);
			
			if(analysis.equals("1")) 
			{
				// Generate result lines to be saved.
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				// Write a file (named like the value of resultPath) with linesTobeSaved. 
				Utils.writeAFile(linesToBeSaved, resultPath);
			}
			else if(analysis.equals("2"))
			{	
				// 0) Set structure of result file
				ArrayList<String> result = new ArrayList<String>();
				result.add("Year,Semester,CourseCode,CourseName,TotalStudents,StudentsTaken,Rate");
				
		        // 1) Count the "number of students" who take the course in which "semester".
		        // 1-1) Check the all courses each students has.
				// 1-2) if there is same course code with the argument of option "c", then StudentTaken++.
				// 1-3) else initialize the count.
				
				HashMap<String, Integer> studentTaken = new HashMap<String, Integer>();
				
				for(String stuID : sortedStudents.keySet()) 
				{
					Student eachStudent = sortedStudents.get(stuID);
				
					for(Course courses : eachStudent.getCoursesTaken()) 
					{
						String semesterInfo = courses.getYearTaken() + "-" + courses.getSemesterCourseTaken();
						
						if(courses.getCourseCode().equals(coursecode)) 
						{
							if(studentTaken.isEmpty()) {
								studentTaken.put(semesterInfo, 1);
							}else {
								if(studentTaken.containsKey(semesterInfo)) {
									studentTaken.put(semesterInfo, studentTaken.get(semesterInfo)+1);
								}else {
									studentTaken.put(semesterInfo, 1);
								}
							}
						}
					}
				}
				
				// 2) Count the total number of students in the relevant semester.
				// 2-1)	Check the semesterInfo(the key of the studentTaken) to count the registered students in the semester.
				// 2-2) Count all students who were register in the semester.
				// 2-2-1) if the student was registered in the relevant semester, then totalStudents++.
				
				HashMap<String, Integer> totalStudents = new HashMap<String, Integer>();
				
				for(String relevantSemester : studentTaken.keySet()) {;
					totalStudents.put(relevantSemester, 0);
					for(String stuID : sortedStudents.keySet()) {
						if(sortedStudents.get(stuID).getSemestersByYearAndSemester().containsKey(relevantSemester)) {
							totalStudents.put(relevantSemester, totalStudents.get(relevantSemester)+1);
						}
					}
				}
				
				// 3) Put the data in order("Year,Semester,CourseCode,CourseName,TotalStudents,StudentsTaken,Rate")
				// 3-1) Still need CourseName matched with CourseCode, and the Rate of taking a course in a specific semester.
				// 3-2) CourseName should be matched with courseCode.
				// 3-3) Rate = StudentsTaken / TotalStudents * 100.
				
				Map<String, Integer> sortedResult = new TreeMap<String,Integer>(totalStudents);
				String courseName = null;
				float rate;
				
				for(CSVRecord courseCheck : lines) {
					if((courseCheck.get(4)).equals(coursecode)) {
						courseName = courseCheck.get(5);
						break;
					}
				}
				
				for(String key : sortedResult.keySet()) 
				{
					rate = (float)studentTaken.get(key) / (float)totalStudents.get(key) * 100;
				
					String dataInOrder = key.split("-")[0] + ","
							   		   + key.split("-")[1] + ","
							   		   + coursecode + ","
							   		   + courseName + ","
							   		   + totalStudents.get(key) + ","
							   		   + studentTaken.get(key) + ","
							   		   + String.format("%.1f", rate) + "%";
					result.add(dataInOrder);
				}
				// #) Finally, write the result to the output file. 
				Utils.writeAFile(result, resultPath);
			}
			else 
			{
				System.out.println("Wrong analysis argument!!\nPlease try again.");
			}
		}
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<CSVRecord> lines) {
		
		// TODO: Implement this method
		HashMap<String,Student> studentCourseRecords = new HashMap<String,Student>();
		Student students = new Student(lines.get(0).get(0));
			
		for(CSVRecord line : lines) {
			Course CourseRecord = new Course(line);
			int currentyear = CourseRecord.getYearTaken();
			
			if(students.getStudentId().equals(CourseRecord.getStudentId()) && currentyear >= Integer.parseInt(startyear) && currentyear <= Integer.parseInt(endyear)){
				students.addCourse(CourseRecord);
				studentCourseRecords.put(CourseRecord.getStudentId(), students);
			}else {
				students = new Student(CourseRecord.getStudentId());
				students.addCourse(CourseRecord);
			}
		}
		return studentCourseRecords; // do not forget to return a proper variable.
	}
	
	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semesters in total. In the first semester (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		// TODO: Implement this method
		ArrayList<String> countNumber = new ArrayList<String>();
		String totalNumberOfSemester, semester, numCoursesTaking;
		
		countNumber.add("StudentID,TotalNumberOfSemestersRegistered,Semester,NumCoursesTakenInTheSemester");
		
		for(String stuID : sortedStudents.keySet()){
			Student studentOne = sortedStudents.get(stuID);
			totalNumberOfSemester = Integer.toString(studentOne.getSemestersByYearAndSemester().size());
			for(int j = 1; j <= studentOne.getSemestersByYearAndSemester().size(); j++) {
				semester = Integer.toString(j);
				numCoursesTaking = Integer.toString(studentOne.getNumCourseInNthSementer(j));
				countNumber.add(stuID+","
							+totalNumberOfSemester+","
							+semester+","
							+numCoursesTaking);
			}
			int k = Integer.parseInt(stuID)+1;
			if(k < 10) {
				stuID = "000" + Integer.toString(k);
			}else if(k < 100) {
				stuID = "00" + Integer.toString(k);
			}
			else if(k < 1000) {
				stuID = "0" + Integer.toString(k);
			}else {
				stuID = Integer.toString(k);
			}
		}
		return countNumber;
	}
	
	private boolean parseOptions(Options options, String[] args)  {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			analysis = cmd.getOptionValue("a");
	        startyear = cmd.getOptionValue("s");
	        endyear = cmd.getOptionValue("e");
			coursecode = cmd.getOptionValue("c");
			help = cmd.hasOption("h");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		
		try {
			if(args.length<3) {
				throw new NotEnoughArgumentException();
			}
		}catch (NotEnoughArgumentException e) {
				System.out.println(e.getMessage());
				System.exit(0);
		}

		return true;
	}
	
	// Definition Stage
	Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()     
				.argName("Output path")
				.required() 
				.build());
		
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()
				.argName("Analysis option")
				.required()
				.build());
		
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()
				.argName("course code")
				.build());
		
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Show a Help page")
		        .argName("Help")
		        .build());

		return options;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("CLIExample", header, options, footer, true);
	}

}
