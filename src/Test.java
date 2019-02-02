import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.featureselection.ensemble.LinearRankingEnsemble;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;
import net.sf.javaml.featureselection.scoring.GainRatio;
import net.sf.javaml.featureselection.subset.GreedyForwardSelection;
import net.sf.javaml.tools.data.FileHandler;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Dataset data = FileHandler.loadDataset(new File("te.csv"), ";");
		System.out.println(data.size());
		/* Construct a greedy forward subset selector */
		GreedyForwardSelection ga = new GreedyForwardSelection(2, new PearsonCorrelationCoefficient());
		/* Apply the algorithm to the data set */
		ga.build(data);
		/* Print out the attribute that has been selected */
		System.out.println(ga.selectedAttributes());

		System.out.println();
		RecursiveFeatureEliminationSVM[] svmrfes = new RecursiveFeatureEliminationSVM[10];
		for (int i = 0; i < svmrfes.length; i++)
			svmrfes[i] = new RecursiveFeatureEliminationSVM(0.2);
		LinearRankingEnsemble ensemble = new LinearRankingEnsemble(svmrfes);
		/* Build the ensemble */
		ensemble.build(data);
		/* Get rank of i-th feature */
		for (int i = 0; i < 5; i++) {
			System.out.println(ensemble.rank(i));
		}

		System.out.println();
		RecursiveFeatureEliminationSVM svmrfe = new RecursiveFeatureEliminationSVM(0.2);
		/* Apply the algorithm to the data set */
		svmrfe.build(data);
		/* Print out the rank of each attribute */
		for (int i = 0; i < svmrfe.noAttributes(); i++)
			System.out.println(svmrfe.rank(i));

		System.out.println();
		GainRatio gain = new GainRatio();
		/* Apply the algorithm to the data set */
		gain.build(data);
		/* Print out the score of each attribute */
		for (int i = 0; i < 5; i++)
			System.out.println(gain.score(i));
	}

}
