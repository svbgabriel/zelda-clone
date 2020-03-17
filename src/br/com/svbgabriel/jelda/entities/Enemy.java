package br.com.svbgabriel.jelda.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.main.Sound;
import br.com.svbgabriel.jelda.world.Camera;
import br.com.svbgabriel.jelda.world.World;

public class Enemy extends Entity {

	private int frames = 0;
	private int maxFrames = 20;
	private int index = 0;
	private int maxIndex = 1;
	private double speed = 0.4;
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
		} else {
			// Inimigo está colidindo com o Player
			if (Game.rand.nextInt(100) < 10) {
				Sound.hurtEffect.play();
				Game.player.life--;
				Game.player.isDamaged = true;
			}
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

}
