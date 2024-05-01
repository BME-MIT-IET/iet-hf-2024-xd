package org.drukmakor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
/**
 * Ez az osztály a Játékosok megjeleítésért felelős, őt értesítik, ha egy játékos állapota változik.
 * Ő felelős még azért is hogy milyen koordinátákra milyen alakzatot rajzoljunk.
 * Gyerekei a SzabotorView és SzereloView
 */

public abstract class JatekosView extends ObjectView {
	/**
	 * Ha több játékos van a mezőn akkor mennyire legyenek eltolva a középponttól.
	 */
	private static final int KOZEPPONTTOL_ELTOLAS = 30;

	/**
	 * Játékos állapotát tároló változók.
	 */
	protected Color szin; // Játékos színe
	private Mezo helyzet; // Játékos előző helye
	private Jatekos jatekos;
	private int drawX = 0, drawY = 0; // Játékos rajzolásának helye (animált)

	public JatekosView() {
		oldalmeret = 0;
	}

	@Override
	public void Notify(Jatekos j) {
		Mezo ujHelyzet = j.getHelyzet();

		// Mát ottlévő játékosok frissítése
		if (ujHelyzet != helyzet) {
			helyzet = ujHelyzet;
			for (Jatekos jSzomszed : helyzet.getJatekosok()) {
				jSzomszed.getView().Notify(jSzomszed);
			}
		}

		// Saját pozició frissítése
		ObjectView helyzetView = helyzet.getView();
		int hanyadikJatekos = helyzet.getJatekosok().indexOf(j);
		int osszesJatekos = helyzet.getJatekosok().size();

		double deg = hanyadikJatekos * (Math.PI * 2 / osszesJatekos);
		int dX = (int) (KOZEPPONTTOL_ELTOLAS * Math.cos(deg));
		int dY = (int) (KOZEPPONTTOL_ELTOLAS * Math.sin(deg));
		x = helyzetView.getKozepX() + dX;
		y = helyzetView.getKozepY() + dY;
	}

	/**
	 * Animációhoz segéd függvény
	 */
	private static final int animationSpeed = 5;
	public static boolean haveSameSign(int num1, int num2) {
		return (num1 >= 0 && num2 >= 0) || (num1 < 0 && num2 < 0);
	}

	@Override
	public void Animate() {
		if (drawX != x || drawY != y) {
			if (drawX == 0 && drawY == 0) {
				drawX = x;
				drawY = y;
			}

			int dx = x - drawX;
			int dy = y - drawY;

			double d = Math.sqrt(dx * dx + dy * dy);
			double ratio = animationSpeed / d;

			drawX += (int) (dx * ratio);
			drawY += (int) (dy * ratio);

			if (!haveSameSign(dx, x - drawX))
				drawX = x;
			if (!haveSameSign(dy, y - drawY))
				drawY = y;
		}
	}

	@Override
	public void Draw(ArrayList<Graphics> layers) {
		// Ha nem látható a helyzet vagy a szerelő, nem történik kirajzolás
		if (jatekos != null) {
			Boolean helyzetLathato = jatekos.getHelyzet().getView().lathato;
			if (!helyzetLathato) {
				return;
			}
		}

		Graphics g = layers.get(2);

		// Háromszög
		int haromszogX = drawX;
		int haromszogY = drawY - 10;
		g.setColor(szin);
		g.fillPolygon(new int[] { haromszogX - 15, haromszogX, haromszogX + 15 },
				new int[] { haromszogY, haromszogY + 25, haromszogY }, 3);

		// Név a háromszög közepére
		DrawName(g, drawX, drawY);
	}
}
