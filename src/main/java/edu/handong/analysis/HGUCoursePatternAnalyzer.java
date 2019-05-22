package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {

		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
		//Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		
		// TODO: Implement this method
		HashMap<String,Student> studentCourseRecords = new HashMap<String,Student>();
		int i, tempID = 1;
		
		String stuID;
		Course CourseRecord = new Course(lines.get(0));
		Student students = new Student(CourseRecord.getStudentId());
		students.addCourse(CourseRecord);
		studentCourseRecords.put(CourseRecord.getStudentId(), students);
		stuID=CourseRecord.getStudentId();
			
		for(i = 1; i < lines.size(); i++) {
			CourseRecord = new Course(lines.get(i));
			stuID = CourseRecord.getStudentId();
			
			if(Integer.parseInt(stuID)==tempID) {
				students.addCourse(CourseRecord);
			}else {
				students = new Student(stuID);
				students.addCourse(CourseRecord);
			}
			studentCourseRecords.put(stuID, students);
			if(tempID == Integer.parseInt(stuID)) {
				studentCourseRecords.put(stuID, students);
			}else {
				tempID++;
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
		String stuID="0001", totalNumberOfSemester, semester, numCoursesTaking;
		
		countNumber.add("StudentID,TotalNumberOfSemestersRegistered,Semester,NumCoursesTakenInTheSemester");
		
		while(sortedStudents.get(stuID) != null){
			Student studentOne = sortedStudents.get(stuID);
			totalNumberOfSemester = Integer.toString(studentOne.getSemestersByYearAndSemester().size());
			for(int j = 1; j <= Integer.parseInt(totalNumberOfSemester); j++) {
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
}
