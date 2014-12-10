package tests;

import java.io.FileNotFoundException;

import main.DemandPaging;
import main.DemandPaging.ReplacementAlgorithm;

public class Test_FIFO {

	private static ReplacementAlgorithm algo = ReplacementAlgorithm.FIFO;

	public static void run06() throws FileNotFoundException {

		int M = 20;
		int P = 10;
		int S = 10;
		int J = 2;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run08() throws FileNotFoundException {

		int M = 20;
		int P = 10;
		int S = 10;
		int J = 3;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run13() throws FileNotFoundException {

		int M = 40;
		int P = 10;
		int S = 90;
		int J = 1;
		int N = 100;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run16() throws FileNotFoundException {

		int M = 1000;
		int P = 40;
		int S = 400;
		int J = 4;
		int N = 5000;

		new DemandPaging(algo, M, P, S, J, N);

	}

	public static void main(String[] args) {
		

	}

}
