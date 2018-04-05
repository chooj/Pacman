package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;


public class Main extends Application {
	
	public static Scene scene;
	public static Pane pane;
	public static String command;
	public static Sphere pac;
	public static double pxs, pys;
	public static Wall[] walls;
	public static String[] hCodes = {"0-600","290-310","290-310","50-130,170-250,350-430,470-550",
			"50-130,170-250,350-430,470-550","","","50-130,170-190,230-370,410-430,470-550",
			"170-190,290-310,410-430","170-190,290-310,410-430","0-130,170-250,290-310,350-430,470-600",
			"110-130,170-190,410-430,470-490","110-130,170-190,410-430,470-490",
			"0-130,170-190,230-270,330-370,410-430,470-600", "230-250,350-370",
			"230-250,350-370","0-130,170-190,230-370,410-430,470-600","110-130,170-190,410-430,470-490",
			"110-130,170-190,410-430,470-490","0-130,170-190,230-370,410-430,470-600","290-310","290-310",
			"50-130,170-250,290-310,350-430,470-550","110-130,470-490","110-130,470-490",
			"110-130,170-190,230-370,410-430,470-490","170-190,290-310,410-430",
			"170-190,290-310,410-430","50-250,290-310,350-550","","","0-600"};
	public static String[] vCodes = {"0-210,390-620","0-210,390-620"};
	
	@Override
	public void start(Stage stage) {
		
		pane = new Pane();
		scene = new Scene(pane, 600, 620, Color.BLACK);
		stage.setScene(scene);
		stage.show();
		//
		initPac();
		initWalls();
		
		runAnimation();
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				System.out.println(t.getSceneX() + ", " + t.getSceneY());
			}
		});
	}
	
	public void initPac() {
		command = "";
		//
		pac = new Sphere(15);
		pac.setTranslateX(300);
		pac.setTranslateY(290);
		PhongMaterial pm = new PhongMaterial();
		pm.setDiffuseColor(Color.YELLOW);
		pac.setMaterial(pm);
		//
		pane.getChildren().add(pac);
	}
	
	public void initWalls() {
		walls = new Wall[hCodes.length + vCodes.length];
		for (int i = 0; i < walls.length; i++) {
			if (i < hCodes.length) {
				if (!hCodes[i].equals("")) {
					walls[i] = new Wall(hCodes[i], i*20, "horizontal");
				}
			} else {
				if (!vCodes[i-hCodes.length].equals("")) {
					walls[i] = new Wall(vCodes[i-hCodes.length], (i-hCodes.length)*600, "vertical");
				}
			}
			if (walls[i] != null) {
				pane.getChildren().add(walls[i].components());
				for (Rectangle r : walls[i].shadows()) {
					pane.getChildren().add(r);
				}
			}
		}
	}
	
	/**
	 * FIX ME PLS
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean spaceOpen(double x, double y) {
		boolean b = true;
		for (Wall w : walls) {
			if (w != null) {
				if (w.shadows()[0].getY() + w.shadows()[0].getHeight()/2 == y ||
						w.shadows()[0].getX() + w.shadows()[0].getWidth()/2 == x) {
					for (Rectangle rect : w.shadows()) {
						if (x > rect.getX() && x < rect.getX() + rect.getWidth() &&
								y > rect.getY() && y < rect.getY() + rect.getHeight()) {
							b = false;
						}
					}
				}
			}
		}
		return b;
	}

	/**
	 * DOUBLE CHECK MOTION
	 */
	public static void runPac() {
		if ((pxs > 0 && pac.getTranslateX() > 610 + pac.getRadius()) || 
				(pxs < 0 && pac.getTranslateX() < -10 - pac.getRadius())) {
			pac.setTranslateX(pac.getTranslateX() + (620 + pac.getRadius())*(-Math.abs(pxs)/pxs));
		}
		//
		boolean canMove = true;
		// runs into something
		if (pxs > 0) {
			canMove = spaceOpen(pac.getTranslateX() + pac.getRadius() + pxs, pac.getTranslateY());
		} else if (pxs < 0) {
			canMove = spaceOpen(pac.getTranslateX() - pac.getRadius() + pxs, pac.getTranslateY());
		} else if (pys > 0) {
			canMove = spaceOpen(pac.getTranslateX(), pac.getTranslateY() + pac.getRadius() + pys);
		} else if (pys < 0) {
			canMove = spaceOpen(pac.getTranslateX(), pac.getTranslateY() - pac.getRadius() + pys);
		}
		// switch direction
		if (!command.equals("")) {
			switch (command) {
				case "LEFT": 
					if (spaceOpen(pac.getTranslateX() - 20, pac.getTranslateY())) {
						pxs = -0.15;
						pys = 0;
						command = "";
						canMove = true;
					}
					break;
				case "RIGHT": 
					if (spaceOpen(pac.getTranslateX() + 20, pac.getTranslateY())) {
						pxs = 0.15;
						pys = 0;
						command = "";
						canMove = true;
					}
					break;
				case "UP":
					if (spaceOpen(pac.getTranslateX(), pac.getTranslateY() - 20)) {
						pxs = 0;
						pys = -0.15;
						command = "";
						canMove = true;
					}
					break;
				case "DOWN": 
					if (spaceOpen(pac.getTranslateX(), pac.getTranslateY() + 20)) {
						pxs = 0;
						pys = 0.15;
						command = "";
						canMove = true;
					}
					break;
				default: break;
			}
		}
		//
		if (canMove) {
			pac.setTranslateX(pac.getTranslateX() + pxs);
			pac.setTranslateY(pac.getTranslateY() + pys);
		}
	}
	
	public static void draw() {
		updateKey();
		runPac();
	}
	
	public static void runAnimation() {
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				draw();
			}
		};
		Timeline timeline = new Timeline(new KeyFrame(Duration.ONE, eh));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void updateKey() {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode code = t.getCode();
				int value = t.getCode().getCode();
				if (value >= 37 && value <= 40) {
					command = code.toString();
				}
			}
		});
	}
}
