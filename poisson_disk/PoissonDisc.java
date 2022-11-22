package poisson_disk;

import java.util.ArrayList;
import java.util.Random;

public class PoissonDisc {
	BoundryBox bb;
	double r;
	final double k = 30;
	double[][] grid;
	double w;
	int rows;
	int cols;
	ArrayList<Integer> active;
	ArrayList<Integer> half_active;
	ArrayList<PoissonPoint> points;

	double sizex, sizey;

	Random rand;

	public PoissonDisc(double sizex, double sizey, double r) {
		rand = new Random();

		this.active = new ArrayList<Integer>();
		this.half_active = new ArrayList<Integer>();
		this.points = new ArrayList<PoissonPoint>();
		this.sizex = sizex;
		this.sizey = sizey;
		this.r = r;
		this.w = r / (double) Math.sqrt(2);

		bb = null;

		// Setup
		rows = (int) Math.floor(sizey / w);
		cols = (int) Math.floor(sizex / w);

		grid = new double[rows * cols][2];
		for (int x = 0; x < rows * cols; x++) {
			grid[x] = null;
		}
	}

	public void setFirst() {
		double x0_x;
		double x0_y;

		if (bb == null) {
			x0_x = (int) Math.floor(rand.nextDouble() * this.sizex);
			x0_y = (int) Math.floor(rand.nextDouble() * this.sizey);
		} else {
			double[] x0 = bb.generateRandom();
			x0_x = x0[0];
			x0_y = x0[1];
		}

		grid[(int)Math.floor(x0_x / w) + (int)Math.floor(x0_y / w) * cols] = new double[] {x0_x, x0_y};
		points.add(new PoissonPoint((int)Math.floor(x0_x / w) + (int)Math.floor(x0_y / w) * cols, 1));
		active.add((int)Math.floor(x0_x / w) + (int)Math.floor(x0_y / w) * cols);
	}

	private void sub_calc_missing() {
		int i = (int)Math.floor(rand.nextInt(active.size())); // Choose from active list
		boolean points_found = false; // If one of the test-points was ok
		double str_angle = rand.nextDouble() * Math.PI * 2; // Random angle where point search starts

		for (int q = 0; q < k; q++) { // Search new points k times
			double angle = rand.nextDouble() * ((Math.PI * 2 / k * (q + 1)) - (Math.PI * 2 / k * q)) + (Math.PI * 2 / k * q) + str_angle; 
			double magn = r * (rand.nextDouble() + 1);

			double[] new_point = new double[2];
			new_point[0] = Math.cos(angle) * magn + grid[active.get(i)][0];	// New calculated point
			new_point[1] = Math.sin(angle) * magn + grid[active.get(i)][1];

			int grid_x = (int)Math.floor(new_point[0] / w); // Calculating grid position
			int grid_y = (int)Math.floor(new_point[1] / w);
			
			if (grid_x >= 0 && grid_x < cols && grid_y >= 0 && grid_y < rows) {
				boolean pos_ok = true;
				start_loop: for (int x = -2; x <= 2; x++) { // Searching points arround new point
					for (int y = -2; y <= 2; y++) {
						if ((grid_x + x) >= 0 && (grid_x + x) < cols && (grid_y + y) >= 0 && (grid_y + y) < rows) { // Is chosen grid cell out of grid
							if (grid[(grid_x + x) + (grid_y + y) * cols] != null) { // Is grid cell not empty
								
								if (Math.sqrt(Math.pow((new_point[0] - (grid[(grid_x + x) + (grid_y + y) * cols][0])), 2) + Math.pow((new_point[1] - (grid[(grid_x + x) + (grid_y + y) * cols][1])), 2)) < r) { // Is point in cell too close to new cell
									pos_ok = false; 																																						// New point is useless
									break start_loop;
								}
							}
						}
					}
				}

				if (pos_ok) { // Point found
					points_found = true;
					grid[grid_x + grid_y * cols] = new_point;
					if (bb == null || bb.intersecting(new_point)) {
						active.add(grid_x + grid_y * cols);
						points.add(new PoissonPoint(grid_x + grid_y * cols, 1));
						break;
					} else {
						half_active.add(grid_x + grid_y * cols);
						points.add(new PoissonPoint(grid_x + grid_y * cols, 2));
					}
				}
			}
		}
		if (!points_found) {
			for (int x = 0; x < points.size(); x++) {
				if (points.get(x).getPos() == active.get(i)) {
					points.get(x).setMode(0);
					break;
				}
			}
			active.remove(i);
		}
	}

	public void calcMissing() {
		while (active.size() > 0) {
			sub_calc_missing();
		}
	}

	public void calcMissingOnce() {
		if (active.size() > 0) {
			sub_calc_missing();
		}
	}

	public void setBoundingBox(BoundryBox bb) {
		this.bb = bb;
	}

	public void deleteBoundingBox() {
		this.bb = null;
	}

	public void updateGrid() {
		this.updateGrid(new double[] {0, 0});
	}
	
	public void updateGrid(double upd_x, double upd_y) {
		this.updateGrid(new double[] {upd_x, upd_y});
	}

	public void updateGrid(double[] change) {
		double[][] new_grid = new double[rows * cols][2];
		for (int x = 0; x < rows * cols; x++) {
			new_grid[x] = null;
		}

		active = new ArrayList<Integer>();
		half_active = new ArrayList<Integer>();

		ArrayList<Integer> remover = new ArrayList<Integer>();

		for (int x = 0; x < points.size(); x++) {
			double[] new_pos = grid[points.get(x).getPos()];
			// println(new_pos);
			new_pos[0] += change[0];
			new_pos[1] += change[1];

			int grid_x = (int)Math.floor(new_pos[0] / w); // Calculating grid position
			int grid_y = (int)Math.floor(new_pos[1] / w);
			
			if (grid_x >= 0 && grid_x < cols && grid_y >= 0 && grid_y < rows
					&& (bb.intersecting(new_pos) || (Math.sqrt(Math.pow(bb.closest(new_pos)[0] - new_pos[0], 2) + Math.pow(bb.closest(new_pos)[1] - new_pos[1], 2)) < 2 * r))) {
				new_grid[grid_x + grid_y * cols] = new_pos;
				points.get(x).setPos(grid_x + grid_y * cols);

				if (bb != null && !bb.intersecting(new_pos)) {
					half_active.add(grid_x + grid_y * cols);
					points.get(x).setMode(2);
				} else if (points.get(x).getMode() == 2 || points.get(x).getMode() == 1) {
					active.add(grid_x + grid_y * cols);
					points.get(x).setMode(1);
				} else {
					points.get(x).setMode(0);
				}
			} else {
				remover.add(x);
			}
		}

		for (int x = remover.size() - 1; x >= 0; x--) {
			points.remove((int) remover.get(x));
		}

		grid = new_grid;
	}

	public ArrayList<PoissonReturnPoint> getPoints() {
		ArrayList<PoissonReturnPoint> return_list = new ArrayList<PoissonReturnPoint>();
		for (int x = 0; x < points.size(); x++) {
			return_list.add(new PoissonReturnPoint(points.get(x), grid));
		}
		return return_list;
	}
}
