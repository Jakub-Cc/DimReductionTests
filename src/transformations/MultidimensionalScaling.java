package transformations;

import core.SimpleMatrix;
import mdsj.MDSJ;

//TODO nalezy dodac w jakiejs formie wybor kryterium nie podobienstwa
public class MultidimensionalScaling implements BaseTransformation {

	
	@Override
	public SimpleMatrix Transform(SimpleMatrix Matrix)
	{
		double[][] input= generateDissimilarityMatrix(Matrix) ;
			
		System.out.println("Calculating MDS");
		double[][] output=MDSJ.classicalScaling(input); // apply MDS
		System.out.println("Finished Calculating MDS");	
			
		SimpleMatrix outputMatrix=new SimpleMatrix(output);
		outputMatrix.TransposeMatrix();
		return outputMatrix;
	}

	private double [][] generateDissimilarityMatrix(SimpleMatrix matrix)
	{
		System.out.println("Calculating Dissimilarity Matrix");
		double [][] input=matrix.getMatrix();
		
		double [][] output= new double[input.length ][input.length];
		
		for (int i=0; i<input.length; i++)
		{
			for (int j=i+1; j<input.length; j++)
			{
				output[i][j]= calculateDissimilarity(input[i] , input[j]);
				output[j][i]=output[i][j];
			}
		}
		System.out.println("Finishing Calculating Dissimilarity Matrix");
		return output;
	}
	
	private double calculateDissimilarity(double [] a, double [] b )
	{
		double output =0;
		for (int i=0; i<a.length; i++)
		{
			output+= Math.pow( (a[i]-b[i] ), 2) ;
		}
		
		return Math.sqrt(output);
	}
}

