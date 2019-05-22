package edu.handong.analysis.utils;

public class NotEnoughArgumentException extends Exception {

	public NotEnoughArgumentException() {
		super("There is not enough argument excetption!");
	}
	
	public NotEnoughArgumentException(String message) {
		super("There is not enough argument excetption!\n" + message);
	}


}
