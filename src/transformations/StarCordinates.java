package transformations;
import core.SimpleMatrix;

public class StarCordinates implements BaseTransformation {

	@Override
	public SimpleMatrix Transform(SimpleMatrix matrix) 
	{
		double newMatrix[][] = new double [matrix.getPointsAmount()][2];
		double projectionVector[][]=new double [matrix.getDimenstions()][2];
		
		for(int i=1; i<= matrix.getDimenstions(); i++)
		{
			projectionVector[i-1][0]=Math.cos( 2*Math.PI *i/matrix.getDimenstions() );
			projectionVector[i-1][1]=Math.sin( 2*Math.PI *i/matrix.getDimenstions() );
		}
		
		newMatrix=SimpleMatrix.multiplicateMatrix(  matrix.getMatrix() , projectionVector);
		
		return new SimpleMatrix(newMatrix);
	}

}
