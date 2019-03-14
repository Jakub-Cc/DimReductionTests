package core;

import java.util.List;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import distanceMeasures.DistanceMeasure;

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

		SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
		RealMatrix realMatrix = spearmansCorrelation.computeCorrelationMatrix(matrix.matrix);

		double[][] correlationMat = realMatrix.getData();

		SimpleMatrix correlation = new SimpleMatrix(correlationMat);

		return correlation;
	}

	/*
	 * result[m,n]=func(matrix[n,m] , vector [n]) performes matrix'*diag(vector) if
	 * function is (a,b)->a*b
	 */
	public static double[][] useFunctionRowWise(double[][] matrix, double[] vector, Function2p func) {
		int aRows = matrix.length;
		int aColumns = matrix[0].length;
		int bRows = vector.length;

		if (aRows != bRows) {
			throw new IllegalArgumentException("Matrix:Rows: " + aRows + " did not match Vector:Rows " + bRows + ".");
		}

		double[][] result = new double[aColumns][aRows];

		for (int i = 0; i < aColumns; i++) {
			for (int j = 0; j < aRows; j++) {
				result[i][j] = func.f(matrix[j][i], vector[j]);
			}
		}
		return result;
	}

	/*
	 * Same as useFunctionOnMatrixes but A is used A'
	 */
	public static double[][] useFunctionOnMatrixesTransposeA(Function2p function, double[][] A, double[][] B) {
		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;

		if (aRows != bColumns) {
			throw new IllegalArgumentException("A:Rows: " + aRows + " did not match B:Columns " + bRows + ".");
		}
		else if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Columns: " + aColumns + " did not match B:Rows " + bColumns + ".");
		}

		double[][] result = new double[aColumns][aRows];
		for (int i = 0; i < aColumns; i++) {
			for (int j = 0; j < aRows; j++) {
				result[i][j] = function.f(A[j][i], B[i][j]);
			}
		}
		return result;
	}

	public static double[][] useFunctionOnMatrixes(Function2p function, double[][] A, double[][] B) {
		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;

		if (aRows != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aRows + " did not match B:Rows " + bRows + ".");
		}
		else if (aColumns != bColumns) {
			throw new IllegalArgumentException("A:Columns: " + aColumns + " did not match B:Columns " + bColumns + ".");
		}

		double[][] result = new double[aRows][aColumns];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < aColumns; j++) {
				result[i][j] = function.f(A[i][j], B[i][j]);
			}
		}
		return result;
	}

	public static double[] eigenValues(double[][] matrix) {
		RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
		EigenDecomposition decomposition = new EigenDecomposition(realMatrix);
		double[] eigenValues = decomposition.getRealEigenvalues();

		return eigenValues;
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

	public static double sumOfElements(double[] vector) {
		double result = 0;
		for (int i = 0; i < vector.length; i++) {
			result += vector[i];
		}
		return result;
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

	// TODO implement better function
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

	public static int[] sortAscending(double[] vector) {
		int[] result = new int[vector.length];

		// Map <Integer, Double> map= new HashMap<>();
		for (int i = 0; i < vector.length; i++) {
			result[i] = i;
		}
		for (int i = 0; i < vector.length; i++) {
			for (int j = i + 1; j < vector.length; j++) {
				if (vector[i] > vector[j]) {
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

	public static void printMatrix(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.printf("%.4f", matrix[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static void printMatrix(double[] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print(matrix[i] + " ");
		}
		System.out.println();
	}

	public static double[][] inverse(double[][] matrix) {
		RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
		RealMatrix inversedMatrix = MatrixUtils.inverse(realMatrix);

		return inversedMatrix.getData();
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
				if (Double.isNaN(matrix[i][j]) || matrix[i][j] == Double.NEGATIVE_INFINITY
						|| matrix[i][j] == Double.POSITIVE_INFINITY) {
					matrix[i][j] = valueToPut;
				}
			}
		}
	}

	public static double[][] ones(int rows, int columns) {
		double[][] result = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				result[j][i] = 1;
			}
		}
		return result;
	}

	public static void transformValues(double[][] matrix, Function function) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = function.f(matrix[i][j]);
			}
		}

	}

	private static double[][] vectorToMatrixColumn(double[] vector) {
		int size = vector.length;
		double[][] matrix = new double[size][1];
		for (int i = 0; i < size; i++) {
			matrix[i][0] = vector[i];
		}
		return matrix;
	}

	private static double[][] vectorToMatrixRow(double[] vector) {
		int size = vector.length;
		double[][] matrix = new double[1][size];
		for (int i = 0; i < size; i++) {
			matrix[0][i] = vector[i];
		}
		return matrix;
	}

	public static double[][] vectorToMatrix(double[] vector, boolean vectorInFirstDim) {
		if (vectorInFirstDim)
			return vectorToMatrixColumn(vector);
		else
			return vectorToMatrixRow(vector);

	}

	/**
	 * Using apache.commons-math3 matrix implementation to wrap given matrixes to
	 * perform multiplication more efficient on big matrixes
	 */
	public static double[][] multiplicateMatrixesWrap(double[][] A, double[][] B) {
		RealMatrix realMatrix = MatrixUtils.createRealMatrix(A);
		RealMatrix realMatrix2 = MatrixUtils.createRealMatrix(B);
		realMatrix = realMatrix.multiply(realMatrix2);

		return realMatrix.getData();
	}

	public static double[][] multiplicateMatrixes(double[][] A, double[][] B) {
		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
		}

		double[][] C = new double[aRows][bColumns];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bColumns; j++) {
				C[i][j] = 0.00000;
			}
		}

		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bColumns; j++) {
				for (int k = 0; k < aColumns; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}

		return C;
	}

	public static double[][] transformToDiagonalMatrix(double[] matrix) {
		int size = matrix.length;
		double[][] result = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				result[i][j] = 0;
			}
			result[i][i] = matrix[i];
		}
		return result;
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

	public static double[][] generateDistanceMatrix(double[][] matrix, DistanceMeasure distanceMeasure) {
		double[][] output = new double[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix.length; j++) {
				output[i][j] = distanceMeasure.getDistance(matrix[i], matrix[j]);
				output[j][i] = output[i][j];
			}
		}

		return output;
	}

	public static double[][] crossProduct(Function2p functon, double[] A, double[] B) {
		double[][] result = new double[A.length][B.length];

		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B.length; j++) {
				result[i][j] = functon.f(A[i], B[j]);
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

	public static double min(double[] matrix) {
		double min = matrix[0];
		for (int i = 1; i < matrix.length; i++) {
			if (min > matrix[i])
				min = matrix[i];
		}
		return min;
	}

	public static double[] minByRow(double[][] matrix) {

		double[] min = new double[matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			min[i] = min(matrix[i]);
		}
		return min;
	}

	public static double max(double[] vactor) {
		double max = vactor[0];
		for (int i = 1; i < vactor.length; i++) {
			if (max < vactor[i]) {
				max = vactor[i];
			}
		}
		return max;
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

	public static double[] dropSingleton(double[][] matrix) {
		if (matrix.length == 1) {
			return matrix[0];
		}
		else if (matrix[0].length == 1) {
			double[] result = new double[matrix.length];
			for (int i = 0; i < matrix.length; i++) {
				result[i] = matrix[i][0];
			}
			return result;
		}
		else {
			throw new IllegalArgumentException("Singleton dimnsion don't exist");
		}
	}

}
