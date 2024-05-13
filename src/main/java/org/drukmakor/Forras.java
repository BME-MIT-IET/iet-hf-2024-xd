package org.drukmakor;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * A Forrás a pálya egy eleme.
 * Azért felelős, hogy vizet készítsen, amit a csőhálózat elszállít.
 */
public class Forras extends Mezo {
	//private int maxJatekosok = Integer.MAX_VALUE;
	private static int MAXVIZ = 1;

	public Forras() {
		maxJatekosok = Integer.MAX_VALUE;
		view = new ForrasView();
	}

	/**
	 * Frissít függvény a víz folyásáért felelős
	 * Itt azt csinálja, hogy a Forrás termel vizet, majd az a hozzá kapcsolódó csőbe tölti
	 */
	@Override
	public void Frissit() throws Exception {
		ArrayList<Mezo> szomszedok = super.GetSzomszedok();		//szomszedok lekérdezése
		int szomszedszam = szomszedok.size();		//szomszédok száma

		if (szomszedszam == 1) 	//ha a szomszédok száma 1, akkor belepumpálja a vizet
			szomszedok.get(0).VizetNovel(MAXVIZ);
	}
}
