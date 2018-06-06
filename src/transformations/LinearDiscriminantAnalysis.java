package transformations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import core.SimpleMatrix;
import nu.thiele.mllib.classifiers.IClassifier;
import nu.thiele.mllib.classifiers.MultiLayerPerceptron;
import nu.thiele.mllib.classifiers.NaiveBayes;
import nu.thiele.mllib.classifiers.NearestNeighbour;
import nu.thiele.mllib.data.Data;
import nu.thiele.mllib.data.DataSet;
import nu.thiele.mllib.data.Data.DataEntry;
import nu.thiele.mllib.utils.Testing;
import nu.thiele.mllib.utils.Utils;
import nu.thiele.mllib.utils.Testing.ClassifierResults;

public class LinearDiscriminantAnalysis implements BaseTransformation {
	
	Map<String, Integer> mapa = new HashMap<String, Integer>();
	Map<Integer, String> BackMapa = new HashMap<Integer, String>();
	
	
	@Override
	public SimpleMatrix Transform(SimpleMatrix matrix) 
	{
		 DataSet dataset = Data.getIrisTrainingSet();
		 DataSet testset = Data.getIrisTestSet();
		 
		 DataSet newdata= readDataFromCSVWithLabels("tes.csv");
		
		 dataset.x= new double[newdata.size()-100][ newdata.x[0].length];
		 dataset.y= new double[newdata.size()-100];
		
		 testset.x= new double[100][newdata.x[0].length];
		 testset.y= new double[100];
		 
		 int k=0;
		for (int i=0; i<newdata.size(); i++)
		{
			//System.out.print( newdata.y[i]);
			for (int j=0; j< newdata.x[0].length; j++ )
			{
				//System.out.print(" " + newdata.x[i][j]);
				if ( i%100 > 0)
				{
					dataset.x[ i - k ][j]=newdata.x[i][j];
					dataset.y[ i - k ]=newdata.y[i];
				}
				else
				{
					testset.x[ k ][j]=newdata.x[i][j];
					testset.y[ k ]=newdata.y[i];
				}
			}
			if ( i%100 == 0)
				k++;
			//System.out.println("");
		}
		
		System.out.println(testset.size());
		System.out.println(dataset.size());
		
		 //Usage of single classifier
		     
		 nu.thiele.mllib.classifiers.LinearDiscriminantAnalysis lda = new nu.thiele.mllib.classifiers.LinearDiscriminantAnalysis();
		    NearestNeighbour nn = new NearestNeighbour(2);
		    NaiveBayes nb = new NaiveBayes();
		    MultiLayerPerceptron mlp = new MultiLayerPerceptron(1, 0.5);
		    //mlp.build(5, new int[]{10}, 3);

		    IClassifier[] classifiers = {lda, nn, nb};
		    for(IClassifier classifier : classifiers){
		    	classifier.train(dataset.x, dataset.y);
		    }
		    
		     //Using testing tools
		    
		    System.out.println();
		    System.out.println("Using test set");
		    for(IClassifier cl : classifiers){
		    	ClassifierResults ldaR = Testing.testSet(cl, testset);
		    	System.out.println(cl.getClass()+" results: "+ldaR);
		    	ldaR = Testing.crossValidation(cl, dataset, 10);
		    	System.out.println(cl.getClass()+" cross validation: "+ldaR);
		    }
				    
		return null;
	}

	public DataSet readDataFromCSVWithLabels(String path) 
	{
		List<DataEntry> DataEntryList = new LinkedList<DataEntry>();
		
		File file= new File(path);
		
		Scanner input=null;
		try {
			input= new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int dimensions=0;
		int pointsAmount=0;
		int classHelper=1;
	
		
		while (input.hasNextLine())
		{
			pointsAmount++;
			String str=input.nextLine();
			String [] strs=str.split(";");
			dimensions= strs.length-2;
			double [] val= new double [dimensions];
			
			if ( !mapa.containsKey(strs[0]) )
			{
				mapa.put(strs[0],classHelper);
				BackMapa.put(classHelper,strs[0]);
				classHelper++;
			}
			
			for (int i=0;i<dimensions;i++)
			{
				val[i]= Double.parseDouble( strs[i+2]);
			}
			
			 
			DataEntryList.add(new DataEntry(val, (double)mapa.get(strs[0]) ) );
		}

		
		System.out.println(pointsAmount);
		System.out.println(dimensions);
		
		return Utils.listToDataSet(DataEntryList);
	}
	
}
