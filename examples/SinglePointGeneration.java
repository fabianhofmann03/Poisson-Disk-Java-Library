package com.fabi.poisson_test;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
public class SinglePointGeneration extends Application {

	final static double circle_size = 5;

	@Override
	public void start(Stage stage) {
		var javaVersion = SystemInfo.javaVersion();
		var javafxVersion = SystemInfo.javafxVersion();

		Pane pain = new Pane();
		
		PoissonDisc pd = new PoissonDisc(640, 640, 50);								// Makes new PoissonDisc with size of 640x640 with an r-value of 50
		pd.setFirst();																// Sets the first point randomly

		var scene = new Scene(pain, 640, 640);
		stage.setScene(scene);
		stage.show();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					pd.calcMissingOnce();											// Distributes one point at a time
					printPoints(pain, pd.getPoints());
				});
			}
		}, 0, 10);
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