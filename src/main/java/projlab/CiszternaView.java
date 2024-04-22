package projlab;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Ez az osztály a Ciszternák megjeleítésért felelős, őt értesítik, ha egy ciszterna állapota változik.
 * O felelős még azért is hogy milyen koordinátákra milyen alakzatot rajzoljunk.
 */
public class CiszternaView extends MezoView {
	private static final Color szin = new Color(245, 154, 35); // Narancs: #f59a23
	private static final Color termeltPumpaSzin = new Color(113, 113, 113); // Szürke: #717171
	private static final int termeltEltolas = 50; // Termelt elemek eltolása

	private int pumpak = 0; // Csiszternánál található termelt pumpák
	private int csovek = 0; // Csiszternánál található termelt csovek
	double startDeg = 0;

	public CiszternaView() {
		int MIN_DISTANCE_BETWEEN = oldalmeret + 100;

		// Ciszterna a pálya utolsó negyedén lehet
		int minX = CANVAS_WIDTH / 4 * 3;
		int maxX = CANVAS_WIDTH - oldalmeret;
		int minY = oldalmeret;
		int maxY = CANVAS_HEIGHT - oldalmeret;

		GenerateXYPlacement(minX, maxX, minY, maxY, MIN_DISTANCE_BETWEEN);
	}

	/**
	 * Ciszterna nézetének frissítése
	 */
	@Override
	public void Notify(Mezo m) {
		pumpak = m.getTermeltPumpak().size();
		if (pumpak > 8)
			pumpak = 8;
		// csovek = m.get???
	}

	@Override
	public void Animate() {
		startDeg += 0.01;
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

		// Négyzet kirajzolása (xy a közepe)
		g.setColor(szin);
		g.fillRect(x - oldalmeret / 2, y - oldalmeret / 2, oldalmeret, oldalmeret);

		// Termelt pumpák megjelenítése
		g.setColor(termeltPumpaSzin);
		int pumpaMeret = 15;
		for (int i = 0; i < pumpak; i++) {
			double deg = startDeg + i * (Math.PI * 2 / pumpak);
			int pX = x + (int) (termeltEltolas * Math.cos(deg));
			int pY = y + (int) (termeltEltolas * Math.sin(deg));
			g.fillOval(pX - pumpaMeret / 2, pY - pumpaMeret / 2, pumpaMeret, pumpaMeret);
		}

		// Név a négyzet közepén
		DrawName(g, x, y);
	}

}
