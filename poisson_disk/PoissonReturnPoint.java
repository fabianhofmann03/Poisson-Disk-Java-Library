package poisson_disk;

public class PoissonReturnPoint {
		  double[] pos;
		  int mode;                  //0: Non-Active, 1: Active, 2: Half-Active
		  double rand;

		  public PoissonReturnPoint(PoissonPoint p, double[][] grid) {
		    this.pos = grid[p.getPos()];
		    this.mode = p.getMode();
		    this.rand = p.getRand();
		  }

		  public void setPos(double[] pos) {
		    this.pos = pos;
		  }

		  public void setMode(int mode) {
		    this.mode = mode;
		  }

		  public double[] getPos() {
		    return pos;
		  }

		  public int getMode() {
		    return mode;
		  }

		  public double getRand() {
		    return rand;
		  }
}
