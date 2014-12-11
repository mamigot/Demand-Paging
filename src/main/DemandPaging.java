package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public final class DemandPaging {

	public enum ReplacementAlgorithm {
		FIFO, RANDOM, LRU
	}

	private static final int RR_QUANTUM = 3;

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

	private LinkedList<Process> processes;
	private LinkedList<Frame> frames;
	private int countAvailablePages;

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

	}

	private void initializeProcesses() {
		// Uses the jobmix number to create processes with certain
		// jobmixprobabilities and places them in this.processes

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

	private void initializeFrames() {
		// Uses the machinesize, pagesize, etc. to create a number of frames and
		// puts them in this.frames

		this.frames = new LinkedList<Frame>();
		this.countAvailablePages = this.M / this.P;

		for (int i = 0; i < this.M / this.P; i++)
			this.frames.add(new Frame(i));

	}

	private Frame isHit(int processID, int pageNumber) {
		// Determines if the page corresponding to a given process and
		// characterized by a given number is in the frame table
		// if it is, it makes the proper modifications based on the replacement
		// algorithm

		for (Frame f : this.frames)
			if (f.getProcessID() == processID
					&& f.getPageNumber() == pageNumber)
				return f;

		return null;

	}

	private void framePlacement(int processID, int pageNumber) {
		// Assuming there is room in the frame table, it places a page
		// corresponding to a process ID and characterized by a given number in
		// the frame table. If
		// successful, returns the frame number
		// Convention: place in highest-numbered free frame

		if (this.countAvailablePages <= 0)
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

		this.countAvailablePages--;
		highest.fill(processID, pageNumber);
		highest.logStartTime(this.sysClock);

		System.out.println("\t using free frame " + highest.getID());

	}

	private void frameReplacement(int processID, int pageNumber) {
		// Uses the replacement algorithm in this.replacementAlgorithm to
		// dispatch to any relevant method, and puts the page corresponding to
		// the arguments in the frame table

		if (this.countAvailablePages > 0)
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

			System.out.println("\t eviction in frame " + frame.getID());

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

	private int getNextWord(Process proc, int currWord) {
		// Relevant after the first word

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

	private static int getRand() {
		if (DemandPaging.scanner.hasNextInt())
			return DemandPaging.scanner.nextInt();

		return 0;
	}

	private void launchSimulation() {
		// After processes and frames have been created, launch the simulation

		int totRefs = this.processes.size() * this.N;

		int word;
		int page;
		Frame frame;
		Process proc;
		for (int i = 0; i < totRefs; i++) {

			proc = this.processes.get(i % this.processes.size());
			word = proc.getCurrentWord();

			for (int ref = 0; (ref < DemandPaging.RR_QUANTUM)
					&& (!proc.isFinished()); ref++) {
				// Simulate the reference for this process (USE word)
				// Calculate the next reference for this process

				// map the word to a page number
				page = word / this.P;
				frame = this.isHit(proc.getID(), page);

				System.out.println(proc.getID() + " references word " + word
						+ " page (" + page + ")\tat time " + sysClock);

				if (frame == null) {
					if (this.countAvailablePages > 0)
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

		for (Process p : this.processes) {
			System.out.println(p.getID() + ": " + p.getNumPageFaults());
			System.out.println("\taverage residency: "
					+ (p.getAvgResidencyTime()));
		}

	}

	public static void main(String[] args) {

		try {
			tests.Test_RANDOM.run15();

			// Error:
			// 4 references word 22 page (2)
			// eviction in frame 7 (SHOULD BE FRAME 6)

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
