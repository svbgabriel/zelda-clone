package br.com.svbgabriel.jelda.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.world.Camera;
import br.com.svbgabriel.jelda.world.World;

public class Enemy extends Entity {

	private double speed = 1;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	public void tick() {
		if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), getY())
				&& !isColliding((int) (x + speed), getY())) {
			x += speed;
		} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), getY())
				&& !isColliding((int) (x - speed), getY())) {
			x -= speed;
		}

		if ((int) y < Game.player.getY() && World.isFree(getX(), (int) (y + speed))
				&& !isColliding(getX(), (int) (y + speed))) {
			y += speed;
		} else if ((int) y > Game.player.getY() && World.isFree(getX(), (int) (y - speed))
				&& !isColliding(getX(), (int) (y - speed))) {
			y -= speed;
		}
	}

	public void render(Graphics g) {
		super.render(g);
	}

	public boolean isColliding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);
		for (Enemy e : Game.enemies) {
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}

}
