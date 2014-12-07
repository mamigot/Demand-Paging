import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class DemandPaging {

	public enum ReplacementAlgorithm {
		FIFO, RANDOM, LRU
	}

	public static final int RR_QUANTUM = 3;

	public static String randomNumbersPath = "inputs/random-numbers.txt";
	public static Scanner scanner;

	public ReplacementAlgorithm algo;
	public int machineSize;
	public int pageSize;
	public int processSize;
	public int jobMixNumber;
	public int refsPerProcess;

	public DemandPaging(ReplacementAlgorithm algo, int machineSize,
			int pageSize, int processSize, int jobMixNumber, int refsPerProcess)
			throws FileNotFoundException {

		this.algo = algo;
		this.machineSize = machineSize;
		this.pageSize = pageSize;
		this.processSize = processSize;
		this.jobMixNumber = jobMixNumber;
		this.refsPerProcess = refsPerProcess;

		DemandPaging.scanner = new Scanner(new File(
				DemandPaging.randomNumbersPath));

	}

	public static int getRand() {
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
			new DemandPaging(algo, machineSize, pageSize, processSize, jobMixNumber,
					refsPerProcess);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
