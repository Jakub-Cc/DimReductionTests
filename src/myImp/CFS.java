package myImp;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import core.MatrixTools;
import core.SimpleMatrix;

public class CFS {

	public static void main(String[] args) {
		SimpleMatrix matrix = new SimpleMatrix();
		matrix.readDataFromCSV("x.csv");
		System.out.println();

		long start = System.currentTimeMillis();
		{

			int[] rank = cfs(matrix.matrix);

			for (int i = 0; i < rank.length; i++)
				System.out.println(rank[i] + 1);

		}

		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println();
		System.out.println((double) elapsedTime / 1000);
	}

	public static int[] cfs(double[][] matrix) {
		PearsonsCorrelation per = new PearsonsCorrelation(matrix);
		double[][] corr = per.getCorrelationMatrix().getData();
		MatrixTools.transformValues(corr, e -> Math.abs(e));

		MatrixTools.replaceUndef(corr, 1);
		double min[] = MatrixTools.minByRow(corr);

		return MatrixTools.sortAscending(min);
	}
}
