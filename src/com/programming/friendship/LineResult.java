package com.programming.friendship;

public class LineResult {

	private Person first;
	private Person second;
	private LineType type;
	private int level;
	private String line;
	
	public Person getFirst() {
		return first;
	}

	public void setFirst(Person first) {
		this.first = first;
	}

	public Person getSecond() {
		return second;
	}

	public void setSecond(Person second) {
		this.second = second;
	}

	public LineType getType() {
		return type;
	}

	public void setType(LineType type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setLine(String line) {
		this.line = line;
	}
	
	public String getLine() {
		return line;
	}
	
	@Override
	public String toString() {
		return this.type.toString();
	}
}
