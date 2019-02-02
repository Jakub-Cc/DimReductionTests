import java.util.ArrayList;

import core.SimpleMatrix;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.datatransform.kernel.KernelPCA;
import jsat.linear.DenseVector;

public class KernelPcaTests {

    public static void main(String[] args) {
	System.out.println("test");

	ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

	SimpleMatrix matrix = new SimpleMatrix();
	matrix.readDataFromCSV("te.csv");
	for (int i = 0; i < matrix.pointsAmount; i++) {
	    DenseVector numericalValues = new DenseVector(matrix.matrix[i]);
	    dataPoints.add(new DataPoint(numericalValues));
	}

	SimpleDataSet dataSet = new SimpleDataSet(dataPoints);

	KernelPCA kernelPCA = new KernelPCA(2);
	kernelPCA.setBasisSize(10000);
	kernelPCA.fit(dataSet);
	System.out.println("fiting ended");

	DataPoint datapoint;
	SimpleMatrix matrix2 = new SimpleMatrix(dataPoints.size(), 2);
	int i = 0;
	for (DataPoint dp : dataPoints) {
	    datapoint = kernelPCA.transform(dp);
	    matrix2.matrix[i++] = datapoint.getNumericalValues().arrayCopy();
	}

	matrix2.saveToFile("KernelPCA.csv");

	System.out.println("end");
    }

}
