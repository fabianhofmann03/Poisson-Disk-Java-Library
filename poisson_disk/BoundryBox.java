package poisson_disk;

public interface BoundryBox {
	public void setPosition(double[] position);
	public void setPosition(double[] position, int positioning);
	public void setPosition(double positionx, double positiony);
	public void setPosition(double positionx, double positiony, int positioning);
	
	public boolean intersecting(double[] position);
	public boolean intersecting(double positionx, double positiony);
	
	public double[] closest(double[] position);
	public double[] closest(double positionx, double positiony);
	
	public double[] generateRandom();
}
