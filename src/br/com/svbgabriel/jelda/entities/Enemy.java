package br.com.svbgabriel.jelda.entities;

import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.world.World;

public class Enemy extends Entity {

	private double speed = 1;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), getY())) {
			x += speed;
		} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), getY())) {
			x -= speed;
		}
		
		if ((int) y < Game.player.getY() && World.isFree(getX(), (int) (y + speed))) {
			y += speed;
		} else if ((int) y > Game.player.getY() && World.isFree(getX(), (int) (y - speed))) {
			y -= speed;
		}
	}

}
