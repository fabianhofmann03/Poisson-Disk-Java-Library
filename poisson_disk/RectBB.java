package poisson_disk;

import java.util.Random;

public class RectBB implements BoundryBox {
	double[] position;
	double[] size;
	int positioning = 0;

	public RectBB() {
		position = new double[] {0, 0};
		size = new double[] {0, 0};
	}

	public RectBB(double[] position, double[] size) {
		this.position = position;
		this.size = size;
	}

	public RectBB(double[] position, int positioning, double[] size) {
		this.position = position;
		this.size = size;
		this.positioning = positioning;
	}

	public RectBB(double positionx, double positiony, double sizex, double sizey) {
		this.position = new double[] { positionx, positiony };
		this.size = new double[] { positionx, positiony };
	}

	public RectBB(double positionx, double positiony, int positioning, double sizex, double sizey) {
		this.position = new double[] { positionx, positiony };
		this.size = new double[] { positionx, positiony };
		this.positioning = positioning;
	}

	public void setPosition(double[] position) {
		this.position = position;

	}
	public void setPosition(double[] position, int positioning) {
		this.position = position;
		this.positioning = positioning;

	}
	public void setPosition(double positionx, double positiony) {
		this.position = new double[] { positionx, positiony };

	}
	public void setPosition(double positionx, double positiony, int positioning) {
		this.position = new double[] { positionx, positiony };
		this.positioning = positioning;

	}

	public void setSize(double[] size) {
		this.size = size;
	}

	public void setSize(int sizex, int sizey) {
		this.size = new double[] { sizex, sizey };
	}

	@Override
	public boolean intersecting(double[] position) {
		return this.intersecting(position[0], position[1]);
	}

	@Override
	public boolean intersecting(double positionx, double positiony) {
		return positionx >= posCalc(this.positioning, 3)[0] * size[0] + position[0]
				&& positionx <= posCalc(this.positioning, 5)[0] * size[0] + position[0]
				&& positiony >= posCalc(this.positioning, 1)[1] * size[1] + position[1]
				&& positiony <= posCalc(this.positioning, 7)[1] * size[1] + position[1];
	}

	private double[] posCalc(int from, int to) {
		double[][] list = new double[][] { { -0.5, -0.5 }, { 0, -0.5 }, { 0.5, -0.5 }, { -0.5, 0 }, { 0, 0 },
				{ 0.5, 0 }, { -0.5, 0.5 }, { 0, 0.5 }, { 0.5, 0.5 } };
		return new double[] { list[to][0] - list[from][0], list[to][1] - list[from][1] };
	}

	@Override
	public double[] closest(double[] position) {
		double[] result = new double[2];
		result[0] = Math.max(this.position[0] + posCalc(this.positioning, 3)[0] * this.size[0],
				Math.min(position[0], this.position[0] + posCalc(this.positioning, 5)[0] * this.size[0]));
		result[1] = Math.max(this.position[1] + posCalc(this.positioning, 1)[1] * this.size[1],
				Math.min(position[1], this.position[1] + posCalc(this.positioning, 7)[1] * this.size[1]));
		return result;
	}

	@Override
	public double[] closest(double positionx, double positiony) {
		return this.closest(new double[] { positionx, positiony });

	}
	
	@Override
	public double[] generateRandom() {
		double[] res = new double[2];
		
		Random rand = new Random();
		
		res[0] = rand.nextDouble() * (position[0] + posCalc(positioning, 5)[0] * size[0] - (position[0] + posCalc(positioning, 3)[0] * size[0])) + position[0] + posCalc(positioning, 3)[0] * size[0];
		res[1] = rand.nextDouble() * (position[1] + posCalc(positioning, 7)[1] * size[1] - (position[1] + posCalc(positioning, 1)[1] * size[1])) + position[1] + posCalc(positioning, 1)[1] * size[1];
		
		return res;
		
	}

}
