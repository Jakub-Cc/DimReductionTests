import java.io.File;
import java.util.Random;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;

public class AttributeSelectionTest {
    /**
     * uses the meta-classifier
     */
    protected static void useClassifier(Instances data) throws Exception {
	System.out.println("\n1. Meta-classfier");
	AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
	CfsSubsetEval eval = new CfsSubsetEval();
	GreedyStepwise search = new GreedyStepwise();
	search.setSearchBackwards(true);
	J48 base = new J48();
	classifier.setClassifier(base);
	classifier.setEvaluator(eval);
	classifier.setSearch(search);
	Evaluation evaluation = new Evaluation(data);
	evaluation.crossValidateModel(classifier, data, 10, new Random(1));
	System.out.println(evaluation.toSummaryString());
    }

    /**
     * uses the filter
     */
    protected static void useFilter(Instances data) throws Exception {
	System.out.println("\n2. Filter");
	weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();
	CfsSubsetEval eval = new CfsSubsetEval();
	GreedyStepwise search = new GreedyStepwise();
	search.setSearchBackwards(true);
	filter.setEvaluator(eval);
	filter.setSearch(search);
	filter.setInputFormat(data);
	Instances newData = Filter.useFilter(data, filter);
	System.out.println(newData.toSummaryString());

    }

    /**
     * uses the low level approach
     */
    protected static void useLowLevel(Instances data) throws Exception {
	System.out.println("\n3. Low-level");
	AttributeSelection attsel = new AttributeSelection();
	CfsSubsetEval eval = new CfsSubsetEval();
	GreedyStepwise search = new GreedyStepwise();
	search.setSearchBackwards(true);
	attsel.setEvaluator(eval);
	attsel.setSearch(search);
	attsel.SelectAttributes(data);
	int[] indices = attsel.selectedAttributes();
	System.out.println("selected attribute indices (starting with 0):\n" + Utils.arrayToString(indices));
    }

    /**
     * takes a dataset as first argument
     *
     * @param args the commandline arguments
     * @throws Exception if something goes wrong
     */
    public static void main(String[] args) throws Exception {
	// load data
	System.out.println("\n0. Loading data");
	// DataSource source = new DataSource("te3.csv");

	// System.out.println(source.getClass());
	// System.out.println(source.getLoader().getClass());

	CSVLoader loader = new CSVLoader();
	loader.setSource(new File("te3.csv"));

	// Instances data = source.getDataSet();
	Instances data = loader.getDataSet();

	// if (data.classIndex() == -1)
	// data.setClassIndex(data.numAttributes() - 1);

	System.out.println(data.classIndex());
	System.out.println(data.numAttributes());

	data.setClassIndex(data.numAttributes() - 1);

	// 1. meta-classifier
	// useClassifier(data);

	// 2. filter
	useFilter(data);

	// 3. low-level
	useLowLevel(data);

	System.out.println();
	ArffSaver saver = new ArffSaver();
	saver.setInstances(data);
	saver.setDestination(new File("art.arff"));
	// saver.writeBatch();
    }
}
