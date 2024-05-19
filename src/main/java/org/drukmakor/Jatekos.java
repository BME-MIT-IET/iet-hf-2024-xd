package org.drukmakor;
import java.util.ArrayList;
import java.util.List;


/**
 * A jatekos osztály egy irányítható karaktert valósít meg a játékban,
 * ez lehet Szabotőr{@link Szabotor} vagy Szerelő{@link Szerelo}
 */
public abstract class Jatekos {

	protected Mezo helyzet;		//a játékos helyzete
	protected int maxHatizsakKapacitas;		//a játékos hátizsákjainak maximum kapacitása
	protected ArrayList<Mezo> pumpaHatizsak;		//a játékos hátizsákja, ami pumpákat tartalmaz
	protected ArrayList<Mezo> csoHatizsak;		//a játékos hátizsákja, ami csöveket tartalmaz

	protected ObjectView view; // A játékoshoz tartozó grafkus nézet

	public Jatekos() {
		pumpaHatizsak = new ArrayList<>();
		csoHatizsak = new ArrayList<>();
		maxHatizsakKapacitas = 5;
	}


	/**
	 * A játékos lépést kezdeményez egy általa választott mezőre
	 * @param m a választott mező referenciája
	 */
	public void Lep(Mezo m) {
		Mezo tmp = getHelyzet();
		if (helyzet == null){helyzet = m; m.JatekosElfogad(this);}
		else {
			boolean elengedve = helyzet.JatekosElenged(this);
			List<Mezo> szomszedok = helyzet.GetSzomszedok();

			if (elengedve && szomszedok.contains(m)) {
				boolean elfogadva = m.JatekosElfogad(this);
				if (elfogadva)
					tmp.JatekosEltavolit(this);
			}
		}

		view.NotifyPlayer(this);
	}

	/**
	 * A játékos kilyukasztja azt az elemet, amin éppen áll.
	 */
	public void Lyukaszt(){	helyzet.Kilyukaszt(); }

	/**
	 * A játékos átállítja a pumpa be és kimenetét
	 */
	public void Allit(Mezo kimenet, Mezo bemenet) {	helyzet.Atallit(kimenet, bemenet); }

	/**
	 * Nincs alap implementáció, a szerelő osztály felüldefiniálja
	 */
	public void Javit(){}

	/**
	 * Nincs alap implementáció, a szerelő osztály felüldefiniálja
	 */
	public void PumpatFelvesz(){}

	/**
	 * Nincs alap implementáció, a szerelő osztály felüldefiniálja
	 * @param m a lecsatolandó mező referenciája
	 */
	public void CsovetLecsatol(Mezo m){}

	/**
	 * Nincs alap implementáció, a szerelő osztály felüldefiniálja
	 * @param m a lecsatolandó mező referenciája
	 */
	public void EgeszCsovetLecsatol(Mezo m){}

	/**
	 * Nincs alap implementáció, a szerelő osztály felüldefiniálja
	 */
	public void CsovetFelcsatol(){}

	/**
	 * Nincs alap implementáció, a szerelő osztály felüldefiniálja
	 */
	public void PumpatEpit(){}

	/**
	 * A játékos ragadóssá teszi a mezőt, amin áll
	 */
	public void RagadossaTesz(){ helyzet.Ragad(); }

	/**
	 * Jatekos tartózkodási helyéhez tartózó szomszédok lekerdezese
	 * @return tartózkodási helyének szomszédai
	 */
	public ArrayList<Mezo> GetSzomszedok(){ return helyzet.GetSzomszedok(); }

	/**
	 * Lerérdezi a játékoshoz tartozó grafuikus nézetet.
	 * @return játékos nézete
	 */
	public ObjectView getView() {
		return view;
	}

	/**
	 * A játékos helyzetének referenciáját adja vissza.
	 * 
	 * @return a játékos helyzete
	 */
	public Mezo getHelyzet(){ return helyzet; }

	/**
	 * A játékos helyzetének beállítása
	 * @param m - az új helyzet referenciája
	 */
	public void setHelyzet(Mezo m) {
		helyzet = m;
		view.NotifyPlayer(this);
	}

	/**
	 * Visszaadja a hátizsákok maximum kapacitását
	 * @return maximum kapacitás
	 */
	public int getMaxHatizsakKapacitas() { return this.maxHatizsakKapacitas; }

	/**
	 * Beállítja a hátizsákok maximum kapacitását
	 * @param c - Az új kapacitás értéke
	 */
	public void setMaxHatizsakKapacitas(int c) { maxHatizsakKapacitas = c; }

	/**
	 * Visszaadja a pumpákat tartalmazó hátizsákot
	 *
	 * @return a hátizsákban található pumpák listája
	 */
	public List<Mezo> getPumpaHatizsak(){	return this.pumpaHatizsak; }

	/**
	 * Visszaadja a csöveket tartalmazó hátizsákot
	 * @return a hátizsákban található csövek listája
	 */
	public ArrayList<Mezo> getCsoHatizsak() { return csoHatizsak; }

	/**
	 * Nincs alap implementáció, a szabotőr osztály felüldefiniálja
	 */
	public void CsuszossaTesz() { }
}
