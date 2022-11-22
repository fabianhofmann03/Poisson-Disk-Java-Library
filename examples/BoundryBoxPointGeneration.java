package com.fabi.poisson_test;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import poisson_disk.PoissonDisc;
import poisson_disk.PoissonReturnPoint;
import poisson_disk.RectBB;

/**
 * JavaFX App
 */
public class BoundryBoxPointGeneration extends Application {

	final static double circle_size = 5;
	
	int up = 0;
	int left = 0;

	@Override
	public void start(@SuppressWarnings("exports") Stage stage) {

		Pane pain = new Pane();
		
		RectBB rec = new RectBB();													// Create a new rectangular boundry box, centered, size 300x300 at position 300 300
		rec.setPosition(300, 300, 4);
		rec.setSize(300, 300);
		
		Rectangle bb_rec = new Rectangle();
		bb_rec.setFill(Color.TRANSPARENT);
		bb_rec.setStroke(Color.GREEN);
		bb_rec.setStrokeWidth(2);
		bb_rec.setX(150);
		bb_rec.setY(150);
		bb_rec.setWidth(300);
		bb_rec.setHeight(300);
		
		PoissonDisc pd = new PoissonDisc(640, 640, 50);								// Makes new PoissonDisc with size of 640x640 with an r-value of 50
		pd.setBoundingBox(rec);
		pd.setFirst();																// Sets the first point randomly
		pd.calcMissing();															// Distributes the rest of the points
		
		printPoints(pain, pd.getPoints());
		bb_rec.toBack();
		pain.getChildren().add(bb_rec);

		var scene = new Scene(pain, 640, 640);
		
		stage.setScene(scene);
		stage.show();
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
			case 2:																	// Any point outside the boundry becomes half-active. They don't generate new points, but if they come back into the boundry, they become new active points.
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