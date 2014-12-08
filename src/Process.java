public class Process {

	private int ID;
	private int size;
	private JobMixProbability jobMix;

	private int remainingReferences;
	private int currentWord;

	private int countPageFaults;
	private int runningSumPageResidencyTime;

	public Process(int ID, int size, JobMixProbability jobMix) {
		this.ID = ID;
		this.size = size;
		this.jobMix = jobMix;

		this.remainingReferences = 0;
		this.currentWord = 0;

		this.countPageFaults = 0;
		this.runningSumPageResidencyTime = 0;
	}

	public int getID() {
		return this.ID;
	}

	public int getSize() {
		return this.size;
	}

	public JobMixProbability getJobMix() {
		return this.jobMix;
	}

	public int getCurrentWord() {
		return this.currentWord;
	}

	public int getCountPageFaults() {
		return this.countPageFaults;
	}

	public void setCurrentWord(int word) {
		this.currentWord = word;
	}

	public int decRemainingReferences() {
		return --this.remainingReferences;
	}

	public int incCountPageFaults() {
		return ++this.countPageFaults;
	}

	public int logPageResidencyTime(int pageResidencyTime) {
		return this.runningSumPageResidencyTime += pageResidencyTime;
	}

}
