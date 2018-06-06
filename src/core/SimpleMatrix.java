package core;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleMatrix {
	
	public double matrix[][];
	public int pointsAmount;
	public int dimensions;
	
	public SimpleMatrix()
	{
		this.pointsAmount= 0;
		this.dimensions=0;
		this.matrix =  null;
	}
	public SimpleMatrix(int pointsAmount, int dimensions)
	{
		this.pointsAmount= pointsAmount;
		this.dimensions=dimensions;
		this.matrix =  new double[pointsAmount][dimensions];
	}
	
	public SimpleMatrix( double [][] matrix)
	{
		this.matrix=matrix;
		this.pointsAmount=matrix.length;
		this.dimensions=matrix[0].length;
	}
	
	public double [][] GenerateFullMatrix (double [] values, int targetdDimensions )
	{
		dimensions=targetdDimensions;
		pointsAmount = (int) Math.pow( values.length , dimensions);
		matrix= new double [pointsAmount][dimensions];
		
		int i;
		for (int j=0; j< dimensions; j++)
		{
			i=0;
			for(int k=0; k<pointsAmount ;k++)
			{
				if( k + 1 > Math.pow( values.length ,  j) * (i +1 )  )
				{
					i=i+1;
				}
				matrix[k][j] = values[ i%values.length ];
			}
		}
		return matrix;
	}
	public void TransposeMatrix()
	{
		double [][] newmatrix= new double [dimensions][pointsAmount];
		for (int i=0; i< pointsAmount;i++)
		{
			for (int j=0; j< dimensions; j++)
			{
				newmatrix[j][i]=matrix[i][j];
			}
		}
		matrix=newmatrix;
		int helper=dimensions;
		dimensions=pointsAmount;
		pointsAmount=helper;
	}
	
	public void StandaryzeMatrix()
	{
		for (int i=0; i< dimensions; i++)
		{
			StandaryzeColum(i);
		}
	}
	
	void StandaryzeColum(int index)
	{
		double mean=0;
		double deviation=0;
		double variance=0;
		
		for (int i=0; i<pointsAmount ; i++)
		{
			mean += matrix[i][index] / pointsAmount;
		}
		for (int i=0; i<pointsAmount ; i++)
		{
			variance+= (matrix[i][index]-mean) * (matrix[i][index]-mean) / pointsAmount;
		}
		
		deviation = Math.pow(variance, 0.5);
		
		for (int i=0; i<pointsAmount ; i++)
		{
			matrix[i][index] = (matrix[i][index] - mean)/deviation;
		}
	}
	
	public void linearlyTransformMatrix()
	{
		for (int i=0; i< dimensions; i++)
		{
			linearlyTransformColum(i);
		}
	}
	
	void linearlyTransformColum(int index)
	{
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
			
		for (int i=0; i<pointsAmount ; i++)
		{
			if (min> matrix[i][index] )
			{ 
				min = matrix[i][index];
			}
			if (max< matrix[i][index] )
			{
				max = matrix[i][index];
			}
		}
		
		double range = max- min;
		for (int i=0; i<pointsAmount ; i++)
		{
			matrix[i][index] =  (matrix[i][index]- min ) / range;
		}
	}
	
	public double[][] getMatrix()
	{
		return matrix;
	}
	public int getPointsAmount()
	{
		return pointsAmount;
	}
	public int getDimenstions()
	{
		return dimensions;
	}
	
	 public static double[][] multiplicateMatrix(double[][] A, double[][] B) 
	 {
	        int aRows = A.length;
	        int aColumns = A[0].length;
	        int bRows = B.length;
	        int bColumns = B[0].length;

	        if (aColumns != bRows) {
	            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
	        }

	        double[][] C = new double[aRows][bColumns];
	        for (int i = 0; i < aRows; i++) {
	            for (int j = 0; j < bColumns; j++) {
	                C[i][j] = 0.00000;
	            }
	        }

	        for (int i = 0; i < aRows; i++) { // aRow
	            for (int j = 0; j < bColumns; j++) { // bColumn
	                for (int k = 0; k < aColumns; k++) { // aColumn
	                    C[i][j] += A[i][k] * B[k][j];
	                }
	            }
	        }

	        return C;
	}
	
	 
	public void saveToFile(String path)
	{
		FileWriter fileWriter=null;
		try 
		{
			fileWriter= new FileWriter( new File(path) );
			
			for (int i=0; i< pointsAmount; i++)
			{
				for (int j=0; j< dimensions ; j++)
				{
					fileWriter.write(matrix[i][j]+" ;");
				}
				fileWriter.write("\n");
			}
			fileWriter.close();
			System.out.println("File saved to "+path);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void readDataFromCSV(String path) 
	{
		File file= new File(path);
		
		Scanner input=null;
		try {
			input= new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<double[]> list = new ArrayList<>();
		
		pointsAmount=0;
		while (input.hasNextLine())
		{
			pointsAmount++;
			String str=input.nextLine();
			String [] strs=str.split(";");
			dimensions= strs.length;
			double [] val= new double [dimensions];
			for (int i=0;i<dimensions;i++)
			{
				val[i]= Double.parseDouble( strs[i]);
			}
			list.add(val);
		}

		matrix= new double[pointsAmount][dimensions];
		
		for (int i= 0; i<pointsAmount;i++)
		{
			for (int j=0; j<dimensions; j++)
			{
				matrix[i][j]=list.get(i)[j];
			}
			
		}
		System.out.println(pointsAmount);
		System.out.println(dimensions);
	}
	
	public String toString()
	{
		String str="";
		for (int i=0; i< pointsAmount; i++)
		{
			for (int j=0; j< dimensions ; j++)
			{
				str+=matrix[i][j]+" ;";
			}
			str+="\n";
		}
		
		return str;
	}
}
