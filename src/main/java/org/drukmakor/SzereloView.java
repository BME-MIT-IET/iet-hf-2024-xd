package org.drukmakor;

import java.awt.Color;
/**
 * Ez az osztály a Szerelők megjeleítésért felelős, őt értesítik, ha egy szerelő állapota változik.
 * Ő felelős még azért is hogy milyen koordinátákra milyen alakzatot rajzoljunk.
 */
public class SzereloView extends JatekosView {
	public SzereloView() {
		szin = new Color(126, 198, 54); // Zöld: #7ec636
		nevSzin = Color.ORANGE;
	}
}
