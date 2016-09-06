package edu.upenn.cis573.predict;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

public class ValueComparator<Pair, Integer extends Comparable<Integer>> implements Comparator<Pair> {

	TreeMap<Pair, Integer> map = new TreeMap<Pair, Integer>();

	/**
	 * Constructor call that initializes fields
	 * 
	 * @param map
	 */
	public ValueComparator(TreeMap<Pair, Integer> map) {

		this.map.putAll(map);
		Iterator<Pair> it = this.map.keySet().iterator();
		while (it.hasNext()) {
			Pair p = it.next();
//			System.out.println(p.getFirst() + " " + p.getSecond() + " " + this.map.get(p));
		}
	}

	/**
	 * Overrides the Compare method
	 */
	@Override
	public int compare(Pair p1, Pair p2) {
		return -map.get(p1).compareTo(map.get(p2));//descending order	
	}

}
