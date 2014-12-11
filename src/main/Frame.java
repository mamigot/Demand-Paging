package main;

public class Frame {

	private int ID;
	private boolean isOccupied;
	private int startTime;

	private int processID;
	private int pageNumber;

	public Frame(int ID) {
		this.ID = ID;
		this.isOccupied = false;

		this.processID = 0;
		this.pageNumber = 0;

		this.startTime = 0;
	}

	public boolean equals(Frame that) {
		return this.ID == that.ID;
	}

	public int getID() {
		return this.ID;
	}

	public int getProcessID() {
		return this.processID;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public boolean isOccupied() {
		return this.isOccupied;
	}

	public void logStartTime(int time) {
		this.startTime = time;
	}

	public int getTimeElapsed(int endTime) {
		return endTime - this.startTime;
	}

	public void fill(int processID, int pageNumber) {
		this.processID = processID;
		this.pageNumber = pageNumber;
		this.isOccupied = true;
	}

	public void free() {
		this.processID = 0;
		this.pageNumber = 0;
		this.isOccupied = false;
	}

}
