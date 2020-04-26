package br.com.svbgabriel.jelda.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.world.Camera;
import br.com.svbgabriel.jelda.world.Node;
import br.com.svbgabriel.jelda.world.Vector2i;

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

	public int depth;

	protected List<Node> path;

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

	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		@Override
		public int compare(Entity n0, Entity n1) {
			if (n1.depth < n0.depth) {
				return +1;
			}
			if (n1.depth > n0.depth) {
				return -1;
			}
			return 0;
		}
	};

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

	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public void followPath(List<Node> path) {
		if (path != null) {
			if (path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				if (x < target.x * 16) {
					x++;
				} else if (x > target.x * 16) {
					x--;
				}

				if (y < target.y * 16) {
					y++;
				} else if (y > target.y * 16) {
					y--;
				}

				if (x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() - 1);
				}

			}
		}
	}

	public boolean isColliding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskWidth, maskHeight);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskWidth, maskHeight);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
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
