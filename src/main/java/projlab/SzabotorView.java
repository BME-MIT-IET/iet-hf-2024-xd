package projlab;

import java.awt.Color;

/**
 * Ez az osztály a Szabotőrök megjeleítésért felelős, őt értesítik, ha egy szabotőr állapota változik.
 * Ő felelős még azért is hogy milyen koordinátákra milyen alakzatot rajzoljunk.
 */
public class SzabotorView extends JatekosView {
	public SzabotorView() {
		szin = Color.ORANGE;
		nevSzin = new Color(255, 100, 100);
	}
}
