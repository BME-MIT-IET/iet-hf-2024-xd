package org.drukmakor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Ez az osztály a Pumpák megjeleítésért felelős, őt értesítik, ha egy pumpa állapota változik.
 * Ő felelős még azért is hogy milyen koordinátákra milyen alakzatot rajzoljunk.
 */

public class PumpaView extends MezoView {
	private static final Color szin = new Color(113, 113, 113); // Szürke: #717171
	private static final Color vizSzin = new Color(99, 168, 252); // Kék

	private Boolean mukodik = true;
	// Be és kimenet helye
	private int beX = -1, beY = -1;
	private int kiX = -1, kiY = -1;

	public PumpaView() {
		oldalmeret = 55;

		int MIN_DISTANCE_BETWEEN = oldalmeret + 100;

		// Pumpa a pálya középső részén lehet
		int minX = CANVAS_WIDTH / 4 + oldalmeret;
		int maxX = CANVAS_WIDTH / 4 * 3 - oldalmeret;
		int minY = oldalmeret + 20;
		int maxY = CANVAS_HEIGHT - oldalmeret;

		GenerateXYPlacement(minX, maxX, minY, maxY, MIN_DISTANCE_BETWEEN);
	}

	/**
	 * Amikor a pumpa változott, akkor átadja magát a hozzá tartozó view-nak, ami ez
	 * alapján tudja frissíteni a grafikáját. Változhat például hogy működik-e vagy
	 * sem, és az, hogy van-e benne víz vagy sem.
	 */
	@Override
	public void Notify(Mezo m) {
		mukodik = m.getMukodik();
		nevSzin = m.getVizmennyiseg() == 0 ? Color.WHITE : vizSzin;

		// Be és kimenet helye
		Mezo be = m.getBemenet();
		Mezo ki = m.getKimenet();

		int jelzesEltolas = 20;
		if (be != null) {
			ObjectView beView = be.getView();
			double dx = beView.getKozepX() - x;
			double dy = beView.getKozepY() - y;
			double d = Math.sqrt(dx * dx + dy * dy);
			beX = (int) (x + dx * jelzesEltolas / d);
			beY = (int) (y + dy * jelzesEltolas / d);
		} else {
			beX = beY = -1;
		}

		if (ki != null) {
			ObjectView kiView = ki.getView();
			double dx = kiView.getKozepX() - x;
			double dy = kiView.getKozepY() - y;
			double d = Math.sqrt(dx * dx + dy * dy);
			kiX = (int) (x + dx * jelzesEltolas / d);
			kiY = (int) (y + dy * jelzesEltolas / d);
		} else {
			kiX = kiY = -1;
		}
	}

	private float animValue = 0;

	@Override
	public void Animate() {
		if (!mukodik)
			animValue += 0.1;
	}

	/**
	 * A viewhez tartozó grafika rárajzolása az adott bufferre.
	 * @param layers - A bufferek amikre rajzolni kell.
	 */
	@Override
	public void Draw(ArrayList<Graphics> layers) {
		if (!lathato) {
			return;
		}

		Graphics g = layers.get(1);

		// Kör kirajzolása (xy a közepe)
		g.setColor(szin);
		g.fillOval(x - oldalmeret / 2, y - oldalmeret / 2, oldalmeret, oldalmeret);

		// Ki és bemenet jelzése
		int kibeJelzesMeret = 5;
		if (beX != -1) {
			g.setColor(Color.GREEN);
			g.fillOval(beX - kibeJelzesMeret / 2, beY - kibeJelzesMeret / 2, kibeJelzesMeret, kibeJelzesMeret);
		}
		if (kiX != -1) {
			g.setColor(Color.RED);
			g.fillOval(kiX - kibeJelzesMeret / 2, kiY - kibeJelzesMeret / 2, kibeJelzesMeret, kibeJelzesMeret);
		}

		// Hibajelzés
		if (!mukodik) {
			int warnX = x;
			int warnY = y - oldalmeret / 2 - 5 - (int) (Math.sin(animValue) * 5);

			g.setColor(Color.RED);
			g.fillPolygon(new int[] { warnX - 9, warnX, warnX + 9 }, new int[] { warnY, warnY - 17, warnY }, 3);
			g.setColor(Color.WHITE);
			g.drawString("!", warnX - 2, warnY - 2);
		}

		// Név a kör közepén
		DrawName(g, x, y);
	}

}
