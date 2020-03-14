package br.com.svbgabriel.jelda.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;
import br.com.svbgabriel.jelda.world.Camera;
import br.com.svbgabriel.jelda.world.World;

public class Player extends Entity {

	public boolean right;
	public boolean up;
	public boolean left;
	public boolean down;

	public double speed = 1.4;

	private int frames = 0;
	private int maxFrames = 5;
	private int index = 0;
	private int maxIndex = 3;
	private int right_dir = 0;
	private int left_dir = 1;
	private int dir = right_dir;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;

	private BufferedImage playerDamage;

	public double life = 100;
	public double maxLife = 100;

	public int ammo = 0;

	public boolean isDamaged = false;
	private int damageFrames = 0;

	public boolean hasWeapon = false;
	public boolean shoot = false;
	public boolean mouseShoot = false;

	public int mx;
	public int my;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (x + speed), getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}

		if (up && World.isFree(getX(), (int) (y - speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(getX(), (int) (y + speed))) {
			moved = true;
			y += speed;
		}

		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}

		checkCollisionWithLifePack();
		checkCollisionWithAmmo();
		checkCollisionWithWeapon();

		if (isDamaged) {
			damageFrames++;
			if (damageFrames == 8) {
				damageFrames = 0;
				isDamaged = false;
			}
		}

		if (shoot) {
			shoot = false;
			if (hasWeapon && ammo > 0) {
				ammo--;
				int dx = 0;
				int px = 0;
				int py = 6;
				if (dir == right_dir) {
					px = 18;
					dx = 1;
				} else {
					px = -8;
					dx = -1;
				}

				Bullet bullet = new Bullet(getX() + px, getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}

		if (mouseShoot) {
			mouseShoot = false;
			if (hasWeapon && ammo > 0) {
				ammo--;
				int px = 0;
				int py = 8;
				double angle = 0;
				if (dir == right_dir) {
					px = 18;
					angle = Math.atan2(my - (getY() + py - Camera.y), mx - (getX() + px - Camera.x));
				} else {
					px = -8;
					angle = Math.atan2(my - (getY() + py - Camera.y), mx - (getX() + px - Camera.x));
				}

				double dx = Math.cos(angle);
				double dy = Math.sin(angle);

				Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}

		// Game over
		if (life <= 0) {
			life = 0;
			Game.gameState = "GAME_OVER";
		}

		updateCamera();
	}

	public void updateCamera() {
		// Calcula a movimentação para a Câmera
		Camera.x = Camera.clamp(getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void checkCollisionWithAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Ammo) {
				if (Entity.isColliding(this, e)) {
					ammo += 100;
					Game.entities.remove(e);
				}
			}
		}
	}

	public void checkCollisionWithLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof LifePack) {
				if (Entity.isColliding(this, e)) {
					life += 10;
					if (life > 100) {
						life = 100;
					}
					Game.entities.remove(e);
				}
			}
		}
	}

	public void checkCollisionWithWeapon() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Weapon) {
				if (Entity.isColliding(this, e)) {
					hasWeapon = true;
					Game.entities.remove(e);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], getX() - Camera.x, getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(WEAPON_RIGHT, getX() - Camera.x + 8, getY() - Camera.y, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], getX() - Camera.x, getY() - Camera.y, null);
				if (hasWeapon) {
					g.drawImage(WEAPON_LEFT, getX() - Camera.x - 8, getY() - Camera.y, null);
				}
			}
		} else {
			g.drawImage(playerDamage, getX() - Camera.x, getY() - Camera.y, null);
			if (hasWeapon) {
				if (dir == left_dir) {
					g.drawImage(Entity.GUN_DAMAGE_LEFT, getX() - Camera.x - 8, getY() - Camera.y, null);
				} else {
					g.drawImage(Entity.GUN_DAMAGE_RIGHT, getX() - Camera.x + 8, getY() - Camera.y, null);
				}
			}
		}
	}

}
