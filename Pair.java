package edu.upenn.cis573.predict;

import java.util.Comparator;

public class Pair implements Comparable<Pair>{

	private String first;
	private String second;
	
	public Pair(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	public String getFirst() {
		return first;
	}
	
	public String getSecond() {
		return second;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}


	@Override
	public int compareTo(Pair o) {
		return (first == o.getFirst() && second == o.getSecond()) ? 0 : 1; 
	}



	
	
	
}
