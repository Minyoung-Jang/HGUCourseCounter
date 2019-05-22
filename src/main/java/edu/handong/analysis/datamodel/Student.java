package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {

	private String studentId;
	private ArrayList<Course> coursesTaken;
	private HashMap<String, Integer> semestersByYearAndSemester;

	public Student(String studentId) {
		this.setStudentId(studentId);
		coursesTaken = new ArrayList<Course>();
	}

	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
	}

	public HashMap<String, Integer> getSemestersByYearAndSemester() {

		semestersByYearAndSemester = new HashMap<String, Integer>();

		int semesterCount = 1;
		String semesterInfo="", temp="";

		for (int i = 0; i < coursesTaken.size(); i++) {
			semesterInfo = Integer.valueOf(coursesTaken.get(i).getYearTaken()) + "-"
					+ Integer.valueOf(coursesTaken.get(i).getSemesterCourseTaken());
			if (temp.equals(semesterInfo)) {
				continue;
			}else {
				semestersByYearAndSemester.put(semesterInfo, semesterCount++);
				temp = semesterInfo;
			}
		}
		
		return semestersByYearAndSemester;
	}

	
	public int getNumCourseInNthSementer(int semester) {
		int count = 0;
		String semesterInfo;
		
		for (int i = 0; i < coursesTaken.size(); i++) {
			semesterInfo = Integer.valueOf(coursesTaken.get(i).getYearTaken()) + "-"
					+ Integer.valueOf(coursesTaken.get(i).getSemesterCourseTaken());
			if (semester == getSemestersByYearAndSemester().get(semesterInfo)){
				count++;
			}
		}
		return count;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	

}
