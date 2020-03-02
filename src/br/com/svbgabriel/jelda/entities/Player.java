package br.com.svbgabriel.jelda.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity {

	public boolean right;
	public boolean up;
	public boolean left;
	public boolean down;

	public double speed = 1.4;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		if (right) {
			x += speed;
		} else if (left) {
			x -= speed;
		}

		if (up) {
			y -= speed;
		} else if (down) {
			y += speed;
		}
	}

}
