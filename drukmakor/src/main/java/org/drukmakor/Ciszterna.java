package org.drukmakor;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import static org.drukmakor.Main.pontsz;

/**
 * A Forrás a pálya aktív eleme. Azért felelős, hogy vizet nyeljen el, amit a
 * csőhálózaton keresztűl kap. Továbbá Pumpákat és Csöveket termel. Összesen
 * maximum 20 hozzácsatlakozott csöve lehet egy csiszternának és bármennyi pumpát termelhet.
 */
public class Ciszterna extends Mezo {

	private ArrayList<Mezo> termeltpumpak;

	public Ciszterna() {
		super(Integer.MAX_VALUE);
		view = new CiszternaView();
		termeltpumpak = new ArrayList<Mezo>();
		maxSzomszedok = 20;
	}

	/**
	 * Ha a termeltpumpák elemszáma 0, akkor nincs felvehető pumpa.
	 * Ha a termeltpumpák elemszáma nagyobb mint 0, akkor az termeltpumpak listaban
	 * az utolso pumpa eltávolításra kerül.
	 */
	public void PumpaEltavolit() {
		// ha van pumpa a ciszterna korul
		if (termeltpumpak.size() >= 1)
			termeltpumpak.remove(termeltpumpak.size() - 1);
		view.Notify(this);
	}

	/**
	 * A ciszternak ezzel a fuggvennyel keszitenek uj pumpakat minden hivas utan 0-2
	 * szam intervallumban ha determinisztikus. Ha a randomizálás ki van kapcsolva akkor 1 pumpát termel.
	 */
	public void PumpaKeszit() {
		Random rand = new Random();

		//Generálunk egy random double-t 0 és 1 között
		double randomNum = rand.nextDouble();
		if (!doRandomThings) // ha determinisztikus a mukodes
			randomNum = 1; //Akkor garantaltan termel 1 pumpat
		if (randomNum > 0.8) // egyébként 20% eséllyel termel pumpát
			termeltpumpak.add(new Pumpa(false));

		view.Notify(this);
	}

	/**
	 * A ciszterna egy magához kapcsolódó szabad végű csövet hoz létre.
	 */
	public void CsovetKeszit() {
		Random rand = new Random();
		//Generálunk egy random double-t 0 és 1 között
		double randomNum = rand.nextDouble();
		if (!doRandomThings) // ha determinisztikus a mukodes
			randomNum = 1; //Akkor garantaltan termel 1 pumpat
		if (randomNum > 0.8) // egyébként 20% eséllyel termel pumpát
		{
			Cso ujcso = new Cso();
			ujcso.SzomszedHozzaad(this);
			this.SzomszedHozzaad(ujcso);
		}
		view.Notify(this);
	}

	/**
	 * getter a termeltpumpakra
	 */
	public List<Mezo> getTermeltPumpak() {
		return this.termeltpumpak;
	}

	/**
	 * A szomszédok listájában mindenkire meghívja a VizetCsokkent függvényt. Azaz
	 * minden rá csatlakoztatott csőtől elveszi a benne található vizet. (ha nincs
	 * víz a csőben, nem kap vizet) Meghívja a PumpatKeszit, CsovetKeszit
	 * függvényeket is.
	 * @Override
	 */
	public void Frissit() {
		ArrayList<Mezo> szomszedok = GetSzomszedok(); // GetLeszedhetoSzomszedok lesz
		for (Mezo cso : szomszedok) {
			try {
				int menny1 = cso.getVizmennyiseg();
				cso.VizetCsokkent(1);
				int menny2 = cso.getVizmennyiseg();
				if(menny1 > menny2)
					pontsz.szereloPontotNovel(1);
			} catch (Exception e) {
				/* Hmm ez nem szép itt ;) */}
		}
		this.CsovetKeszit();
		this.PumpaKeszit();
		view.Notify(this);
	}
}