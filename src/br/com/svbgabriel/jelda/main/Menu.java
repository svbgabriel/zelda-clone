package br.com.svbgabriel.jelda.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.com.svbgabriel.jelda.world.World;

public class Menu {

	public String options[] = { "Novo jogo", "Carregar jogo", "Sair" };
	public int currentOption = 0;
	public int maxOptions = options.length - 1;
	public boolean up;
	public boolean down;
	public boolean enter;
	public static boolean pause = false;
	public static boolean saveExists = false;
	public static boolean saveGame = false;

	public void tick() {
		File file = new File("save.rxt");
		if (file.exists()) {
			saveExists = true;
		} else {
			saveExists = false;
		}
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
				file = new File("save.txt");
				file.delete();
			} else if (options[currentOption].equals("Carregar jogo")) {
				file = new File("save.txt");
				if (file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			} else if (options[currentOption].equals("Sair")) {
				System.exit(0);
			}
		}
	}

	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch (spl2[0]) {
			case "level":
				World.restartGame("level" + spl2[1] + ".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			case "life":
				Game.player.life = Double.parseDouble(spl2[1]);
				break;
			default:
				break;
			}
		}
	}

	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if (file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while ((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for (int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				} catch (IOException e) {
				}
			} catch (FileNotFoundException e) {
			}
		}

		return line;
	}

	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for (int j = 0; j < value.length; j++) {
				value[j] += encode;
				current += value[j];
			}
			try {
				write.write(current);
				if (i < val1.length - 1) {
					write.newLine();
				}
			} catch (IOException e) {
			}
		}
		try {
			write.flush();
			write.close();
		} catch (Exception e) {
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
