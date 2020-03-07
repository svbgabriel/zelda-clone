package br.com.svbgabriel.jelda.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import br.com.svbgabriel.jelda.entities.Player;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(8, 4, 70, 8);
		g.setColor(Color.GREEN);
		g.fillRect(8, 4, (int) ((Player.life / Player.maxLife) * 70), 8);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int) Player.life + "/" + (int) Player.maxLife, 30, 11);
	}
}
