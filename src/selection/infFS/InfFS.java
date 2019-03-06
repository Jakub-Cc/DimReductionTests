package selection.infFS;

import core.SimpleMatrix;
import selection.infFS.methods.MatrixTools;

public class InfFS {

	public int[] infFS(SimpleMatrix Xtrain, double alpha, boolean verbose) {

		int[] ranked = new int[Xtrain.dimensions];

		SimpleMatrix corr = MatrixTools.spearmanCorrelation(Xtrain);
		MatrixTools.replaceUndef(corr.matrix, 0);
		MatrixTools.transformValues(corr.matrix, x -> 1 - Math.abs(x));

		double[] STD = MatrixTools.standardDeviation(Xtrain.matrix, false);
		double[][] sigma = MatrixTools.bsxfun((a, b) -> a > b ? a : b, STD, STD);
		double min = MatrixTools.min(sigma);
		MatrixTools.transformValues(sigma, e -> e - min);

		double max = MatrixTools.max(sigma);
		MatrixTools.transformValues(sigma, e -> e / max);
		MatrixTools.replaceUndef(sigma, 0);

		MatrixTools.transformValues(corr.matrix, x -> alpha * x);
		MatrixTools.transformValues(sigma, x -> (1 - alpha) * x);

		double[][] A = MatrixTools.useFunctionOnMatrixes((a, b) -> a + b, corr.matrix, sigma);

		double[][] I = MatrixTools.eye(A.length);

		double factor = 0.99;
		double r = (factor / MatrixTools.max(MatrixTools.eigenValues(A)));

		MatrixTools.transformValues(A, x -> x * r);
		double[][] y = MatrixTools.useFunctionOnMatrixes((a, b) -> a - b, I, A);

		double[][] S = MatrixTools.useFunctionOnMatrixes((a, b) -> a - b, MatrixTools.inverse(y), I);

		double[] weight = MatrixTools.sumOfElements(S, false);

		ranked = MatrixTools.sortDescendant(weight);

		return ranked;
	}

}
