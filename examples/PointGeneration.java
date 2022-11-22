package com.fabi.poisson_test;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import poisson_disk.PoissonDisc;
import poisson_disk.PoissonReturnPoint;

/**
 * JavaFX App
 */
public class PointGeneration extends Application {

	final static double circle_size = 5;

	@Override
	public void start(@SuppressWarnings("exports") Stage stage) {

		Pane pain = new Pane();
		
		PoissonDisc pd = new PoissonDisc(640, 640, 50);								// Makes new PoissonDisc with size of 640x640 with an r-value of 50
		pd.setFirst();																// Sets the first point randomly
		pd.calcMissing();															// Distributes the rest of the points

		printPoints(pain, pd.getPoints());											// Prints the points
	}
	
	private void printPoints(Pane pain, ArrayList<PoissonReturnPoint> pts) {
		pain.getChildren().clear();
		for (int x = 0; x < pts.size(); x++) {
			Circle new_circle = new Circle();
			switch (pts.get(x).getMode()) {
			case 0:																	// Non-active points. They're just here. They don't do anything
				new_circle.setFill(Color.BLACK);
				break;
			case 1:																	// Active points. Generate new active points. If they can't generate more points (because of limited space) they become non-active
				new_circle.setFill(Color.RED);
				break;
			case 2:																	// Not important now
				new_circle.setFill(Color.PURPLE);
				break;
			default:																// Just in case something happens, that's not supposed to happen
				new_circle.setFill(Color.YELLOW);
				break;
			}
			new_circle.setRadius(circle_size);
			new_circle.setTranslateX(pts.get(x).getPos()[0]);
			new_circle.setTranslateY(pts.get(x).getPos()[1]);
			pain.getChildren().add(new_circle);
		}
	}

	public static void main(String[] args) {
		launch();
	}

}