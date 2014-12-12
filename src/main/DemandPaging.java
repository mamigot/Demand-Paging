package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Simulates demand paging and reports relevant statistics for the replacement
 * algorithms listed in {@link ReplacementAlgorithm} below
 */
public final class DemandPaging {

	/**
	 * Supported replacement algorithms
	 */
	public enum ReplacementAlgorithm {
		FIFO, RANDOM, LRU
	}

	/**
	 * Round-Robin quantum
	 */
	private static final int RR_QUANTUM = 3;

	/**
	 * Used to model random numbers
	 */
	private static String randomNumbersPath = "inputs/random-numbers.txt";
	private static Scanner scanner;

	/**
	 * Replacement algorithm
	 */
	private ReplacementAlgorithm R;
	/**
	 * Machine size (in words)
	 */
	private int M;
	/**
	 * Page size (in words)
	 */
	private int P;
	/**
	 * Size of a process (references are to virtual addresses 0...S-1)
	 */
	private int S;
	/**
	 * Job mix (determines the simulation parameters for the processes)
	 */
	private int J;
	/**
	 * Number of references per process
	 */
	private int N;

	/**
	 * Working processes and frames
	 */
	private LinkedList<Process> processes;
	private LinkedList<Frame> frames;
	private int countAvailableFrames;
	/**
	 * Models the system clock
	 */
	private int sysClock = 1;

	public DemandPaging(ReplacementAlgorithm algo, int machineSize,
			int pageSize, int processSize, int jobMixNumber, int refsPerProcess)
			throws FileNotFoundException {

		this.R = algo;
		this.M = machineSize;
		this.P = pageSize;
		this.S = processSize;
		this.J = jobMixNumber;
		this.N = refsPerProcess;

		DemandPaging.scanner = new Scanner(new File(
				DemandPaging.randomNumbersPath));

		this.initializeProcesses();
		this.initializeFrames();
		this.launchSimulation();
		this.printReport();

	}

	/**
	 * Uses predefined job mix parameters to create relevant processes
	 */
	private void initializeProcesses() {

		this.processes = new LinkedList<Process>();

		switch (this.J) {
		case 1:
			this.processes.add(new Process(1, this.S, this.N,
					new JobMixProbability(1, 0, 0)));

			break;

		case 2:
			for (int i = 1; i < 5; i++)
				this.processes.add(new Process(i, this.S, this.N,
						new JobMixProbability(1, 0, 0)));

			break;

		case 3:
			for (int i = 1; i < 5; i++)
				this.processes.add(new Process(i, this.S, this.N,
						new JobMixProbability(0, 0, 0)));

			break;

		case 4:
			LinkedList<JobMixProbability> types = new LinkedList<JobMixProbability>();
			types.add(new JobMixProbability(0.75, 0.25, 0));
			types.add(new JobMixProbability(0.75, 0, 0.25));
			types.add(new JobMixProbability(0.75, 0.125, 0.125));
			types.add(new JobMixProbability(0.5, 0.125, 0.125));

			for (int i = 1; i < 5; i++)
				this.processes.add(new Process(i, this.S, this.N, types
						.get(i - 1)));

			break;
		}

	}

	/**
	 * Uses the predefined machine and page size to create relevant frames
	 */
	private void initializeFrames() {

		this.frames = new LinkedList<Frame>();
		this.countAvailableFrames = this.M / this.P;

		for (int i = 0; i < this.M / this.P; i++)
			this.frames.add(new Frame(i));

	}

	/**
	 * Determines if the page corresponding to a given process and number is in
	 * memory (in a frame). If it is, the relevant frame is returned.
	 */
	private Frame isHit(int processID, int pageNumber) {

		for (Frame f : this.frames)
			if (f.getProcessID() == processID
					&& f.getPageNumber() == pageNumber)
				return f;

		return null;

	}

	/**
	 * Places a given page (characterized by a processID and a page number) in
	 * the highest numbered free frame. If there are no free frames, an
	 * UnsupportedOperationException is thrown.
	 */
	private void framePlacement(int processID, int pageNumber) {

		if (this.countAvailableFrames <= 0)
			throw new UnsupportedOperationException(
					"Placement question is only relevant if there are free pages in the frame table.");

		Frame highest = this.frames.peek();
		for (Frame f : this.frames)
			if (!f.isOccupied() && (f.getID() > highest.getID()))
				highest = f;

		if (this.R.equals(ReplacementAlgorithm.LRU)
				|| this.R.equals(ReplacementAlgorithm.FIFO)) {
			this.frames.remove(highest);
			this.frames.addLast(highest);
		}

		this.countAvailableFrames--;
		highest.fill(processID, pageNumber);
		highest.logStartTime(this.sysClock);

	}

	/**
	 * Replaces a given page in the frame table with a new one (characterized by
	 * a processID and a page number), according to the used replacement
	 * algorithm ({@link this.R}).
	 */
	private void frameReplacement(int processID, int pageNumber) {

		if (this.countAvailableFrames > 0)
			throw new UnsupportedOperationException(
					"No replacement needed if there are free spots available!");

		Frame frame = null;
		int evictedPID = 0;
		if (this.R.equals(ReplacementAlgorithm.LRU)
				|| this.R.equals(ReplacementAlgorithm.FIFO)) {
			frame = this.frames.removeFirst();
			evictedPID = frame.getProcessID();

			frame.fill(processID, pageNumber);
			this.frames.addLast(frame);

		} else if (this.R.equals(ReplacementAlgorithm.RANDOM)) {
			int randIndex = DemandPaging.getRand() % this.frames.size();
			frame = frames.get(randIndex);
			evictedPID = frame.getProcessID();

			frame.fill(processID, pageNumber);
		}

		int elapsedTime = frame.getTimeElapsed(this.sysClock);
		frame.logStartTime(this.sysClock);

		Process evicted = this.processes.get(evictedPID - 1);
		evicted.logPageResidencyTime(elapsedTime);
		evicted.incrementPageEvictions();

	}

	/**
	 * Gets a process' next word based on its current word, using the random
	 * numbers from {@link #getRand()}.
	 */
	private int getNextWord(Process proc, int currWord) {

		int r = DemandPaging.getRand();
		double y = r / (Integer.MAX_VALUE + 1d);

		JobMixProbability jm = proc.getJobMix();
		int S = this.S;

		if (y < jm.getA())
			return (currWord + 1 + S) % S;

		else if (y < (jm.getA() + jm.getB()))
			return (currWord - 5 + S) % S;

		else if (y < (jm.getA() + jm.getB() + jm.getC()))
			return (currWord + 4 + S) % S;

		else
			return (DemandPaging.getRand() + S) % S;

	}

	/**
	 * Gets the next random number from the available feed, found in the file
	 * listed in {@link #randomNumbersPath}.
	 */
	private static int getRand() {
		if (DemandPaging.scanner.hasNextInt())
			return DemandPaging.scanner.nextInt();

		return 0;
	}

	/**
	 * Launches the demand-paging simulation after the processes and frames have
	 * been initialized (see {@link DemandPaging(ReplacementAlgorithm, int, int,
	 * int, int, int)} ).
	 */
	private void launchSimulation() {

		int totRefs = this.processes.size() * this.N;

		int i;
		int ref;
		int word;
		int page;
		Frame frame;
		Process proc;
		for (i = 0; i < totRefs; i++) {

			proc = this.processes.get(i % this.processes.size());
			word = proc.getCurrentWord();

			// Simulates the current reference for this process and calculates
			// the next reference
			for (ref = 0; (ref < DemandPaging.RR_QUANTUM)
					&& (!proc.isFinished()); ref++) {

				page = word / this.P; // Map the word to a page number
				frame = this.isHit(proc.getID(), page);

				if (frame == null) {
					if (this.countAvailableFrames > 0)
						this.framePlacement(proc.getID(), page);

					else
						this.frameReplacement(proc.getID(), page);

					proc.incrementPageFaults();

				} else {
					// there was a hit; make the modification in the frame table
					if (this.R.equals(ReplacementAlgorithm.LRU)) {
						this.frames.remove(frame);
						this.frames.addLast(frame);

					}
				}

				word = this.getNextWord(proc, word);
				proc.setCurrentWord(word);
				proc.decrementRefs();

				this.sysClock++;
			}
		}

	}

	/**
	 * Prints a report according to the assignment's guidelines, and calculates
	 * additional statistics.
	 */
	private void printReport() {

		System.out.println("The machine size is " + this.M + ".");
		System.out.println("The page size is " + this.P + ".");
		System.out.println("The process size is " + this.S + ".");
		System.out.println("The job mix number is " + this.J + ".");
		System.out.println("The number of references per process is " + this.N
				+ ".");
		System.out.println("The replacement algorithm is " + this.R + ".");
		System.out.println();

		int totFaults = 0;
		int totResidency = 0;
		int totEvictions = 0;
		boolean displayAvgResidency = true;

		for (Process p : this.processes) {
			System.out.print("Process " + p.getID() + " had "
					+ +p.getNumPageFaults() + " faults");

			if (Double.isNaN(p.getAvgResidencyTime()))
				System.out
						.print(".\n\tWith no evictions, the average residence is undefined.\n");

			else
				System.out.print(" and " + p.getAvgResidencyTime()
						+ " average residency.\n");

			totFaults += p.getNumPageFaults();
			totEvictions += p.getNumEvictions();
			totResidency += p.getSumResidencyTime();
		}

		if (totResidency <= 0)
			displayAvgResidency = false;

		System.out.print("\nThe total number of faults is " + totFaults + " ");
		if (displayAvgResidency)
			System.out.print("and the overall average residency is "
					+ (totResidency * 1.0 / totEvictions) + ".");
		else
			System.out
					.println("\n\tWith no evictions, the overall average residence is undefined.");

	}

	public static void main(String[] args) {
		
		if(args.length != 6)
			throw new IllegalArgumentException("Provide the following 6 arguments: M P S J N R");

		int M = Integer.parseInt(args[0]);
		int P = Integer.parseInt(args[1]);
		int S = Integer.parseInt(args[2]);
		int J = Integer.parseInt(args[3]);
		int N = Integer.parseInt(args[4]);
		String R = args[5];
		ReplacementAlgorithm algo = null;
		
		if(R.equals("fifo"))
			algo = ReplacementAlgorithm.FIFO;
		else if(R.equals("lru"))
			algo = ReplacementAlgorithm.LRU;
		else if(R.equals("random"))
			algo = ReplacementAlgorithm.RANDOM;
		
		try {
			new DemandPaging(algo, M, P, S, J, N);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
}
