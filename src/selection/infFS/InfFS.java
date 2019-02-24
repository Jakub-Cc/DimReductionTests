package selection.infFS;

import core.SimpleMatrix;
import selection.infFS.methods.MatrixTools;

public class InfFS {

	public int[] infFS(SimpleMatrix Xtrain, double alpha, boolean verbose) {

		int[] ranked = new int[Xtrain.dimensions];

		SimpleMatrix corr = MatrixTools.spearmanCorrelation(Xtrain);
		MatrixTools.replaceUndef(corr.matrix, 0);
		MatrixTools.transformValues(corr.matrix, x -> 1 - Math.abs(x));

		return ranked;
	}

}
