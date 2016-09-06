package edu.upenn.cis573.predict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

	public static void main(String[] args) {

		Scanner sc = null;
		ArrayList<String> bigList = new ArrayList<String>();
		TreeMap<Pair, Integer> mapping = new TreeMap<Pair, Integer>();

		/**
		 * Error handling
		 */
		if (args.length != 1) {
			System.exit(1);
		}
		File dir = new File(args[0]);
		if (!dir.exists() && !dir.isDirectory() && !dir.canRead() && !(dir.listFiles().length > 0)) {
			System.exit(1);
		}

		/**
		 * Read in files
		 */
		File[] files = fileNameFilter(dir);
//		for (File file : files) {
			try {
				sc = new Scanner(files[1]);
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					String[] field = line.split("\t");
					if (Character.isLetter(field[0].charAt(0)) && field.length == 3) {
						String normalizedWord = field[1];
						bigList.add(normalizedWord);
//						System.out.print(normalizedWord + " ");
					}
					

				}
			} catch (FileNotFoundException e) {
				System.out.println("File Error!");
				System.exit(1);
			}
//		}

		String string = handleUserInput();
		Predictor predictor = new Predictor(bigList);
//		predictor.printResult();

	}

	private static File[] fileNameFilter(File dir) {
		File[] fileList = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
		return fileList;
	}
	
	private static String handleUserInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter a word: ");
		String string = null;
		while (sc.hasNext()) {
			string = sc.next();
			string = string.trim().split(" ")[0].toLowerCase();
			boolean valid = checkInput(string);
			if (!valid) {
				System.out.println("Please enter a word: ");
				continue;
			} else {
				break;
			}
		}
		sc.close();
		return string;
		
	}
	
	private static boolean checkInput(String string) {
		boolean valid = true;
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isLetter(string.charAt(i))) {
				valid = false;
				System.out.println("Invalid word! Do not include non-alphabets!");
				break;
			}
		}
		return valid;
	}

}
