package projlab;

import java.util.ArrayList;
import java.util.Random;
/**
 * Ez az osztály a Mező megjeleítésért felelős, őt értesítik, ha egy mező állapota változik.
 * Ő felelős még azért is hogy milyen koordinátákra milyen alakzatot rajzoljunk.
 * Gyerekei a CiszternaView, CsőView, PumpaView és a ForrasView
 */

public abstract class MezoView extends ObjectView {
	/**
	 * Maximum hány próbálkozás lehetséges a véletlen elhelyezésre. Nagyobb szám
	 * esetén ha már nincs hely, akkor tovább keres. Találat esetén nem iterál
	 * végig.
	 */
	final int MAX_PLACEMENT_TRIALS = 5000;

	/**
	 * A pálya elemeinek helyét ezzel a függvénnyel tudják a leszármazottak
	 * legenerálni. Véletlenszerű helyet generál kicsit átfedési eséllyel.
	 */
	protected void GenerateXYPlacement(int minX, int maxX, int minY, int maxY, float minDistance) {
		Random random = new Random();
		ArrayList<ObjectView> views = ObjectView.GetAllViews();

		// Mező helyének legenerálása, minden ütközés esetén újragenerálással
		for (int i = 0; i < MAX_PLACEMENT_TRIALS; i++) {
			Boolean intersect = false;

			x = random.nextInt((maxX - minX) + 1) + minX;
			y = random.nextInt((maxY - minY) + 1) + minY;

			for (int j = 0; j < views.size(); j++) {
				ObjectView tested = views.get(j);
				if (tested == this || tested.lathato == false)
					continue;

				double d = tested.getDistanceFromPoint(x, y);

				if (d < minDistance) {
					intersect = true;
				}
			}

			if (!intersect)
				break;
		}
	}
}
