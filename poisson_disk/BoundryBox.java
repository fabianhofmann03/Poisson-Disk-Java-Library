package poisson_disk;

public interface BoundryBox {
	public boolean intersecting(double[] position);
	public boolean intersecting(double positionx, double positiony);
	
	public double[] closest(double[] position);
	public double[] closest(double positionx, double positiony);
	
	public double[] generateRandom();
}
