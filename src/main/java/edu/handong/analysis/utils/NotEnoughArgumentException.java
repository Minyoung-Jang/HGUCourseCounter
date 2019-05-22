package edu.handong.analysis.utils;


@SuppressWarnings("serial")
public class NotEnoughArgumentException extends Exception {
	
	public NotEnoughArgumentException() {
		super.setStackTrace(getStackTrace());
		System.out.println("There is not enough argument excetption!");
	}
	
	public NotEnoughArgumentException(String message) {
		super.setStackTrace(getStackTrace());
		System.out.println("There is not enough argument excetption!\n" + message);
	}


}
