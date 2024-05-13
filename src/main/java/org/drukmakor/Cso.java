package org.drukmakor;

import java.util.ArrayList;
import java.util.Random;

import static org.drukmakor.Main.pontsz;


/**
 * A cső osztály a pálya egy passzív eleme.
 * ő felelős a víz szállításáért.
 * Ha kijukad akkor a benne levő víz kifolyik és a sivatagban elveszik.
 */
public class Cso extends Mezo {
	//private int maxJatekosok = 1;

	/**
	 * A cső konstruktora
	 * Beállítja a maxJatekosok változót 1-re
	 */
	public Cso() {
		super(1);
		view = new CsoView();
		maxSzomszedok = 2;
		lyukCooldown = 0;
		csuszos = 0;
		ragados = 0;
	}

	private int vizmennyiseg;

	private int lyukCooldown;
	private int csuszos;
	private int ragados;
	Jatekos ragadossaTette;

	/**
	 * A szerelő megjavít egy elemet, amin éppen áll
	 */
	@Override
	public void Megjavit() {
		setMukodik(true);
		view.Notify(this);
	}

	public boolean JatekosElenged(Jatekos j) {
		return ragados == 0 || j == ragadossaTette;
	}


	@Override
	public void JatekosEltavolit(Jatekos j) {
		ragadossaTette = null;
		super.JatekosEltavolit(j);
	}

	@Override
	public boolean JatekosElfogad(Jatekos j) {
		if (getJatekosok().size() < maxJatekosok) {
			super.JatekosElfogad(j);
			j.setHelyzet(this);

			if (csuszos != 0) {
				if (GetSzomszedok().size() > 0 && doRandomThings) {
					j.Lep(GetSzomszedok().get(new Random().nextInt(2)));
				}
				else {j.Lep(GetSzomszedok().get(0));}
			}

			return true;
		}

		return false;
	}


	/**
	 * Ezt a fuggveny a szerelo hivja meg azon a csovon amin all, mikor uj pumpat akar lerakni
	 * A fuggveny az eredeti csovet "kette vagja", ugy hogy
	 * az "eredeti cso lesz az elso fele "a masodik fele pedig  egy uj cso lesz amit a fuggveny hoz letre
	 * es  a ketto koze a szerelo egy pumpat rak
	 */
	@Override
	public void PumpaEpit() {
		ArrayList<Mezo> voltszomszedok = GetSzomszedok();
		Cso ujcso = new Cso();
		Jatekos jatekos = getJatekosok().get(0);
		Mezo pumpa = jatekos.getPumpaHatizsak().remove(0);

		// Pumpa nézetének láthatóvá tétele a játékos helyén
		ObjectView jatekosView = jatekos.getView();
		ObjectView pumpaView = pumpa.getView();
		pumpaView.setHely(jatekosView.getKozepX(), jatekosView.getKozepY());
		pumpaView.setLathato(true);

		pumpa.SzomszedFelcsatol(this);
		pumpa.SzomszedFelcsatol(ujcso);
		pumpa.Atallit(ujcso, this);

		ujcso.SzomszedHozzaad(pumpa);
		ujcso.SzomszedHozzaad(voltszomszedok.get(0));

		Mezo torlendo = voltszomszedok.get(0);
		torlendo.SzomszedTorol(this);
		voltszomszedok.get(0).SzomszedHozzaad(ujcso);
		SzomszedTorol(torlendo);
		SzomszedHozzaad(pumpa);

		view.Notify(this);
		getJatekosok().get(0).getView().Notify(getJatekosok().get(0)); //Notify-olni kell a rajta álló játékost is
		pumpa.getView().Notify(pumpa);
	}

	/**
	 * A függvény kilyukasztja a csövet, azaz átálllítja a mukodik változoót arra, hogy nem működik (false)
	 * @Override
	 */
	@Override
	public void Kilyukaszt() {
		if (lyukCooldown == 0) {
			setMukodik(false);
			if (vizmennyiseg > 0)
			{
				pontsz.szabotorPontotNovel(1);
			}
			vizmennyiseg = 0;
			lyukCooldown = 5; //5 Körig nem lehet majd kilyukasztani a csövet
		}

		view.Notify(this);
	}

	/**
	 * <p>A függvény megnöveli a csőbe levő víz mennyiségét megadott értékkel (nem növekedhet MAXVÍZ felé).</p>
	 * Ezt a Pumpa {@link Pumpa}, Forrás {@link Forras}  fogja meghívni
	 * @param meret a víz mennysége amit a csőbe pumpálunk
	 * @Override
	 */
	@Override
	public void VizetNovel(int meret) throws Exception {
		if (!mukodik){
			pontsz.szabotorPontotNovel(1);
			return;
		}
		if (meret + vizmennyiseg > 1) {
			return;
		}
		else{
			vizmennyiseg += meret;
		}
		view.Notify(this);
	}

	/**
	 * <p>A függvény csökkenti a csőbe levő víz mennyiségét megadott értékkel (nem csökkenhet 0 alá).</p>
	 * Ezt a Pumpa {@link Pumpa}, Forrás {@link Ciszterna}  fogja meghívni
	 * @param meret a víz mennysége amit a csőből kiszívunk.
	 */
	@Override
	public void VizetCsokkent(int meret) throws Exception {
		if (meret <= vizmennyiseg) {
			vizmennyiseg -= meret;
			view.Notify(this);
		}
	}

	public void Csuszik() {
		csuszos = 5;
		view.Notify(this);
	} //3 körig lesz csúszós a cső

	public void Ragad() {
		if (ragados == 0) {
			ragados = 5; //3 körig lesz ragadós a cső
			ragadossaTette = getJatekosok().get(0);
			view.Notify(this);
		}
	}

	/**
	 * Ez a vizmennyiség változóhoz egy getter, visszaadja a csőben levő víz értkét
	 *
	 * @return int típusú, azt adja vissza, hogy mennyi víz van a csőben éppen.
	 */
	@Override
	public void Frissit() throws Exception {
		if (ragados > 0) {
			ragados -= 1;
		}
		if (csuszos > 0) {
			csuszos -= 1;
		}
		if (lyukCooldown > 0) {
			lyukCooldown -= 1;
		}
		view.Notify(this);
	}

	public void SetVizmennyiseg(int viz) {
		vizmennyiseg = viz;
		view.Notify(this);
	}

	public int getVizmennyiseg() {
		return vizmennyiseg;
	}


	/**
	 * Egy mező szomszádaihoz hozzáad egy új mezőt
	 *
	 * @param m a hozzaadnado mező
	 */
	@Override
	public void SzomszedHozzaad(Mezo m) {
		ArrayList<Mezo> szomszedok = GetSzomszedok();
		if (szomszedok.size() < 2)
			szomszedok.add(m);
		view.Notify(this);
	}

	public boolean SzomszedFelcsatol(Mezo m) {
		return false;
	}
	public int getLyukCooldown() {
		return lyukCooldown;
	}

	public int getCsuszos() {
		return csuszos;
	}

	public int getRagados() {
		return ragados;
	}
	public Jatekos getRagadossaTette() {
		return ragadossaTette;
	}
	public boolean getMukodik() {
		return mukodik;
	}
}
