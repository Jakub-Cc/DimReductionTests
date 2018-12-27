import java.util.ArrayList;

import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.datatransform.kernel.KernelPCA;

public class KernelPcaTests {

    public static void main(String[] args) {
	System.out.println("test");

	ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();
	SimpleDataSet dataSet = new SimpleDataSet(dataPoints);

	KernelPCA kernelPCA = new KernelPCA(2);
	kernelPCA.setBasisSize(1000);
	kernelPCA.fit(dataSet);
    }

}
