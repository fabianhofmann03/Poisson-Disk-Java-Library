package poisson_disk;

import java.util.Random;

public class PoissonPoint {
	int grid_pos;
	int mode; // 0: Non-Active, 1: Active, 2: Half-Active
	Random r;
	double rand;

	PoissonPoint(int pos, int mode) {
	    this.grid_pos = pos;
	    this.mode = mode;
	    this.r = new Random();
	    this.rand = r.nextDouble();
	  }

	void setPos(int pos) {
		this.grid_pos = pos;
	}

	void setMode(int mode) {
		this.mode = mode;
	}

	int getPos() {
		return grid_pos;
	}

	int getMode() {
		return mode;
	}

	double getRand() {
		return rand;
	}
}
