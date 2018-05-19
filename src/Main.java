import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public double matrix[][];
	public int pointsAmount=0;
	public int dimensions =0;
	
	public static void main(String[] args) {
		Main main = new Main();
		
		//main.readDataFromCSV("te.csv");
		//main.saveToFile("test.csv");
		main.GenerateDiffMatrix( new double[] {1,0}, 4);

		main.linearlyTransformMatrix();
		//main.saveToFile("test2.csv");
		System.out.println(main.toString());
		
		main.transformIntoStarCordinates();
		//main.saveToFile("test3.csv");
		
		main.saveToFile("test4.csv");
	}

	 public static double[][] multiplicar(double[][] A, double[][] B) 
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
	 
	public double [][] transformIntoStarCordinates()
	{
		double newMatrix[][] = new double [pointsAmount][2];
		double projectionVector[][]=new double [dimensions][2];
		
		for(int i=1; i<= dimensions; i++)
		{
			projectionVector[i-1][0]=Math.cos( 2*Math.PI *i/dimensions );
			projectionVector[i-1][1]=Math.sin( 2*Math.PI *i/dimensions );
		}
		
		newMatrix=multiplicar(  matrix , projectionVector);
		
		matrix=newMatrix;
		dimensions=2;
		return newMatrix;
	}
	
	
	public double [][] GenerateDiffMatrix (double [] values, int targetdDimensions )
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
	
	void StandaryzeMatrix()
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
	
	void linearlyTransformMatrix()
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
