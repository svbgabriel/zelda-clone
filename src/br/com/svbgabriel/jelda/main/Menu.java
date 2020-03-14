package br.com.svbgabriel.jelda.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {

	public String options[] = { "Novo jogo", "Carregar jogo", "Sair" };
	public int currentOption = 0;
	public int maxOptions = options.length - 1;
	public boolean up;
	public boolean down;
	public boolean enter;
	public boolean pause = false;

	public void tick() {
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOptions;
			}
		}
		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOptions) {
				currentOption = 0;
			}
		}
		if (enter) {
			enter = false;
			if (options[currentOption].equals("Novo jogo") || options[currentOption].equals("Continuar")) {
				Game.gameState = "NORMAL";
				pause = false;
			}
			if (options[currentOption].equals("Sair")) {
				System.exit(0);
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.RED);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("Tale of Jelda", (Game.WIDTH * Game.SCALE) / 2 - 110, (Game.HEIGHT * Game.SCALE) / 2 - 160);

		// Opções de menu
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 24));
		if (!pause) {
			g.drawString("Novo jogo", (Game.WIDTH * Game.SCALE) / 2 - 50, 160);
		} else {
			g.drawString("Resumir", (Game.WIDTH * Game.SCALE) / 2 - 40, 160);
		}
		g.drawString("Carregar jogo", (Game.WIDTH * Game.SCALE) / 2 - 70, 200);
		g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 10, 240);

		if (options[currentOption].equals("Novo jogo")) {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 90, 160);
		} else if (options[currentOption].equals("Carregar jogo")) {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 90, 200);
		} else if (options[currentOption].equals("Sair")) {
			g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 40, 240);
		}
	}
}
