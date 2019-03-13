package myImp;

import core.MatrixTools;
import core.SimpleMatrix;
import distanceMeasures.Euclidean;

public class LaplacianScore {

    public static void main(String[] args) {
	SimpleMatrix matrix = new SimpleMatrix();

	matrix.readDataFromCSV("x.csv");
	System.out.println();

	long start = System.currentTimeMillis();
	{

	    int[] rank = laplacianScore(matrix.matrix);

	    for (int i = 0; i < rank.length; i++) {
		System.out.println(rank[i]);
	    }
	}
	long elapsedTime = System.currentTimeMillis() - start;
	System.out.println();
	System.out.println((double) elapsedTime / 1000);

    }

    public static int[] laplacianScore(double[][] matrix) {

	double[][] distanceMatrix = MatrixTools.generateDistanceMatrix(matrix, new Euclidean());
	double max = MatrixTools.max(distanceMatrix);
	MatrixTools.transformValues(distanceMatrix, x -> -(x / max));

	double[] sum = MatrixTools.sumOfElements(distanceMatrix, false);
	double[][] sumMatrix = MatrixTools.vectorToMatrix(sum, false);

	double[][] tmpl = MatrixTools.multiplicateMatrixesWrap(sumMatrix, matrix);

	double sumOfAllElements = MatrixTools.sumOfElements(sum);
	MatrixTools.transformValues(tmpl, x -> x * x / sumOfAllElements);

	double[][] DPrime = calculateDPrime(matrix, sum);
	DPrime = MatrixTools.useFunctionOnMatrixes((a, b) -> a - b, DPrime, tmpl);

	double[][] LPrime = calculateLPrime(matrix, distanceMatrix);
	LPrime = MatrixTools.useFunctionOnMatrixes((a, b) -> a - b, LPrime, tmpl);

	MatrixTools.transformValues(DPrime, x -> (x < 1e-12) ? 10000 : x);

	double[][] laplacianScores = MatrixTools.useFunctionOnMatrixes((a, b) -> a / b, LPrime, DPrime);
	laplacianScores = MatrixTools.transpose(laplacianScores);

	int[] ranking = MatrixTools.sortDescendant(MatrixTools.dropSingleton(laplacianScores));
	return ranking;

    }

    private static double[][] calculateDPrime(double[][] X, double[] D) {
	double[][] DPrime = MatrixTools.useFunctionRowWise(X, D, (a, b) -> a * b);

	DPrime = MatrixTools.useFunctionOnMatrixesTransposeA((a, b) -> a * b, DPrime, X);

	double[] sumOfLPrime = MatrixTools.sumOfElements(DPrime, false);

	return MatrixTools.vectorToMatrix(sumOfLPrime, false);
    }

    private static double[][] calculateLPrime(double[][] elementsMatrix, double[][] multiplicantMatrix) {
	double[][] elementsMatrixTrans = MatrixTools.transpose(elementsMatrix); // X'

	double[][] LPrime = MatrixTools.multiplicateMatrixesWrap(elementsMatrixTrans, multiplicantMatrix);

	LPrime = MatrixTools.useFunctionOnMatrixesTransposeA((a, b) -> a * b, LPrime, elementsMatrix); // (X'*L)'.*X

	double[] sumOfLPrime = MatrixTools.sumOfElements(LPrime, false); // sum((X'*D)'.*X)

	return MatrixTools.vectorToMatrix(sumOfLPrime, false);
    }
}
