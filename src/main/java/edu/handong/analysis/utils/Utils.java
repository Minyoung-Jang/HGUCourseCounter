package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

	public static ArrayList<String> getLines(String file, boolean removeHeader)
	{
        ArrayList<String> lines = new ArrayList<String>();
        String bufline;
        
		try{
            File fileName = new File(file);
            if(!fileName.exists()) {
            	throw new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
            }
            FileReader filereader = new FileReader(fileName);
            BufferedReader bufReader = new BufferedReader(filereader);
            while((bufline = bufReader.readLine())!=null)
            	lines.add(bufline);
            if(removeHeader)
            	lines.remove(0);
            bufReader.close();
        }catch (FileNotFoundException e) {
        	System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }catch(NotEnoughArgumentException e) {
        	System.out.println(e);
        }
		return lines;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		try {
			File file = new File(targetFileName);
            FileWriter filewriter = new FileWriter(file);
            BufferedWriter bufWriter = new BufferedWriter(filewriter);
            
            for(String buflines : lines) {
            	bufWriter.write(buflines);
            	bufWriter.newLine();
            }
            
            bufWriter.close();
            	
			if(!file.exists()) {
				file.mkdir();
				System.out.println("new directory has been made");
			}
		}catch(IOException e) {
			System.out.println(e);
		}catch(Exception e) {
			e.getStackTrace();
		}
	}


}
