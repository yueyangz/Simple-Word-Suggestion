package edu.upenn.cis573.predict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class implements the prediction algorithm
 * 
 * @author Yueyang
 *
 */
public class Predictor {

	private HashMap<String, ArrayList<String>> mapping;
	private ArrayList<ArrayList<String>> list;

	/**
	 * Constructor passes in the huge list read from the corpus and also calls
	 * creatingMapping() just once. That saves time because the mapping is
	 * always the same for the same corpus. No need to regenerate the mapping
	 * every time we print a prediction
	 * 
	 * @param list
	 */
	public Predictor(ArrayList<ArrayList<String>> list) {
		mapping = new HashMap<String, ArrayList<String>>();
		this.list = list;
		createMapping();
	}

	/**
	 * This method transforms the huge list into a map following the
	 * specification, i.e. every word maps to the word that comes after it,
	 * every two words map to the word that comes after them
	 */
	public void createMapping() {

		if (!(list.size() > 0)) {
			System.out.println("No corpus available for creating the mapping!");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			ArrayList<String> smallList = list.get(i);
			for (int j = 0; j < smallList.size(); j++) {
				String firstString = smallList.get(j);
				if (j + 1 == smallList.size()) {	//if at the last element, exit this loop
					break;
				}
				String secondString = smallList.get(j + 1);
				updateMap(firstString, secondString);
				if (j + 2 == smallList.size()) {	//if at the second to last element, don't go beyond the index 
					continue;
				}
				String thirdString = smallList.get(j + 2);
				String bigram = firstString.concat(" ").concat(secondString);	//create a bigram with a space in between
				updateMap(bigram, thirdString);
			}
		}

	}

	/**
	 * This method is a helper method for creatingMap(). It updates the
	 * key-value pairs in the mapping
	 * 
	 * @param firstString
	 * @param secondString
	 */
	private void updateMap(String firstString, String secondString) {
		if (mapping.containsKey(firstString)) {
			ArrayList<String> arrayList = mapping.get(firstString);
			arrayList.add(secondString);
			mapping.put(firstString, arrayList);
		} else {
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add(secondString);
			mapping.put(firstString, arrayList);
		}
	}

	/**
	 * This method transforms a list of possible predictions into a sorted list
	 * based on frequencies of each word
	 * 
	 * @param list
	 * @return
	 */
	private ArrayList<Entry<String, Integer>> generateResult(ArrayList<String> list) {
		HashMap<String, Integer> ranking = new HashMap<String, Integer>();
		for (String string : list) {
			ranking.put(string, ranking.getOrDefault(string, 0) + 1);	//update the frequency of a word
		}
		ArrayList<Entry<String, Integer>> result = sortMap(ranking);
		return result;

	}

	/**
	 * This is a helper method for generateResult(). It just sorts a hashmap's
	 * entries based on values
	 * 
	 * @param ranking
	 * @return
	 */
	private ArrayList<Entry<String, Integer>> sortMap(HashMap<String, Integer> ranking) {
		Set<Entry<String, Integer>> set = ranking.entrySet();
		ArrayList<Entry<String, Integer>> sortable = new ArrayList<Entry<String, Integer>>(set);
		Collections.sort(sortable, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				int comparison = o2.getValue().compareTo(o1.getValue());	//compare the numbers/frequencies
				if (comparison == 0) {
					return o1.getKey().compareTo(o2.getKey());	//if the frequency is equal, then compare the alphabets
				} else {
					return comparison;
				}
			}
		});
		return sortable;
	}

	/**
	 * This method selects alternative words for bigrams which have fewer than 3 suggestions
	 * to fill up the gap
	 * @param string
	 * @param suggestions
	 * @return
	 */
	private ArrayList<Entry<String, Integer>> generateAlternate(String string,
			ArrayList<Entry<String, Integer>> suggestions) {
		String word = string.split(" ")[1];	//extract the most recently entered word
		ArrayList<Entry<String, Integer>> alternate = processing(word);	//get a list of predictions for this most recent word
		alternate.removeIf(e -> {
			for (Entry<String, Integer> f : suggestions) {	//if any of the suggestions overlap with existing suggestions, remove them
				if (e.getKey().equals(f.getKey()))
					return true;
			}
			return false;
		});
		suggestions.addAll(alternate);	//Add the alternative words to fill up the blank so that the result has 3 words
		return suggestions;
	}

	/**
	 * This method picks 3 or fewer final suggested words
	 * 
	 * @param string
	 * @return
	 */
	private ArrayList<Entry<String, Integer>> processing(String string) {
		ArrayList<String> wordList = mapping.get(string);
		if (wordList == null) {
			return null;
		}
		ArrayList<Entry<String, Integer>> result = generateResult(wordList);
		int size = (result.size() >= 3) ? 3 : result.size();	//determine the size of the final word list for the bigram	
		ArrayList<Entry<String, Integer>> suggestions = new ArrayList<Entry<String, Integer>>();
		for (int i = 0; i < size; i++) {
			suggestions.add(result.get(i));
		}
		if (size < 3 && string.split(" ").length == 2) {	//if end up having fewer than 2 words and we have a bigram, then go get some alternatives
			suggestions = generateAlternate(string, suggestions);
		}

		return suggestions;
	}

	/**
	 * 'Public API' for this Predictor class. It prints out the final results.
	 * If none, simply prints 'No Suggestions'
	 * 
	 * @param suggestions
	 */
	public void printResult(String suggestions) {
		ArrayList<Entry<String, Integer>> list = processing(suggestions);
		if (list == null) {
			System.out.println("No Suggestions!");
			return;
		}
		int listSize = (list.size() >= 3) ? 3 : list.size();
		for (int i = 0; i < listSize; i++) {
			System.out.println("Suggestion " + (i + 1) + ": " + list.get(i).getKey() + " ---- with a frequency of "
					+ list.get(i).getValue());
		}
	}

}
