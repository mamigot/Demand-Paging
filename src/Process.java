public class Process {

	private int ID;
	private int size;
	private JobMixProbability jobMix;

	private int remainingReferences;
	private int currentWord;

	private int countPageFaults;
	private int runningSumPageResidencyTime;

	public Process(int ID, int size, int totRefs, JobMixProbability jobMix) {
		this.ID = ID;
		this.size = size;
		this.jobMix = jobMix;

		this.remainingReferences = totRefs;
		this.currentWord = (111 * this.ID) % this.size; // Starting value

		this.countPageFaults = 0;
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

	public int decrementRefs() {
		return --this.remainingReferences;
	}

	public int incrementPageFaults() {
		return ++this.countPageFaults;
	}

	public int getCurrentWord() {
		return this.currentWord;
	}

	public void setCurrentWord(int word) {
		this.currentWord = word;
	}

	public int getNumPageFaults() {
		return this.countPageFaults;
	}

	public int logPageResidencyTime(int pageResidencyTime) {
		return this.runningSumPageResidencyTime += pageResidencyTime;
	}

	public String toString() {
		return "Process #" + this.getID();
	}

}
