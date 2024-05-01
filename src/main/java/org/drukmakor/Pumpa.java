package org.drukmakor;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A pumpák a víz folyását szabályozzák. Minden pumpához adott számú
 * csövet(maxCso) lehet csatlakoztatni, de ezekből mindig 1 kiválasztott cső a
 * bemeneti cső(bemenet), amiből a pumpa magába tud szívni vizet és mindig 1 cső
 * a kimeneti cső(kimenet),amibe a pumpa vizet tud pumpálni. A pumpák véletlen
 * időközönként elromlanak. Ilyenkor se vizet nem tudnak magukba szívni, se
 * kipumpálni. A pumpákban található víz (vízmennyiség) akkor nő meg ha
 * bemenetéről szívja a vizet, illetve akkor csökken, ha kimenetére kipumpálja a
 * vizet (MAXVIZ attribútumnyit szív és kipumpál). Maximum 5 egységnyi víz lehet
 * egy pumpában Ha a pumpa nem tud vizet átadni a kimenetére (mert a kimenet
 * tele van vízzel), akkor a víz a pumpában marad. A pumpa csak akkor tud vizet
 * szívni és kipumpálni, ha működik(Mezo ososztaly mukodik attributuma). A
 * pumpálásnak van felsőkorlátja(MAXVIZ), maximum ennyi vizet szívhat, illetve
 * kipumpálhat a csövekből.
 * 
 */
public class Pumpa extends Mezo {

	private static int MAXVIZ;
	private int maxCso;
	private int vizmennyiseg;
	private Mezo bemenet;
	private Mezo kimenet;

	public Pumpa() {
		this(true);
	}

	public Pumpa(boolean lathato) {
		super(Integer.MAX_VALUE);
		view = new PumpaView();
		view.setLathato(lathato);
		MAXVIZ = 1;
		maxCso = 5;
		vizmennyiseg = 0;
		bemenet = null;
		kimenet = null;
		maxSzomszedok = 5;
	}


	/**
	 * Egy mező szomszédaihoz hozzáad egy új mezőt A maxCso attribútum határozza meg
	 * mennyi szomszédja(rácsatlakotatt csövek) lehet egy pumpának. A szomszédok
	 * listáját a Mezo ősosztályból örökli.
	 * 
	 * @param m a hozzáadandó mező
	 * @Override
	 */
	public void SzomszedHozzaad(Mezo m) {
		ArrayList<Mezo> szomszedok = super.GetSzomszedok(); // Mezo osztaly attributuma
		if (szomszedok.size() < maxCso)
			szomszedok.add(m);
		view.Notify(this);
	}

	/**
	 * Egy játékos átállítja egy pumpa be és kimenetét. Ha valamelyik paraméterben
	 * kapott referencia nem szerepel a pumpa szomszédai között, nem történik meg az
	 * átállítás Ha a paraméterek szerepelnek a szomszédok között,akkor a pumpa
	 * bemenetét átállítja a paraméterként kapott bemenetre és a kimenetét is a
	 * paraméterben kapott kimenetre állítja.
	 * 
	 * @param kimenet amire át lesz állítva a kimenet
	 * @param bemenet amire át lesz állítva a bemenet
	 * @Override
	 */

	public void Atallit(Mezo bemenet, Mezo kimenet) {
		// megnezzuk szomszedok-e
		ArrayList<Mezo> szomszedok = GetSzomszedok(); // Mezo osztaly attributuma
		for (int i = 0; i < szomszedok.size(); ++i) {
			if (bemenet == szomszedok.get(i)) {
				this.bemenet = bemenet;
			}
			if (kimenet == szomszedok.get(i)) {
				this.kimenet = kimenet;
			}
		}
		view.Notify(this);
	}

	/**
	 * Atallitja a mukodik attributumot true-ra.
	 * 
	 * @Override
	 */
	public void Megjavit() {
		setMukodik(true);
		view.Notify(this);
	}

	/**
	 * Felüldefiniált metódus a Mezo osztályból. A pumpa e függvény hatására
	 * (determinisztikusan vagy véletlenszerűen (az aktivitásdiagrammon 50% esély
	 * van erre)) elromlik (mukodik attributum értékét false-ra állítja), ekkor nem
	 * tud vizet szívni, kipumpálni. Ha a pumpa működik (mukodik attributum értéke
	 * true) a bemenetéről először beszívja a vizet a tartályába (ha van víz maximum
	 * egy egységnyivel nő a vizmennyiseg attribútum értéke, mivel a csövek maximum
	 * egy egységnyi vizet tárolhatnak). Ezután kipumpálja a vizet a kimenetére (ha
	 * van víz akkor maximum eggyel csökken a vízmennyiségattribútum). Ha a kimenet
	 * tele van vízzel, akkor a tartályban marad a víz. Ha a bemenetéről nem tud
	 * vizet szívni, akkor a tartályában lévő vizet adja át a kimenetnek (ha a
	 * tartályban nincs víz nem kap a kimenet vizet)
	 * 
	 * @Override
	 */
	public void Frissit() {
		try {
			Random rand = new Random();
			if (rand.nextDouble() > 0.9) { //10% esely van a hibara
				if (doRandomThings) // ha determinisztikus a mukodes
					this.setMukodik(false);
			}
			// ha mukodik akkor pumpalhat
			if (this.mukodik) {
				// ha a pumpa tartalya ures vagy nincs tele
				if (vizmennyiseg < 5) {
					if (bemenet != null) {
						int vizet_sziv = bemenet.getVizmennyiseg(); // annyi vizet sziv ki amennyi a bemenetben van
						bemenet.VizetCsokkent(vizet_sziv);
						vizmennyiseg += vizet_sziv;
					}
					// vizet átad kimenetnek, ha van viz
					if(kimenet!=null) {
					if (vizmennyiseg >= 1) {
						int vizet_pumpal = MAXVIZ - kimenet.getVizmennyiseg(); // MAXVIZ=1, ennyi fér bele egy csőbe
						kimenet.VizetNovel(vizet_pumpal);
						vizmennyiseg -= vizet_pumpal;
					}
				}
				}

				// ha tele van a pumpa
				if (vizmennyiseg == 5) {
					if(kimenet!=null) {
					// eloszor megprobal kimenetre kipumpalni
					int vizet_pumpal = MAXVIZ - kimenet.getVizmennyiseg();
					kimenet.VizetNovel(vizet_pumpal);
					vizmennyiseg -= vizet_pumpal;
					}
					// ha sikerult atadni a vizet a kimenetnek a bemenettol kaphat vizet
					if (vizmennyiseg != 5) {
						if (bemenet != null) {
							int vizet_sziv = bemenet.getVizmennyiseg();
							// Ha tul tolteni a pumpat a bementrol kapott viz, akkor csak annyit toltunk be
							// amennyi a pumpaba befer
							if (vizmennyiseg + vizet_sziv > 5)
								vizet_sziv = 5 - vizmennyiseg;
							bemenet.VizetCsokkent(vizet_sziv);
							vizmennyiseg += vizet_sziv;
						}
					}
				}
			}
		}
		catch (Exception e){}
		view.Notify(this);
	}

	/**
	 * getter a bemenetre
	 */
	public Mezo getBemenet() {
		return this.bemenet;
	}

	/**
	 * getter a Kimenetre Hasznalva van
	 */
	public Mezo getKimenet() {
		return this.kimenet;
	}

	/**
	 * Mennyi víz legyen a pumpában (max 5 lehet es 0-nal tobb)
	 * 
	 * @param meret: viz mennyisege
	 */
	public void SetVizmennyiseg(int meret) {
		// max 5 viz lehet benne
		if (meret > 5) {
			this.vizmennyiseg = 5;
		} else if (meret < 0) {
		} else
			this.vizmennyiseg = meret;

		view.Notify(this);
	}
	public int getVizmennyiseg() {
		return vizmennyiseg;
	}
}