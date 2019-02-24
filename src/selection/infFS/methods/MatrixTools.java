package selection.infFS.methods;

import java.util.List;

import core.Function;
import core.SimpleMatrix;

public class MatrixTools {

	public static void main(String[] args) {
		SimpleMatrix simpleMatrix = new SimpleMatrix(8, 8);
		simpleMatrix.GenerateFullMatrix(new double[] { 1, 2 }, 2);

		// transformValues(simpleMatrix.matrix, x -> 1 - x);

		System.out.println(simpleMatrix);

		double[] sdev = standardDeviation(simpleMatrix.matrix);

		for (double d : sdev)
			System.out.print(" " + d);
	}

	public static SimpleMatrix spearmanCorrelation(SimpleMatrix matrix) {
		int dimension = matrix.dimensions;
		SimpleMatrix correlation = new SimpleMatrix(dimension, dimension);

		// TODO
		// http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/stat/correlation/SpearmansCorrelation.html
		// ??

		return correlation;
	}

	public static void replaceValue(double[][] matrix, double valueToPut, List<Double> valuesToReplace) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (valuesToReplace.contains(matrix[i][j]))
					matrix[i][j] = valueToPut;
			}
		}
	}

	public static void replaceUndef(double[][] matrix, double valueToPut) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == Double.NaN || matrix[i][j] == Double.NEGATIVE_INFINITY
						|| matrix[i][j] == Double.POSITIVE_INFINITY)
					matrix[i][j] = valueToPut;
			}
		}
	}

	public static void transformValues(double[][] matrix, Function function) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = function.f(matrix[i][j]);
			}
		}

	}

	public static double[] standardDeviation(double[][] matrix, boolean fistDim) {
		if (fistDim)
			return standardDeviationFirstDim(matrix);
		else
			return standardDeviationSecondDim(matrix);
	}

	private static double[] standardDeviationFirstDim(double[][] matrix) {
		double[] standardDeviation = new double[matrix.length];
		double[] mean = mean(matrix, true);

		for (int i = 0; i < matrix.length; i++) {
			standardDeviation[i] = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				standardDeviation[i] += 0;
			}
		}
		return standardDeviation;
	}

	public static double[] standardDeviationSecondDim(double[][] matrix) {
		int points = matrix.length;
		int dims = matrix[0].length;

		double[] standardDeviation = new double[dims];
		double[] mean = mean(matrix, false);

		for (int i = 0; i < dims; i++) {
			standardDeviation[i] = 0;
			for (int j = 0; j < points; j++) {
				standardDeviation[i] += Math.pow(matrix[j][i] - mean[i], 2) / (points - 1);
			}
			standardDeviation[i] = Math.sqrt(standardDeviation[i]);
		}

		return standardDeviation;
	}

	/*
	 * calculate mean of matrix, by first or second dimension
	 */
	public static double[] mean(double[][] matrix, boolean firstDim) {
		if (firstDim)
			return meanFirstDim(matrix);
		else
			return meanSecondDim(matrix);
	}

	private static double[] meanFirstDim(double[][] matrix) {
		double[] mean = new double[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			mean[i] = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				mean[i] += matrix[i][j] / matrix[i].length;
			}
		}

		return mean;
	}

	private static double[] meanSecondDim(double[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;

		double[] mean = new double[n];

		for (int i = 0; i < n; i++) {
			mean[i] = 0;
			for (int j = 0; j < m; j++) {
				mean[i] += matrix[j][i] / m;
			}
		}
		return mean;
	}
}
