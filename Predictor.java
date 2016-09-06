package edu.upenn.cis573.predict;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Predictor {

	private TreeMap<Pair, Integer> mapping;
	private ArrayList<String> list;

	public Predictor(ArrayList<String> list) {
		mapping = new TreeMap<Pair, Integer>();
		this.list = list;
	}

	public void createMapping() {

		if (!(list.size() > 0)) {
			System.out.println("No corpus available for creating the mapping!");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			String firstString = list.get(i);
			if (i + 1 == list.size()) {
				break;
			}
			String secondString = list.get(i + 1);
			Pair pair = new Pair(firstString, secondString);
			mapping.put(pair, mapping.getOrDefault(pair, 0) + 1);
			if (i + 2 == list.size()) {
				continue;
			}
			String thirdString = list.get(i + 2);
			String bigram = createBigram(firstString, secondString);
			Pair bigramPair = new Pair(bigram, thirdString);
			mapping.put(bigramPair, mapping.getOrDefault(bigramPair, 0) + 1);
		}

		Iterator<Entry<Pair, Integer>> it = mapping.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Pair, Integer> p = it.next();
			System.out.println(p.getKey().getFirst() + " " + p.getKey().getSecond() + " " + p.getValue());
		}
	}

	private String createBigram(String firstString, String secondString) {
		return (firstString + secondString);
	}

	public ArrayList<Entry<Pair, Integer>> sortMap() {
		// mapping.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).limit(50)
		// .forEach(pair -> System.out.println(pair.getKey().getFirst() + " " +
		// pair.getKey().getSecond()));
		Set<Entry<Pair, Integer>> set = mapping.entrySet();
		ArrayList<Entry<Pair, Integer>> list = new ArrayList<Entry<Pair, Integer>>(set);
		Collections.sort(list, new Comparator<Map.Entry<Pair, Integer>>() {
			@Override
			public int compare(Entry<Pair, Integer> o1, Entry<Pair, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		return list;
	}

	private ArrayList<Entry<Pair, Integer>> processing() {
		createMapping();
		ArrayList<Entry<Pair, Integer>> list = sortMap();
		return list;
	}

	public void printResult() {
		ArrayList<Entry<Pair, Integer>> list = processing();
		if (list.size() > 0) {
			System.out.println("The top predictions are: ");
			for (int i = 0; i < 3; i++) {
				System.out.println(list.get(i).getKey().getFirst() + " " + list.get(i).getKey().getSecond() + " " + list.get(i).getValue());
			}
			
		}
			
	}

}
