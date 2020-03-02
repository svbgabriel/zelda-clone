package br.com.svbgabriel.jelda.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.svbgabriel.jelda.main.Game;

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

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}

	public void tick() {
		moved = false;
		if (right) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}

		if (up) {
			moved = true;
			y -= speed;
		} else if (down) {
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
	}

	public void render(Graphics g) {
		if (dir == right_dir) {
			g.drawImage(rightPlayer[index], getX(), getY(), null);
		} else if (dir == left_dir) {
			g.drawImage(leftPlayer[index], getX(), getY(), null);
		}
	}

}