package com.fabi.poisson_test;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
public class BoundryBoxContinousGeneration extends Application {

	final static double circle_size = 5;
	
	static int down = 0;
	static int up = 0;
	static int right = 0;
	static int left = 0;

	@Override
	public void start(Stage stage) {
		var javaVersion = SystemInfo.javaVersion();
		var javafxVersion = SystemInfo.javafxVersion();

		Pane pain = new Pane();
		
		RectBB rec = new RectBB();													// Create a new rectangular boundry box, centered, size 300x300 at position 300 300
		rec.setPosition(300, 300, 4);
		rec.setSize(300, 300);
		
		Rectangle bb_rec = new Rectangle();
		bb_rec.setFill(Color.TRANSPARENT);
		bb_rec.setStroke(Color.YELLOW);
		bb_rec.setStrokeWidth(2);
		bb_rec.setX(150);
		bb_rec.setY(150);
		bb_rec.setWidth(300);
		bb_rec.setHeight(300);
		bb_rec.toBack();
		
		PoissonDisc pd = new PoissonDisc(640, 640, 50);								// Makes new PoissonDisc with size of 640x640 with an r-value of 50
		pd.setBoundingBox(rec);
		pd.setFirst();																// Sets the first point randomly

		var scene = new Scene(pain, 640, 640);
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
		      if(key.getCode()==KeyCode.W || key.getCode()==KeyCode.UP) {
		    	  up = -1;
		      }else if(key.getCode()==KeyCode.A || key.getCode()==KeyCode.LEFT) {
		    	  left = -1;
		      }else if(key.getCode()==KeyCode.S || key.getCode()==KeyCode.DOWN) {
		    	  down = 1;
		      }else if(key.getCode()==KeyCode.D || key.getCode()==KeyCode.RIGHT) {
		    	  right = 1;
		      }
		});
		scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
		      if(key.getCode()==KeyCode.W || key.getCode()==KeyCode.UP) {
		    	  up = 0;
		      }else if(key.getCode()==KeyCode.A || key.getCode()==KeyCode.LEFT) {
		    	  left = 0;
		      }else if(key.getCode()==KeyCode.S || key.getCode()==KeyCode.DOWN) {
		    	  down = 0;
		      }else if(key.getCode()==KeyCode.D || key.getCode()==KeyCode.RIGHT) {
		    	  right = 0;
		      }
		});
		
		stage.setScene(scene);
		stage.show();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					pd.updateGrid(right + left, down + up);							// Moves the whole grid about a certain vector
					pd.calcMissing();												// Distributes points
					printPoints(pain, pd.getPoints());
					pain.getChildren().add(bb_rec);
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