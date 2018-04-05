package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;

public class Wall {
	
	private Box[] boxes;
	private Rectangle[] rects;
	
	public Wall(String specs, int y, String ortn) {
		String[] bigBits = specs.split(",");
		boxes = new Box[bigBits.length];
		rects = new Rectangle[bigBits.length];
		for (int i = 0; i < bigBits.length; i++) {
			String[] lilBits = bigBits[i].split("-");
			Box b;
			if (ortn.equals("horizontal")) {
				double l = Integer.parseInt(lilBits[0]), r = Integer.parseInt(lilBits[1]);
				b = new Box(r - l, 20, 40);
				b.setTranslateX(l + b.getWidth()/2);
				b.setTranslateY(y);
				rects[i] = new Rectangle(l, y - 10, r - l, 20);
				rects[i].setFill(Color.TRANSPARENT);
			} else {
				double t = Integer.parseInt(lilBits[0]), bo = Integer.parseInt(lilBits[1]);
				b = new Box(20, bo - t, 40);
				b.setTranslateX(y);
				b.setTranslateY(t + b.getHeight()/2);
				rects[i] = new Rectangle(y - 10, t, 20, t - bo);
				rects[i].setFill(Color.TRANSPARENT);
			}
			PhongMaterial bm = new PhongMaterial();
			bm.setDiffuseColor(Color.NAVY);
			b.setMaterial(bm);
			boxes[i] = b;
		}
	}
	
	public Group components() {
		Group g = new Group();
		for (Box b : boxes) {
			g.getChildren().add(b);
		}
		return g;
	}
	
	public Rectangle[] shadows() {
		return rects;
	}
}
