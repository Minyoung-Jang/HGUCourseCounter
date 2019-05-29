package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

	public static ArrayList<CSVRecord> getLines(String file, boolean removeHeader) {
		
		ArrayList<CSVRecord> lines = new ArrayList<CSVRecord>();
		
		FileReader fileReader = null;
		CSVParser csvFileParser = null;
		
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.
				withIgnoreHeaderCase().
				withIgnoreSurroundingSpaces().
				withIgnoreEmptyLines(true).
				withTrim();

		try {
			fileReader = new FileReader(file);
			csvFileParser = csvFileFormat.parse(fileReader);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Iterator<CSVRecord> iter = csvFileParser.iterator();

		try {
			while (iter.hasNext()) {
				try {
					lines.add(iter.next());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("iter.hasNext() exception");
		}
		
		if(removeHeader)
			lines.remove(0);

        try {
            fileReader.close();
            csvFileParser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	
		return lines;
	}


	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		try {
			File file = new File(targetFileName);
			FileWriter filewriter = new FileWriter(file);
			BufferedWriter bufWriter = new BufferedWriter(filewriter);

			for (String buflines : lines) {
				bufWriter.write(buflines);
				bufWriter.newLine();
			}

			bufWriter.close();

			if (!file.exists()) {
				file.createNewFile();
				System.out.println("new directory has been made");
			}
		} catch (IOException e) {
			System.out.println(e);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
