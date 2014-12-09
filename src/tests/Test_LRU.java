package tests;

import java.io.FileNotFoundException;

import main.DemandPaging;
import main.DemandPaging.ReplacementAlgorithm;

public class Test_LRU {

	private static ReplacementAlgorithm algo = ReplacementAlgorithm.LRU;

	public static void run01() throws FileNotFoundException {

		int M = 10;
		int P = 10;
		int S = 20;
		int J = 1;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run02() throws FileNotFoundException {

		int M = 10;
		int P = 10;
		int S = 10;
		int J = 1;
		int N = 100;

		new DemandPaging(algo, M, P, S, J, N);

	}

	public static void run03() throws FileNotFoundException {

		int M = 10;
		int P = 10;
		int S = 10;
		int J = 2;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run04() throws FileNotFoundException {

		int M = 20;
		int P = 10;
		int S = 10;
		int J = 2;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run09() throws FileNotFoundException {

		int M = 20;
		int P = 10;
		int S = 10;
		int J = 4;
		int N = 10;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run11() throws FileNotFoundException {

		int M = 90;
		int P = 10;
		int S = 40;
		int J = 4;
		int N = 100;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run12() throws FileNotFoundException {

		int M = 40;
		int P = 10;
		int S = 90;
		int J = 1;
		int N = 100;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void run14() throws FileNotFoundException {

		int M = 800;
		int P = 40;
		int S = 400;
		int J = 4;
		int N = 5000;

		new DemandPaging(algo, M, P, S, J, N);

	}
	
	public static void main(String[] args) throws FileNotFoundException {

		run11();
		
	}

}
