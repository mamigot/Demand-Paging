import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public final class DemandPaging {

	private enum ReplacementAlgorithm {
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

	private Queue<Process> processes;
	private ArrayList<Frame> frames;
	private boolean hasAvailablePages;

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

	}

	private void initializeFrames() {
		// Uses the machinesize, pagesize, etc. to create a number of frames and
		// puts them in this.frames
		this.hasAvailablePages = true;

	}

	private boolean isHit(int processID, int pageNumber) {
		// Determines if the page corresponding to a given process and
		// characterized by a given number is in the frame table

		return true;
	}

	private void placementFrameTable(int processID, int pageNumber)
			throws UnsupportedOperationException {
		// Assuming there is room in the frame table (hence the
		// unsupportedoperationexception), it places a page corresponding to a
		// process ID and characterized by a given number in the frame table. If
		// successful, returns the frame number

	}

	private void replacementFrameTable(int processID, int pageNumber) {
		// Uses the replacement algorithm in this.replacementAlgorithm to
		// dispatch to any relevant method, and puts the page corresponding to
		// the arguments in the frame table
	}

	private void replacementFrameTableFIFO(int processID, int pageNumber) {

	}

	private void replacementFrameTableRANDOM(int processID, int pageNumber) {

	}

	private void replacementFrameTableLRU(int processID, int pageNumber) {

	}

	private void launchSimulation() {
		// After processes and frames have been created, launch the simulation

	}

	private static int getRand() {
		if (DemandPaging.scanner.hasNextInt())
			return DemandPaging.scanner.nextInt();

		return 0;
	}

	public static void main(String[] args) {

		ReplacementAlgorithm algo = ReplacementAlgorithm.LRU;
		int machineSize = 1;
		int pageSize = 1;
		int processSize = 1;
		int jobMixNumber = 1;
		int refsPerProcess = 1;

		try {
			new DemandPaging(algo, machineSize, pageSize, processSize,
					jobMixNumber, refsPerProcess);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
