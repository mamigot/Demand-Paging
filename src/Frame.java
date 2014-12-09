public class Frame {

	private int ID;
	private boolean isOccupied;

	private int processID;
	private int pageNumber;

	public Frame(int ID) {
		this.ID = ID;
		this.isOccupied = false;

		this.processID = 0;
		this.pageNumber = 0;
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
