package selection.infFS.methods;

import java.util.List;

import core.Function;
import core.Function2p;
import core.SimpleMatrix;

public class MatrixTools {

	public static void main(String[] args) {
		SimpleMatrix simpleMatrix = new SimpleMatrix(8, 8);
		simpleMatrix.GenerateFullMatrix(new double[] { 1, 2 }, 2);

		// transformValues(simpleMatrix.matrix, x -> 1 - x);

		System.out.println(simpleMatrix);

		double[] sdev = standardDeviation(simpleMatrix.matrix, false);

		double min = min(simpleMatrix.matrix);
		System.out.println(min);

		double max = max(simpleMatrix.matrix);
		System.out.println(max);

		transformValues(simpleMatrix.matrix, e -> e - min);
		System.out.println(simpleMatrix.toString());

		transformValues(simpleMatrix.matrix, e -> e / max);
		System.out.println(simpleMatrix.toString());

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

	public static double[][] useFunctionOnMatrixes(Function2p function, double[][] A, double[][] B) {
		int firstDimSize = A.length;
		int secondDimSize = A[0].length;

		double[][] result = new double[firstDimSize][secondDimSize];
		for (int i = 0; i < firstDimSize; i++) {
			for (int j = 0; j < secondDimSize; j++) {
				result[i][j] = function.f(A[i][j], B[i][j]);
			}
		}
		return result;
	}

	public static double[][] eigenValues(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[][] result = new double[firstDimSize][secondDimSize];

		// TODO

		return result;
	}

	public static double[][] eye(int size) {
		int firstDimSize = size;
		int secondDimSize = size;

		double[][] result = new double[firstDimSize][secondDimSize];
		for (int i = 0; i < firstDimSize; i++) {
			for (int j = 0; j < secondDimSize; j++) {
				result[i][j] = 0;
			}
			result[i][i] = 1;
		}
		return result;
	}

	public static double[][] addMatrixes(double[][] A, double[][] B) {
		int firstDimSize = A.length;
		int secondDimSize = A[0].length;

		double[][] result = new double[firstDimSize][secondDimSize];
		for (int i = 0; i < firstDimSize; i++) {
			for (int j = 0; j < secondDimSize; j++) {
				result[i][j] = A[i][j] + B[i][j];
			}
		}
		return result;
	}

	public static void replaceValue(double[][] matrix, double valueToPut, List<Double> valuesToReplace) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (valuesToReplace.contains(matrix[i][j]))
					matrix[i][j] = valueToPut;
			}
		}
	}

	public static double[] sumOfElements(double[][] matrix, boolean firstDim) {
		if (firstDim)
			return sumOfElementsFirstDim(matrix);
		else
			return sumOfElementsSecondDim(matrix);
	}

	private static double[] sumOfElementsFirstDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] sum = new double[firstDimSize];

		for (int i = 0; i < firstDimSize; i++) {
			sum[i] = 0;
			for (int j = 0; j < secondDimSize; j++) {
				sum[i] += matrix[i][j];
			}
		}
		return sum;
	}

	public static int[] sortDescendant(double[] vector) {
		int[] result = new int[vector.length];

		// Map <Integer, Double> map= new HashMap<>();
		for (int i = 0; i < vector.length; i++) {
			result[i] = i;
		}
		for (int i = 0; i < vector.length; i++) {
			for (int j = i + 1; j < vector.length; j++) {
				if (vector[i] < vector[j]) {
					double temp = vector[i];
					vector[i] = vector[j];
					vector[j] = temp;
					int tmp = result[i];
					result[i] = result[j];
					result[j] = tmp;
				}
			}
		}

		return result;
	}

	public static double[][] inverse(double[][] matrix) {
		// TODO
		return null;
	}

	private static double[] sumOfElementsSecondDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] sum = new double[secondDimSize];

		for (int i = 0; i < secondDimSize; i++) {
			sum[i] = 0;
			for (int j = 0; j < firstDimSize; j++) {
				sum[i] += matrix[j][i];
			}
		}
		return sum;
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

	public static double[][] transpose(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[][] transposed = new double[secondDimSize][firstDimSize];

		for (int i = 0; i < firstDimSize; i++) {
			for (int j = 0; j < secondDimSize; j++) {
				transposed[j][i] = matrix[i][j];
			}
		}
		return transposed;
	}

	public static double[] standardDeviation(double[][] matrix, boolean fistDim) {
		if (fistDim)
			return standardDeviationFirstDim(matrix);
		else
			return standardDeviationSecondDim(matrix);
	}

	private static double[] standardDeviationFirstDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] standardDeviation = new double[firstDimSize];
		double[] mean = mean(matrix, true);

		for (int i = 0; i < firstDimSize; i++) {
			standardDeviation[i] = 0;
			for (int j = 0; j < secondDimSize; j++) {
				standardDeviation[i] += Math.pow(matrix[i][j] - mean[i], 2) / (firstDimSize - 1);
			}
		}
		return standardDeviation;
	}

	public static double[] standardDeviationSecondDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] standardDeviation = new double[secondDimSize];
		double[] mean = mean(matrix, false);

		for (int i = 0; i < secondDimSize; i++) {
			standardDeviation[i] = 0;
			for (int j = 0; j < firstDimSize; j++) {
				standardDeviation[i] += Math.pow(matrix[j][i] - mean[i], 2) / (firstDimSize - 1);
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

	// TODO check later correct functionality for multi dim matrixes
	public static double[][] bsxfun(Function2p functon, double[] A, double[] B) {
		double[][] result = new double[A.length][B.length];

		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B.length; j++) {
				result[i][j] = functon.f(A[i], B[i]);
			}
		}

		return result;
	}

	public static double min(double[][] matrix) {
		double min = matrix[0][0];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (min > matrix[i][j]) {
					min = matrix[i][j];
				}
			}
		}
		return min;
	}

	public static double max(double[][] matrix) {
		double max = matrix[0][0];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (max < matrix[i][j]) {
					max = matrix[i][j];
				}
			}
		}
		return max;
	}
}
