package br.com.svbgabriel.jelda.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.com.svbgabriel.jelda.entities.Ammo;
import br.com.svbgabriel.jelda.entities.Enemy;
import br.com.svbgabriel.jelda.entities.Entity;
import br.com.svbgabriel.jelda.entities.LifePack;
import br.com.svbgabriel.jelda.entities.Player;
import br.com.svbgabriel.jelda.entities.Weapon;
import br.com.svbgabriel.jelda.graphics.Spritesheet;
import br.com.svbgabriel.jelda.main.Game;

public class World {

	private static Tile[] tiles;
	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int currentPixel = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (currentPixel == 0xFF000000) {
						// Floor/Chão
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (currentPixel == 0xFFFFFFFF) {
						// Parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if (currentPixel == 0xFF1F16EC) {
						// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (currentPixel == 0xFFFF0000) {
						// Enemy
						Enemy enemy = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(enemy);
						Game.enemies.add(enemy);
					} else if (currentPixel == 0xFFDC7F2C) {
						// Weapon
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
					} else if (currentPixel == 0xFFD36E6E) {
						// Life Pack
						LifePack pack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);
						Game.entities.add(pack);
					} else if (currentPixel == 0xFFECE416) {
						// Ammo
						Game.entities.add(new Ammo(xx * 16, yy * 16, 16, 16, Entity.AMMO_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Verifica se o tile para onde está se movendo esta livre
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;

		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;

		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * WIDTH)] instanceof WallTile) || (tiles[x2 + (y2 * WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * WIDTH)] instanceof WallTile) || (tiles[x4 + (y4 * WIDTH)] instanceof WallTile));
	}

	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}

	public void render(Graphics g) {
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;

		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);

		for (int xx = xStart; xx <= xFinal; xx++) {
			for (int yy = yStart; yy <= yFinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
