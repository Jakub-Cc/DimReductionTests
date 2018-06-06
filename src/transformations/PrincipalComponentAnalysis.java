package transformations;

import core.SimpleMatrix;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

//TODO przeniesc z innego projektu biblioteki aby pca dzialalo tutaj a nie na samych eigenvektorach albo obliczyc z eigenwektorow redukcje
public class PrincipalComponentAnalysis implements BaseTransformation {

	@Override
	public SimpleMatrix Transform(SimpleMatrix matrix) {
		double[][] pointsArray = matrix.getMatrix();

		//create real matrix
		RealMatrix realMatrix = MatrixUtils.createRealMatrix(pointsArray);

		//create covariance matrix of points, then find eigen vectors
		//see https://stats.stackexchange.com/questions/2691/making-sense-of-principal-component-analysis-eigenvectors-eigenvalues

		Covariance covariance = new Covariance(realMatrix);
		RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
		EigenDecomposition ed = new EigenDecomposition(covarianceMatrix);
		
		
		double[][] newmat = ed.getD().getData();
		System.out.println(  ed.getEigenvector(0));
		System.out.println(  ed.getEigenvector(1));
		System.out.println(  ed.getEigenvector(2));
		System.out.println(  ed.getEigenvector(3));
		System.out.println(  ed.getEigenvector(4));
		
		for ( int i=0; i< ed.getImagEigenvalues().length; i++ )
			System.out.println(  ed.getImagEigenvalues()[i] );
		
		for ( int i=0; i< ed.getRealEigenvalues().length; i++ )
			System.out.println(  ed.getRealEigenvalues()[i] );
		
	
		 newmat =(  realMatrix.multiply( ed.getV()) ).getData();
		 
		return new SimpleMatrix(newmat);
	}

}
