package org.drukmakor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
/**
 * Ez az osztály a Források megjeleítésért felelős, őt értesítik, ha egy forrás állapota változik.
 * Ő felelős még azért is hogy milyen koordinátákra milyen alakzatot rajzoljunk.
 */

public class ForrasView extends MezoView {
	private static final Color szin = new Color(0, 207, 255); // Kék: #00cfff

	public ForrasView() {
		int MIN_DISTANCE_BETWEEN = oldalmeret + 100;

		// Forrás a pálya első negyedén lehet
		int minX = oldalmeret;
		int maxX = CANVAS_WIDTH / 4;
		int minY = oldalmeret;
		int maxY = CANVAS_HEIGHT - oldalmeret;

		GenerateXYPlacement(minX, maxX, minY, maxY, MIN_DISTANCE_BETWEEN);
	}

	/**
	 * Mivel a forrás állapota egyelőre sehogyan sem változhat grafikusan (a helye
	 * sem), ezért egyelőre üres
	 */
	@Override
	public void Notify(Mezo m) {
	}

	/**
	 * A forrás nézetének rárajzolása az átadott graphics bufferre.
	 * @param layers - A graphics buffer amire rajzol.
	 */
	@Override
	public void Draw(ArrayList<Graphics> layers) {
		if (!lathato) {
			return;
		}

		Graphics g = layers.get(1);

		// Négyzet kirajzolása (xy a közepe)
		g.setColor(szin);
		g.fillRect(x - oldalmeret / 2, y - oldalmeret / 2, oldalmeret, oldalmeret);

		// Név a négyzet közepén
		DrawName(g, x, y);
	}
}
