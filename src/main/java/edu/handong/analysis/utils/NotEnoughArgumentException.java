package edu.handong.analysis.utils;


@SuppressWarnings("serial")
public class NotEnoughArgumentException extends Exception {
	
	public NotEnoughArgumentException() {
		//super.setStackTrace(getStackTrace());
		System.out.println("No CLI argument Exception! Please put a file path.");
	}

	public NotEnoughArgumentException(String message) {
		//super.setStackTrace(getStackTrace());
		System.out.println(message);
		System.exit(0);
	}


}
