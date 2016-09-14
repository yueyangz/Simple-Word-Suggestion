package edu.upenn.cis573.predict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Main {

	private static Queue<String> wordQueue = new LinkedList<String>();

	public static void main(String[] args) {

		ArrayList<ArrayList<String>> bigList = new ArrayList<ArrayList<String>>();

		/**
		 * Error handling
		 */
		if (args.length != 1) {
			System.out.println("Wrong number of arguments!");
			System.exit(1);
		}
		File dir = new File(args[0]);
		if (!dir.exists() && !dir.isDirectory() && !dir.canRead() && !(dir.listFiles().length > 0)) {
			System.out.println("The directory is invalid. It's either non-existent, requires file permission or empty!");
			System.exit(1);
		}

		handleFileIO(dir, bigList);

		handleUserInput(bigList);

	}

	/**
	 * Helper method for reading all the files in a directory
	 * 
	 * @param dir
	 * @return
	 */
	private static File[] fileNameFilter(File dir) {
		File[] fileList = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
		return fileList;
	}

	/**
	 * Helper method for reading in files
	 * 
	 * @param dir
	 * @param bigList
	 */
	private static void handleFileIO(File dir, ArrayList<ArrayList<String>> bigList) {

		File[] files = fileNameFilter(dir);
		Scanner sc = null;
		for (File file : files) {
			try {
				sc = new Scanner(file);
				ArrayList<String> smallList = new ArrayList<String>();
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					String[] field = line.split("\t");
					if (field.length < 3)
						continue; // There are arrays with length 1
					if (field[1].length() < 1)
						continue; // There are strings with length 0
					if (Character.isLetter(field[1].charAt(0))) {
						String normalizedWord = field[1];
						smallList.add(normalizedWord);
					}
				}
				bigList.add(smallList);
			} catch (FileNotFoundException e) {
				System.out.println("File Error!");
				System.exit(1);
			}
			sc.close();
		}
	}

	/**
	 * This handles user input
	 * 
	 * @param bigList
	 */
	private static void handleUserInput(ArrayList<ArrayList<String>> bigList) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter a word: ");
		String string = null;
		Predictor predictor = new Predictor(bigList);
		while (sc.hasNextLine()) {
			string = sc.nextLine();
			if (string.equals(".")) {
				System.out.println("Goodbye!");
				break;
			}

			if (string.isEmpty()) {
				System.out.println("Please enter a word: ");
				continue;
			}
			wordQueue.add(string.trim().split(" |,|\t")[0].toLowerCase());
			String input = handleBigrams();
			System.out.println("The word(s) => " + input);
			predictor.printResult(input);
			System.out.println("Please enter a word: ");
		}
		sc.close();
	}

	/**
	 * Helper method for handling user input so that the program always looks at
	 * the most recently entered words
	 * 
	 * @return
	 */
	private static String handleBigrams() {
		if (wordQueue.isEmpty())
			return null;
		if (wordQueue.size() < 2)
			return wordQueue.peek();
		String string1 = wordQueue.remove();
		String string2 = wordQueue.peek();
		return string1.concat(" ").concat(string2);
	}

}