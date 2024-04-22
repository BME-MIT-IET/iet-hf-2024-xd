package projlab;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.ZoneView;

/**
 * A szerelők tartják karban a csőhálózatot. ők javítják meg az elromlott
 * pumpákat, ők állítják át a pumpákat, hogy mindig a lehető legtöbb víz tudjon
 * áthaladni a hálózaton, és ha egy cső kilyukad, az ő dolguk a cső megfoltozása
 * is. A szerelők dolga a ciszternáknál lévő szabad csövekkel a hálózat
 * kapacitásának növelése.
 */
public class Szerelo extends Jatekos {

	public Szerelo() {
		view = new SzereloView();
	}

	/**
	 * A szerelő megjavít egy mezőt, amin éppen áll
	 */
	@Override
	public void Javit() {
		helyzet.Megjavit();
		maxHatizsakKapacitas = 5;
	}

	/**
	 * Ezzel a függvénnyel lecsatolhat a szerelő egy csőnek az egyik végét a mezőről
	 * amin éppen áll és ez a csoHatizsak-ba kerül. Ekkor a cső szomszédai közül a
	 * mező, a mező szomszédai közül pedig a cső kikerül. A cső ezután bekerül a
	 * hátizsákba.
	 * 
	 * @param m - A helyzetről lecsatolandó mező
	 */
	@Override
	public void CsovetLecsatol(Mezo m) {
		// Ha nincs ilyen leszedhető szomszéd, vagy nincs táska hely, semmi sem történik
		if (!helyzet.GetLeszedhetoSzomszedok().contains(m) || csoHatizsak.size() > maxHatizsakKapacitas) {
			return;
		}

		// Frissítjük a referenciákat
		csoHatizsak.add(m);
		helyzet.SzomszedTorol(m);
		m.SzomszedTorol(helyzet);
		//Notify-olni kell a cső viewját
		m.view.Notify(m);

	}

	/**
	 * A szerelő lecsatol egy teljes csövet (mindkét végét) a mezőről amin éppen
	 * áll, és ez a csoHatizsak-ba kerül. Ilyenkor a cső kikerül mindkét
	 * szomszédjának a szomszedok listájából, és a cső referenciája bekerül a
	 * hátizsákba kétszer (mind a két vége, hiszen felcsatolásnál majd kétszer lehet
	 * felcsatolni ugyanazt a csövet).
	 * 
	 * @param m - A helyzetről lecsatolandó mező
	 */
	@Override
	public void EgeszCsovetLecsatol(Mezo m) {
		// Ha nincs ilyen leszedhető szomszéd, vagy nincs táska hely, semmi sem történik
		if (!helyzet.GetLeszedhetoSzomszedok().contains(m) || csoHatizsak.size() > maxHatizsakKapacitas) {
			return;
		}

		// Lecsatoljuk az összes végét
		for (Mezo sz : m.GetSzomszedok()) {
			sz.SzomszedTorol(m);
		}
		m.GetSzomszedok().clear();
		
		// Hátizsákba tesszük az összes végét
		for (int i = 0; i < m.maxSzomszedok; i++) {
			csoHatizsak.add(m);
		}
		//Notify-olni kell a cső viewját
		m.view.Notify(m);

	}

	/**
	 * A szerelő kivesz egy csövet a hátizsákjából (ha úgy nézzük akkor az egyik
	 * végét) és felcsatolja a mezőre, amin éppen áll. Ekkor mind a mező és a cső
	 * "szomszedok" listája frissül, és a cső ezen vége kikerül a hátizsákból.
	 */
	@Override
	public void CsovetFelcsatol() {
		// Üres hátizsák esetén nem történik semmi
		if (csoHatizsak.size() == 0) {
			return;
		}

		// Kiválasztunk egy felcsatolandót a hátizákból.
		// TODO: Lehet hogy még változik, hogy melyiket csatoljuk fel ilyenkor.
		// Egyenlőre a legutóbbi felvett csővéget választjuk.
		Mezo felcsatolando = csoHatizsak.get(csoHatizsak.size() - 1);

		// Megpróbáljuk felcsatolni
		Boolean sikerult = helyzet.SzomszedFelcsatol(felcsatolando);

		// Ha sikerült frissítjük a referenciákat
		if (sikerult) {
			felcsatolando.SzomszedHozzaad(helyzet);
			csoHatizsak.remove(felcsatolando);
		}

	}

	/**
	 * A szerelo egy pumpat akar felvenni annal a ciszternanal ahol eppen
	 * tartozkodik Eloszor megnezi, hogy van-e eleg hely a pumpaHatizsakjaban, majd
	 * megnezi a ciszternanal talalhato-e pumpa Ha a feltetelek teljesulnek, a
	 * ciszterna termeltpumpak kollekciobol kiveszi az utolso pumpat es belerakja a
	 * hatizsakjaba.
	 */
	@Override
	public void PumpatFelvesz() {
		// Ha nincs hely a hátizsákban vagy nincs termelt pumpa, semmi sem történik
		if (pumpaHatizsak.size() >= maxHatizsakKapacitas || helyzet.getTermeltPumpak().size() == 0) {
			return;
		}

		// Szerelő berakja a hátizsákba a ciszterna termeltpumpak listájában található
		// utolsó pumpát
		List<Mezo> termeltPumpak = helyzet.getTermeltPumpak();
		pumpaHatizsak.add(termeltPumpak.get(termeltPumpak.size() - 1));

		// Ciszterna eltavolítja azt a pumpát amit felvett a szerelő (ez is az utolsót
		// távolítja el)
		helyzet.PumpaEltavolit();
	}

	/**
	 * Ha a szerelo uj pumpat akar helyezni a csorendszerbe, ezt a fuggvenyt
	 * hasznalja A szerelonek egy csovon kell allnia, ennek a csonek hivja meg a
	 * PumpaEpit fuggvenyet A szerelo pumpaHatizsak kollekciojabol ki is torli a
	 * pumpat, amit elhelyezett.
	 */
	@Override
	public void PumpatEpit() {
		if(pumpaHatizsak.size() == 0)
			System.out.println("Nincs pumpa a hatizsakban");
		else
			helyzet.PumpaEpit();
	}
}
