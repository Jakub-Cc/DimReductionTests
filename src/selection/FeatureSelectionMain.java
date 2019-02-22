package selection;

import java.util.Set;

/**
 * The main class for running feature selection from command line. Also shows how this
 * should be used from java code.
 */
public class FeatureSelectionMain {

    private static void printHelp() {
	System.out.println("-------------------");
	System.out.println("PARAMETERS");
	System.out.println("1 - String. Type of selection to use. Must be one of: SFS, SBS, SFFS, SFBS");
	System.out.println("2 - String. Training filename (must be .arff file)");
	System.out.println(
		"3 - String (OPTIONAL). Testing filename to use (must be .arff file). If no testing file is specified the training file will be split.");
	System.out.println("4 - Integer. Max iterations without progression, used for the stopping criteria. ");

	System.out.println("-------------------");

    }

    public static void main(String[] args) throws Exception {

	String type = "SBS";
	String fileName = "a.arff";
	String testingFileName = null;
	int maxIterationsWithoutProgress = 10000;

	FeatureSelection featureSelection;

	FeatureSelection selector = null;
	switch (type.toUpperCase()) {
	case "SBS":
	    selector = testingFileName == null ? new SequentialBackwardSelection(fileName, maxIterationsWithoutProgress)
		    : new SequentialBackwardSelection(fileName, testingFileName, maxIterationsWithoutProgress);
	    break;
	case "SFS":
	    selector = testingFileName == null ? new SequentialForwardSelection(fileName, maxIterationsWithoutProgress)
		    : new SequentialForwardSelection(fileName, testingFileName, maxIterationsWithoutProgress);
	    break;
	case "SFBS":
	    selector = testingFileName == null
		    ? new SequentialFloatingBackwardSelection(fileName, maxIterationsWithoutProgress)
		    : new SequentialFloatingBackwardSelection(fileName, testingFileName, maxIterationsWithoutProgress);
	    break;
	case "SFFS":
	    selector = testingFileName == null
		    ? new SequentialFloatingForwardSelection(fileName, maxIterationsWithoutProgress)
		    : new SequentialFloatingForwardSelection(fileName, testingFileName, maxIterationsWithoutProgress);
	    break;
	default:
	    System.out.println("INVALID TYPE SPECIFIED");
	    printHelp();
	    return;
	}

	System.out.println("FEATURE SELECTION - " + type);
	System.out.println("-------------------");
	Set<Integer> selectedIndices = selector.select();
	selector.compareTestingAccuracy(selectedIndices);
	System.out.println("-------------------");

    }

}
