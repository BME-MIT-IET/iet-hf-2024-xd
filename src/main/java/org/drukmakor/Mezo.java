package org.drukmakor;

import java.util.ArrayList;
import java.util.List;

/**
 * A Mező egy absztrakt osztály, ami a csőrendszer összes elemét reprezentálja.
 * Mivel absztrakt az osztály ezért példányosítani nem lehet. Mező lehet egy cső
 * vagy egy aktívelem.
 */
public abstract class Mezo {
	/**
	 * Csináljon véletlenszerű dolgokat a program (true), vagy determinisztikus
	 * legyen (false)
	 */
	static boolean doRandomThings = true;

	private int vizmennyiseg;

	/**
	 * Hamis, ha nem működik a mező.
	 */
	protected boolean mukodik;

	/**
	 * Maximum hány szomszédja lehet egy mezőnek. Gyerek osztályonként különböző
	 */
	protected int maxSzomszedok;

	/**
	 * Azt tárolja, hogy hány játékos állhat egy mezőn. Ez felül lesz írva a
	 * leszármazottak konstruktoraiban.
	 */
	protected int maxJatekosok = 0;

	/**
	 * A mezőn éppen álló játékosok listája.
	 */
	private ArrayList <Jatekos> jatekosok;

	/**
	 * A mező szomszédjainak listája. A listában minden olyan mező szerepel, ami rá
	 * van csatlakoztatva.
	 */
	private ArrayList <Mezo> szomszedok;

	/**
	 * A mezőhöz tartozó nézet
	 */
	protected ObjectView view;

	/**
	 * Default konstruktor
	 */
	public Mezo() {
		jatekosok = new ArrayList<Jatekos>();
		szomszedok = new ArrayList<Mezo>();
		maxSzomszedok = 5;
	}

	/**
	 * Létrehoz egy Mezőt a mezőre jellemző maximum játékosok számával.
	 * @param maxJatekosok - Maximum ennyi játékos állhat rajta
	 */
	public Mezo(int maxJatekosok) {
		mukodik = true;
		jatekosok = new ArrayList<Jatekos>();
		szomszedok = new ArrayList<Mezo>();
		this.maxJatekosok = maxJatekosok;
	}

	/**
	 * Adott mezo szomszedjait kerdezi le
	 * @return Adott mezo szomszedjai egy kollekcioban
	 */
	public ArrayList<Mezo> GetSzomszedok() {
		return szomszedok;
	}

	/**
	 * Működése hasonló a GetSzomszedokhoz, viszont csak a játékosok által
	 * lecsatlakoztatható szomszédokkal tér vissza. Default implementációként minden
	 * szomszéddal visszatér, de a leszármazottaktól függ, hogy hogyan
	 * implementálják.
	 * 
	 * @return lecsatlakoztatható szomszédok
	 */
	public ArrayList<Mezo> GetLeszedhetoSzomszedok() {
		return GetSzomszedok();
	}

	/**
	 * Visszatérési értéke alapértelmezetten igaz, vagyis engedi ellépni a játékost.
	 * Az olyan leszármazottak, akik saját logikájuk szerint nem engedik el a
	 * játékosokat, felüldefiniálják ezt a függvényt. (Egyelőre csak a Cso
	 * definiálja felül.)
	 * 
	 * @param j - A játékos aki lépni akar
	 * @return Léphet a játékos vagy sem
	 */
	public boolean JatekosElenged(Jatekos j) {
		return true;
	}

	/**
	 * A függvény hozzáadja a "j" játékost a mezőhöz (a mező jatekosok listájához)
	 * ha még nincs maxJatekosok darab játékos a listában. A visszatérés akkor igaz,
	 * ha sikerült a hozzáadás. (A Cso felüldefiniálja.)
	 * @param j A hozzáadandó játékos.
	 * @return Igaz, ha sikerült a hozzáadás. Akkor sikerül, ha a mezőn még nincs a
	 *         maximális játékosok száma.
	 */
	public boolean JatekosElfogad(Jatekos j) {
		if (jatekosok.size() < maxJatekosok) {
			jatekosok.add(j);
			j.setHelyzet(this);
			return true;
		}
		return false;
	}

	/**
	 * Egy játékos eltávolítása egy mezőről
	 * @param j a játékos referenciája
	 */
	public void JatekosEltavolit(Jatekos j) {
		jatekosok.remove(j);
	}

	/**
	 * Ha a mezőnek lehet be- és kimenete, felüldefiniálhatja.
	 * @param kimenet amire át lesz állítva a kimenet
	 * @param bemenet amire át lesz állítva a bemenet
	 */
	public void Atallit(Mezo kimenet, Mezo bemenet) {
	}

	/**
	 * A szerelő megjavít egy elemet, amin éppen áll. Default implementációja üres.
	 */
	public void Megjavit() {
	}

	/**
	 * A szabotőr kilyukaszt egy csövet. Default implementációja üres.
	 */
	public void Kilyukaszt() {
	}

	/**
	 * A szerelő lehelyez egy új pumpát arra a csőre, amin éppen áll, azaz
	 * kettévágja és új pumpát rak a kettő közé. A szerelőknél lévő pumpák száma
	 * csökken. Default implementációja üres.
	 */
	public void PumpaEpit() {
	}

	/**
	 * Hozzáadja a paraméterként átadott mezőt a szomszédok kollekcióhoz.
	 * 
	 * @param m - Hozzáadandó szomszéd
	 */
	public void SzomszedHozzaad(Mezo m) {
		if (szomszedok.size() < maxSzomszedok) {
			szomszedok.add(m);
			view.NotifyField(this);
		}
	}

	/**
	 * Az "m" paraméterként kapott mezőt eltávolítja a "szomszedok” listából.
	 * 
	 * @param m Törlendő szomszéd
	 */
	public void SzomszedTorol(Mezo m) {
		szomszedok.remove(m);
		view.NotifyField(this);
	}

	/**
	 * A Játékosok ezen a függvényen keresztül csatolhatnak fel elemeket a mezőkhöz.
	 * A visszatérési értékben megadja, hogy sikeres volt-e a felcsatolás. Default
	 * implementációja megegyezik a "SzomszedHozzaad" függvénnyel. Minden
	 * leszármazott amelyikre nem lehet felcsatolni, felüldefiniálhatja.
	 * 
	 * @param m - Felcsatolandó
	 * @return Sikerült-e a felcsatolás
	 */
	public boolean SzomszedFelcsatol(Mezo m) {
		if (szomszedok.size() < maxSzomszedok) {
			szomszedok.add(m);
			view.NotifyField(this);
			return true;
		}
		return false;
	}

	/**
	 * Csak ciszterma osztalyon ertelmezett szerelo hivja meg ciszternara. Default
	 * implementációja üres.
	 */
	public void PumpaEltavolit() {
	}

	/**
	 * Ez a függvény Csökkenti a csőben levő víz mennyiségét
	 * @param meret ez egy egész érték, amivel csökken a víz értéke
	 * @throws Exception ezt dobja ha több vizet szeretnénk a csőből kinyerni, mint ami benne van
	 */
	public void VizetCsokkent(int meret) throws Exception {
	}

	/**
	 * Ez a függvény Növeli a csőben levő víz mennyiségét
	 * @param meret ez egy egész érték, amivel csökken a víz értéke
	 * @throws Exception ezt dobja ha több vizet szeretnénk a csőbe pumpálni, mint ami belefér
	 */
	public void VizetNovel(int meret) throws Exception {
	}

	/**
	 * Ez a függvény azért felelős, hogy a mező működik-e vagy nem.
	 * 
	 * @param status Ez egy igaz/hamis értéket felvehető érték ami azt mondja meg,
	 *               hogy mire állítsuk a mező működik értékét
	 */
	public void setMukodik(boolean status) {
		mukodik = status;
		view.NotifyField(this);
	}

	/**
	 * Ez a getter azért felelős, hogy visszaadja mennyi víz van éppen a csőben.
	 * @return vizmennyiseg - visszaadja, hogy mennyi víz van eppen a csőben
	 */
	public int getVizmennyiseg() {
		return vizmennyiseg;
	}

	/**
	 * getter a jatekosok attributumra
	 */
	public List<Jatekos> getJatekosok() {
		return this.jatekosok;
	}

	/**
	 * Csak ciszterma osztalyon ertelmezett getter a ciszterna termeltpumpak
	 * attributumra
	 */
	public List<Mezo> getTermeltPumpak() {
		return null;
	}

	/**
	 * Visszaadja hogy maximum hány játékos lehet a mezőn.
	 * @return maxJatekosok
	 */
	public int getMaxJatekosok() {
		return maxJatekosok;
	}

	/**
	 * Visszaadja hogy maximum hány szomszédja lehet mezőnek.
	 * 
	 * @return maxSzomszedok
	 */
	public int getMaxSzomszedok() {
		return maxSzomszedok;
	}

	/**
	 * Csúszóssá teszi a mezőt ha implementálja.
	 */
	public void Csuszik() {
	}

	/**
	 * Ragadóssá teszi a mezőt ha implementálja.
	 */
	public void Ragad() {
	}

	public void SetVizmennyiseg(int mennyiseg) {
		vizmennyiseg = mennyiseg;
		view.NotifyField(this);
	}

	public boolean getMukodik() {
		return mukodik;
	}

	public ObjectView getView() {
		return view;
	}

	/**
	 * Ha a mezőnek lehet kimenete, akkor felüldefiniálhatja a függvényt.
	 * @return kimenet
	 */
	public Mezo getKimenet() {
		return null;
	}

	/**
	 * Ha a mezőnek lehet bemenete, akkor felüldefiniálhatja a függvényt.
	 * 
	 * @return bemenet
	 */
	public Mezo getBemenet() {
		return null;
	}

	/**
	 * Tesztekhez kellő függvények állapot lekérdezésre, don't touch them, don't use
	 * them!
	 * 
	 * @return semmit, ne használd
	 */
	public int getLyukCooldown() {
		return -1;
	}

	public int getCsuszos() {
		return -1;
	}

	public int getRagados() {
		return -1;
	}
	public Jatekos getRagadossaTette() {
		return null;
	}

	public void PumpatKeszit() {
	}

	public void CsovetKeszit() {
	}

	/**
	 * Frissíti a mezőt. Ez a függvény alapból nem csinál semmit, a Mezo összes
	 * gyerekosztály felüldefiniálja.
	 * @throws Exception
	 */
	public abstract void Frissit() throws Exception;

	//Default
	public void PumpaKeszit() {
	}
}