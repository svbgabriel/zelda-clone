package br.com.svbgabriel.jelda.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.world.Camera;

public class Bullet extends Entity {

	private double dx;
	private double dy;
	private double speed = 4;
	private int life = 30;
	private int currentLife = 0;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x += dx * speed;
		y += dy * speed;
		currentLife++;
		if (currentLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.drawOval(getX() - Camera.x, getY() - Camera.y, width, height);
	}

}
