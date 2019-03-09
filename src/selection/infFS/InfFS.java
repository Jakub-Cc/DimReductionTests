package selection.infFS;

import core.SimpleMatrix;
import selection.infFS.methods.MatrixTools;

public class InfFS {

    public static void main(String[] args) {
	SimpleMatrix matrix = new SimpleMatrix();

	matrix.readDataFromCSV("x.csv");
	System.out.println();

	long start = System.currentTimeMillis();
	{
	    InfFS infFS = new InfFS();
	    int[] rank = infFS.infFS(matrix, 0.5, false);

	    for (int i = 0; i < rank.length; i++) {
		System.out.println(rank[i]);
	    }
	}
	long elapsedTime = System.currentTimeMillis() - start;
	System.out.println();
	System.out.println((double) elapsedTime / 1000);
    }

    public int[] infFS(SimpleMatrix Xtrain, double alpha, boolean verbose) {

	int[] ranked = new int[Xtrain.dimensions];

	SimpleMatrix corr = MatrixTools.spearmanCorrelation(Xtrain);
	MatrixTools.replaceUndef(corr.matrix, 0);
	MatrixTools.transformValues(corr.matrix, x -> 1 - Math.abs(x));

	System.out.println("corelation done");

	double[] STD = MatrixTools.standardDeviation(Xtrain.matrix, false);
	double[][] sigma = MatrixTools.bsxfun((a, b) -> a > b ? a : b, STD, STD);

	System.out.println("STD done");

	double min = MatrixTools.min(sigma);
	MatrixTools.transformValues(sigma, e -> e - min);

	double max = MatrixTools.max(sigma);
	MatrixTools.transformValues(sigma, e -> e / max);
	MatrixTools.replaceUndef(sigma, 0);

	MatrixTools.transformValues(corr.matrix, x -> alpha * x);
	MatrixTools.transformValues(sigma, x -> (1 - alpha) * x);

	double[][] A = MatrixTools.useFunctionOnMatrixes((a, b) -> a + b, corr.matrix, sigma);

	double[][] I = MatrixTools.eye(A.length);
	System.out.println("A done");

	// MatrixTools.printMatrix(A);

	double factor = 0.99;
	double r = (factor / MatrixTools.max(MatrixTools.eigenValues(A)));
	System.out.println("Eigen done");

	MatrixTools.transformValues(A, x -> x * r);
	double[][] y = MatrixTools.useFunctionOnMatrixes((a, b) -> a - b, I, A);

	double[][] S = MatrixTools.useFunctionOnMatrixes((a, b) -> a - b, MatrixTools.inverse(y), I);

	double[] weight = MatrixTools.sumOfElements(S, false);
	System.out.println("weights done");

	for (int i = 0; i < weight.length; i++) {
	    System.out.print("" + i + " " + weight[i] + " ;");
	}
	System.out.println();

	ranked = MatrixTools.sortDescendant(weight);
	System.out.println("sort done");
	return ranked;
    }

}
