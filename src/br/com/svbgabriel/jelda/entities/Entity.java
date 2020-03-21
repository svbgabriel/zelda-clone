package br.com.svbgabriel.jelda.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.world.Camera;

public class Entity {

	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(112, 0, 16, 16);
	public static BufferedImage AMMO_EN = Game.spritesheet.getSprite(96, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(112, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
	public static BufferedImage WEAPON_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
	public static BufferedImage WEAPON_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_DAMAGE_LEFT = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage GUN_DAMAGE_RIGHT = Game.spritesheet.getSprite(0, 32, 16, 16);

	protected double x;
	protected double y;
	protected double z;
	protected int width;
	protected int height;

	// Máscara de colisão
	private int maskX;
	private int maskY;
	private int maskWidth;
	private int maskHeight;

	private BufferedImage sprite;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskX = 0;
		this.maskY = 0;
		this.maskWidth = width;
		this.maskHeight = height;
	}

	public void setMask(int maskX, int maskY, int maskWidth, int maskHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskWidth = maskWidth;
		this.maskHeight = maskHeight;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void tick() {

	}

	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskWidth, e1.maskHeight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskWidth, e2.maskHeight);

		if (e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		}

		return false;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, null);
	}

}
