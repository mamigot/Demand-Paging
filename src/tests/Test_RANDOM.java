package tests;

import java.io.FileNotFoundException;

import main.DemandPaging;
import main.DemandPaging.ReplacementAlgorithm;

public class Test_RANDOM {

	private static ReplacementAlgorithm algo = ReplacementAlgorithm.RANDOM;

	public static void run05() throws FileNotFoundException {

		int M = 20;
		int P = 10;
		int S = 10;
		int J = 2;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run10() throws FileNotFoundException {

		int M = 20;
		int P = 10;
		int S = 10;
		int J = 4;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}

	public static void run15() throws FileNotFoundException {

		int M = 10;
		int P = 5;
		int S = 30;
		int J = 4;
		int N = 3;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void main(String[] args) {
		

	}

}
