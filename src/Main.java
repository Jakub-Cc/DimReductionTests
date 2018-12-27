import core.SimpleMatrix;
import transformations.BaseTransformation;
import transformations.MultidimensionalScaling;
import transformations.PrincipalComponentAnalysis;
import transformations.StarCordinates;

public class Main {

    public static void main(String[] args) {

	BaseTransformation transformation;
	int whatTransformation = 1;

	switch (whatTransformation) {
	case 0:
	    transformation = new StarCordinates();
	    break;
	case 2:
	    transformation = new PrincipalComponentAnalysis();
	    break;
	case 3:
	    transformation = new MultidimensionalScaling();
	    break;
	default:
	    transformation = new StarCordinates();
	}

	SimpleMatrix matrix = new SimpleMatrix();

	matrix.readDataFromCSV("te.csv");
	matrix.linearlyTransformMatrix();

	SimpleMatrix newMatrix = transformation.Transform(matrix);

	if (newMatrix != null)
	    newMatrix.saveToFile("test5.csv");

    }
}
