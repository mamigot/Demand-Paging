package main;

public class Process {

	private int ID;
	private int size;
	private JobMixProbability jobMix;

	private int remainingReferences;
	private int currentWord;

	private int numPageFaults;
	private int numEvictions;
	private int runningSumPageResidencyTime;

	public Process(int ID, int size, int totRefs, JobMixProbability jobMix) {
		this.ID = ID;
		this.size = size;
		this.jobMix = jobMix;

		this.remainingReferences = totRefs;
		this.currentWord = (111 * this.ID) % this.size;

		this.numPageFaults = 0;
		this.numEvictions = 0;
		this.runningSumPageResidencyTime = 0;
	}

	public boolean isFinished() {
		return this.remainingReferences <= 0;
	}

	public int getID() {
		return this.ID;
	}

	public JobMixProbability getJobMix() {
		return this.jobMix;
	}

	public int getSize() {
		return this.size;
	}

	public int getCurrentWord() {
		return this.currentWord;
	}

	public void setCurrentWord(int word) {
		this.currentWord = word;
	}

	public void decrementRefs() {
		this.remainingReferences--;
	}

	public void incrementPageFaults() {
		this.numPageFaults++;
	}

	public int getNumPageFaults() {
		return this.numPageFaults;
	}

	public void incrementPageEvictions() {
		this.numEvictions++;
	}

	public int getNumEvictions() {
		return this.numEvictions;
	}

	public void logPageResidencyTime(int pageResidencyTime) {
		this.runningSumPageResidencyTime += pageResidencyTime;
	}

	public int getSumResidencyTime() {
		return this.runningSumPageResidencyTime;
	}

	public double getAvgResidencyTime() {
		return this.runningSumPageResidencyTime / (this.numEvictions * 1.0);
	}

	public String toString() {
		return "Process #" + this.getID();
	}

}
