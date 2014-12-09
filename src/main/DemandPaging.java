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

	private ReplacementAlgorithm replacementAlgorithm;
	private int machineSize;
	private int pageSize;
	private int processSize;
	private int jobMixNumber;
	private int refsPerProcess;

	private LinkedList<Process> processes;
	private LinkedList<Frame> frames;
	private int countAvailablePages;

	private int sysClock = 1;

	public DemandPaging(ReplacementAlgorithm algo, int machineSize,
			int pageSize, int processSize, int jobMixNumber, int refsPerProcess)
			throws FileNotFoundException {

		this.replacementAlgorithm = algo;
		this.machineSize = machineSize;
		this.pageSize = pageSize;
		this.processSize = processSize;
		this.jobMixNumber = jobMixNumber;
		this.refsPerProcess = refsPerProcess;

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

		switch (this.jobMixNumber) {
		case 1:
			this.processes.add(new Process(1, this.processSize,
					this.refsPerProcess, new JobMixProbability(1, 0, 0)));

			break;

		case 2:
			for (int i = 1; i < 5; i++)
				this.processes.add(new Process(i, this.processSize,
						this.refsPerProcess, new JobMixProbability(1, 0, 0)));

			break;

		case 3:
			for (int i = 1; i < 5; i++)
				this.processes.add(new Process(i, this.processSize,
						this.refsPerProcess, new JobMixProbability(0, 0, 0)));

			break;

		case 4:
			this.processes.add(new Process(1, this.processSize,
					this.refsPerProcess, new JobMixProbability(0.75, 0.25, 0)));

			this.processes.add(new Process(2, this.processSize,
					this.refsPerProcess, new JobMixProbability(0.75, 0, 0.25)));

			this.processes.add(new Process(3, this.processSize,
					this.refsPerProcess, new JobMixProbability(0.75, 0.125,
							0.125)));

			this.processes.add(new Process(4, this.processSize,
					this.refsPerProcess, new JobMixProbability(0.5, 0.125,
							0.125)));

			break;
		}

	}

	private void initializeFrames() {
		// Uses the machinesize, pagesize, etc. to create a number of frames and
		// puts them in this.frames

		this.frames = new LinkedList<Frame>();
		this.countAvailablePages = this.machineSize / this.pageSize;

		for (int i = 0; i < this.machineSize / this.pageSize; i++)
			this.frames.add(new Frame(i));

	}

	private boolean isHit(int processID, int pageNumber) {
		// Determines if the page corresponding to a given process and
		// characterized by a given number is in the frame table

		for (Frame f : this.frames)
			if (f.getProcessID() == processID
					&& f.getPageNumber() == pageNumber) {
				System.out.println("\t hit in frame " + f.getID());
				return true;
			}

		return false;

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

		highest.fill(processID, pageNumber);
		this.frames.remove(highest);
		this.countAvailablePages--;

		if (this.replacementAlgorithm.equals(ReplacementAlgorithm.LRU))
			this.frames.addLast(highest);

		System.out.println("\t using free frame " + highest.getID());

	}

	private void frameReplacement(int processID, int pageNumber) {
		// Uses the replacement algorithm in this.replacementAlgorithm to
		// dispatch to any relevant method, and puts the page corresponding to
		// the arguments in the frame table

		if (this.countAvailablePages > 0)
			throw new UnsupportedOperationException(
					"No replacement needed if there are free spots available!");

		Frame frame;
		if (this.replacementAlgorithm.equals(ReplacementAlgorithm.LRU)) {
			frame = this.frames.removeFirst();

			System.out.println("\t eviction in frame " + frame.getID());

			frame.fill(processID, pageNumber);

			this.frames.addLast(frame);
		}

	}

	private int getNextWord(Process proc, int currWord) {
		// Relevant after the first word

		int r = DemandPaging.getRand();
		double y = r / (Integer.MAX_VALUE + 1d);

		JobMixProbability jm = proc.getJobMix();
		int S = this.processSize;

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

		int totRefs = this.processes.size() * this.refsPerProcess;

		int word;
		int page;
		Process proc;
		for (int i = 0; i < totRefs; i++) {

			proc = this.processes.get(i % this.processes.size());
			word = proc.getCurrentWord();

			for (int ref = 0; (ref < DemandPaging.RR_QUANTUM)
					&& (!proc.isFinished()); ref++) {
				// Simulate the reference for this process (USE word)
				// Calculate the next reference for this process

				// map the word to a page number
				page = word / this.pageSize;
				System.out.println(proc.getID() + " references word " + word
						+ " page (" + page + ")\tat time " + sysClock);

				if (!this.isHit(proc.getID(), page)) {
					if (this.countAvailablePages > 0)
						this.framePlacement(proc.getID(), page);

					else
						this.frameReplacement(proc.getID(), page);

					proc.incrementPageFaults();
				}

				word = this.getNextWord(proc, word);
				proc.setCurrentWord(word);
				proc.decrementRefs();
				
				this.sysClock++;
				
				if(sysClock == 35) // Debugging
					System.exit(1);
			}
		}

		for (Process p : this.processes)
			System.out.println(p.getID() + ": " + p.getNumPageFaults());

	}

	public static void main(String[] args) {

		try {
			tests.Test_LRU.run11();

			// Error:
			// 4 references word 22 page (2)
			// eviction in frame 7 (SHOULD BE FRAME 6)

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
