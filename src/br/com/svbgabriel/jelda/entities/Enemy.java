package br.com.svbgabriel.jelda.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.main.Sound;
import br.com.svbgabriel.jelda.world.AStar;
import br.com.svbgabriel.jelda.world.Camera;
import br.com.svbgabriel.jelda.world.Vector2i;

public class Enemy extends Entity {

	private int frames = 0;
	private int maxFrames = 20;
	private int index = 0;
	private int maxIndex = 1;
	private int life = 10;

	// Máscara de colisão
	private int maskX = 8;
	private int maskY = 8;
	private int maskWidth = 10;
	private int maskHeight = 10;

	private BufferedImage[] sprites;

	private boolean isDamaged = false;
	private int damagedFrames = 10;
	private int damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}

	public void tick() {
		if (!isCollidingWithPlayer()) {
			if (path == null || path.size() == 0) {
				Vector2i start = new Vector2i((int) (x / 16), (int) (y / 16));
				Vector2i end = new Vector2i((int) (Game.player.x / 16), (int) (Game.player.y / 16));
				path = AStar.findPath(Game.world, start, end);
			}
		} else {
			if (Game.rand.nextInt(100) < 5) {
				Sound.hurtEffect.play();
				Game.player.life -= Game.rand.nextInt(3);
				Game.player.isDamaged = true;
			}
		}

		if (Game.rand.nextInt(100) < 90) {
			followPath(path);
		}
		if (Game.rand.nextInt(100) < 5) {
			Vector2i start = new Vector2i((int) (x / 16), (int) (y / 16));
			Vector2i end = new Vector2i((int) (Game.player.x / 16), (int) (Game.player.y / 16));
			path = AStar.findPath(Game.world, start, end);
		}

		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}

		checkCollisionWithBullet();

		if (life <= 0) {
			destroySelf();
			return;
		}

		if (isDamaged) {
			damageCurrent++;
			if (damageCurrent == damagedFrames) {
				damageCurrent = 0;
				isDamaged = false;
			}
		}
	}

	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			g.drawImage(sprites[index], getX() - Camera.x, getY() - Camera.y, null);
		} else {
			g.drawImage(Entity.ENEMY_FEEDBACK, getX() - Camera.x, getY() - Camera.y, null);
		}

	}

	public void checkCollisionWithBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet bullet = Game.bullets.get(i);
			if (Entity.isColliding(this, bullet)) {
				life--;
				isDamaged = true;
				Game.bullets.remove(bullet);
				return;
			}
		}
	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemy = new Rectangle(getX() + maskX, getY() + maskY, maskWidth, maskHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemy.intersects(player);
	}

}
